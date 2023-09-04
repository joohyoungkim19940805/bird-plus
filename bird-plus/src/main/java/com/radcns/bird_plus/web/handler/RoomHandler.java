package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.config.security.JwtIssuerType;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.repository.room.RoomFavoritesRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.room.RoomRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class RoomHandler {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	
	@Autowired
	private RoomFavoritesRepository roomFavoritesRepository;
	
	@Autowired
	private WorkspaceInAccountRepository workspaceInAccountRepository;
	
	public Mono<ServerResponse> createRoom(ServerRequest request){
		return accountService.convertJwtToAccount(request)
		.flatMap(account -> request.bodyToMono(RoomEntity.class)
			.flatMap(room ->{
				room.setCreateBy(account.getId());
				return roomRepository.save(room);
			})
			.doOnSuccess(e-> {
				List<RoomType> roomType;
				if(e.getRoomType().equals(RoomType.ROOM_PRIVATE) || e.getRoomType().equals(RoomType.ROOM_PRIVATE)) {
					roomType = List.of(RoomType.ROOM_PRIVATE, RoomType.ROOM_PUBLIC);
				}else{//(e.getRoomType().equals(RoomType.MESSENGER) || e.getRoomType().equals(RoomType.SELF)) {
					roomType = List.of(RoomType.MESSENGER, RoomType.SELF);
				}
				roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), e.getWorkspaceId(), roomType)
				.flatMap(count -> 
					roomInAccountRepository.save(
						RoomInAccountEntity.builder()
						.roomId(e.getId())
						.accountId(account.getId())
						.orderSort(count + 1)
						.workspaceId(e.getWorkspaceId())
						.build()
					)
				)
				.subscribe();
				/*roomRepository.save(
					e.withRoomCode(List.of(accountService.generateAccessToken(account, JwtIssuerType.BOT).getToken()))
					.withCreateAt(null)
				).subscribe();*/
			})
		)//
		.flatMap(room -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, room)), Response.class)
		)
		;
	}
	
	public Mono<ServerResponse> createRoomInAccount(ServerRequest request){
		return ok()
		.contentType(MediaType.TEXT_EVENT_STREAM)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMapMany(account -> {
				Sinks.Many<RoomInAccountEntity> sinks = Sinks.many().unicast().onBackpressureBuffer();
				AtomicInteger emitCount = new AtomicInteger();
				var save = roomInAccountRepository.saveAll(
					request.bodyToFlux(RoomInAccountDomain.CreateRoomInAccountRequest.class)
					.flatMap(createRoomInAccount -> {
						return accountRepository.findByAccountName(createRoomInAccount.getAccountName())
						.filterWhen(e -> 
							workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(createRoomInAccount.getWorkspaceId(), e.getId())
						)
						.flatMap(e-> {
							RoomInAccountEntity roomInAccountEntity = RoomInAccountEntity.builder()
							.accountId(e.getId())
							.workspaceId(createRoomInAccount.getWorkspaceId())
							.roomId(createRoomInAccount.getRoomId())
							.build();
							List<RoomType> roomType;
							if(createRoomInAccount.getRoomType().equals(RoomType.ROOM_PRIVATE) || createRoomInAccount.getRoomType().equals(RoomType.ROOM_PRIVATE)) {
								roomType = List.of(RoomType.ROOM_PRIVATE, RoomType.ROOM_PUBLIC);
							}else{//(e.getRoomType().equals(RoomType.MESSENGER) || e.getRoomType().equals(RoomType.SELF)) {
								roomType = List.of(RoomType.MESSENGER, RoomType.SELF);
							}
							return roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomType
								(roomInAccountEntity.getId(), roomInAccountEntity.getWorkspaceId(), roomType)
									.map(count -> roomInAccountEntity.withOrderSort(count));
						})
						;
					})
				);
				save.doOnNext(e-> {
					sinks.tryEmitNext(e);
					//emitCount.getAndIncrement();
					save.count().subscribe((cnt)->{
						if(emitCount.get() == emitCount.getAndIncrement()) {
							sinks.tryEmitComplete();
						}
					});
				})
				;
				save.subscribe();
				return sinks.asFlux();
			})
		, RoomInAccountEntity.class);
	}
	
	public Mono<ServerResponse> updateRoomInAccount(ServerRequest request){
		return accountService.convertJwtToAccount(request)
		.flatMapMany(account -> 
			roomInAccountRepository.saveAll(
				request.bodyToFlux(RoomInAccountEntity.class)
				.filter(roomInAccount -> roomInAccount.getId() != null)
				.filterWhen(roomInAccount->
					roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), roomInAccount.getRoomId())
				).flatMap(roomInAccount -> roomInAccountRepository.findById(roomInAccount.getId())
					.map(newRoomInAccount->newRoomInAccount.withOrderSort(roomInAccount.getOrderSort()))
				)
			)
		)
		.collectList()
		.flatMap(roomInAccountList -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, null)), Response.class)
		)
		;
	}
	
	public Mono<ServerResponse> createRoomFavorites(ServerRequest request){
		return accountService.convertJwtToAccount(request)
		.flatMap(account -> request.bodyToMono(RoomFavoritesEntity.class)
			.flatMap(roomFavorites -> {
				roomFavorites.setAccountId(account.getId());
				return roomFavoritesRepository.countByAccountIdAndWorkspaceId(account.getId(), roomFavorites.getWorkspaceId())
						.flatMap(count -> roomFavoritesRepository.save(roomFavorites.withOrderSort(count)))
						.map(e->e.withAccountId(null));
				/*return roomRepository.findById(roomFavorites.getRoomId())
					.flatMap(roomEntity -> roomFavoritesRepository.countByAccountIdAndWorkspaceId(account.getId(), roomEntity.getId()))
					.flatMap(count -> roomFavoritesRepository.save(roomFavorites.withOrderSort(count)))
					.map(e->e.withAccountId(null));*/
				
			})
		)
		.flatMap(roomFavorites -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, roomFavorites)), Response.class)
		)
		;
	}

	public Mono<ServerResponse> updateRoomFavorites(ServerRequest request){
		return accountService.convertJwtToAccount(request)
		.flatMapMany(account -> 
			roomFavoritesRepository.saveAll(
					request.bodyToFlux(RoomInAccountEntity.class)
					.filterWhen(roomInAccount->
						Mono.just(roomInAccount.getId() != null)
						.flatMap(bol->roomFavoritesRepository.existsByAccountIdAndRoomId(account.getId(), roomInAccount.getRoomId()))
					).flatMap(roomInAccount -> roomFavoritesRepository.findById(roomInAccount.getId())
						.map(newRoomInAccount->newRoomInAccount.withOrderSort(roomInAccount.getOrderSort()))
					)
				)
		)
		.collectList()
		.flatMap(roomInAccountList -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, null)), Response.class)
		)
		;
	}
	
	public Mono<ServerResponse> searchRoom(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}
				RoomType roomType;
				try {
					roomType = RoomType.valueOf(param.getFirst("roomType"));
				}catch(IllegalArgumentException e) {
					return Mono.error(new RoomException(Result._300));
				}
				
				String roomName = param.getFirst("roomName");
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				Flux<RoomEntity> roomEntityFlux;
				Mono<Long> countMono;
				if(roomName != null && ! roomName.isBlank()) {
					roomEntityFlux = roomRepository.findAllJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(account.getId(), workspaceId, roomName, roomType, pageRequest);
					countMono = roomRepository.countJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(account.getId(), workspaceId, roomName, roomType);
				}else {
					roomEntityFlux = roomRepository.findAllJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, roomType, pageRequest);					
					countMono = roomRepository.countJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, roomType);
				}
				
				return roomEntityFlux.collectList()
				.zipWith(countMono)
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class);
	}

	/*
	public Mono<ServerResponse> searchRoomMyJoined(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}

				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomInAccountRepository.findAllJoinRoomByAccountIdAndWorkspaceId(account.getId(), workspaceId, pageRequest)
				.collectList()
				.zipWith(roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceId(account.getId(), workspaceId))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class);
	}
	*/
	public Mono<ServerResponse> searchRoomMyJoinedAndRoomType(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}
				List<RoomType> roomType;
				try {
					roomType = param.get("roomType").stream().map(RoomType::valueOf).toList();
				}catch(IllegalArgumentException e) {
					return Mono.error(new RoomException(Result._300));
				}
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomInAccountRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, roomType, pageRequest)
				.collectList()
				.zipWith(roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, roomType))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class);
	}
	public Mono<ServerResponse> searchRoomMyJoinedNameAndRoomType(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}
				List<RoomType> roomType;
				try {
					roomType = param.get("roomType").stream().map(RoomType::valueOf).toList();
				}catch(IllegalArgumentException e) {
					return Mono.error(new RoomException(Result._300));
				}
				String roomName = param.getOrDefault("roomName", List.of("")).get(0);
				//if(roomName == null) {
				//	return Mono.empty();
				//}
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomInAccountRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(account.getId(), workspaceId, roomName, roomType, pageRequest)
				.collectList()
				.zipWith(roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(account.getId(), workspaceId, roomName, roomType))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class); 
	}
	
	/*
	public Mono<ServerResponse> searchRoomMyJoinedName(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}

				String roomName = param.getOrDefault("roomName", List.of("")).get(0);
				//if(roomName == null) {
				//	return Mono.empty();
				//}
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomInAccountRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomName(account.getId(), workspaceId, roomName, pageRequest)
				.collectList()
				.zipWith(roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomName(account.getId(), workspaceId, roomName))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class); 
	}
	*/
	public Mono<ServerResponse> searchRoomFavoritesMyJoined(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}

				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomFavoritesRepository.findAllJoinRoomByAccountIdAndWorkspaceId(account.getId(), workspaceId, pageRequest)
				.collectList()
				.zipWith(roomFavoritesRepository.countJoinRoomByAccountIdAndWorkspaceId(account.getId(), workspaceId))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class);
	}
	
	public Mono<ServerResponse> searchRoomFavoritesMyJoinedNema(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}
				String roomName = param.getOrDefault("roomName", List.of("")).get(0);
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomFavoritesRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomName(account.getId(), workspaceId, roomName, pageRequest)
				.collectList()
				.zipWith(roomFavoritesRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomName(account.getId(), workspaceId, roomName))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.map(list -> response(Result._0, list))
		, Response.class)
		;
	}

}
