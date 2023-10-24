package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain.RoomJoinedAccountResponse;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.repository.room.RoomFavoritesRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.room.RoomRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;
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
	
	@Autowired
	private WorkspaceBroker workspaceBorker;
	
	public Mono<ServerResponse> createRoom(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		.flatMap(account -> 
			request.bodyToMono(RoomEntity.class)
			.filterWhen(e-> workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(e.getWorkspaceId(), account.getId()))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(room ->{
				room.setCreateBy(account.getId());
				return roomRepository.save(room)
				.doOnSuccess(e->e.withCreateBy(null));
			})
			.doOnSuccess(e-> {
				List<RoomType> roomType;
				if(e.getRoomType().equals(RoomType.ROOM_PUBLIC) || e.getRoomType().equals(RoomType.ROOM_PRIVATE)) {
					roomType = List.of(RoomType.ROOM_PRIVATE, RoomType.ROOM_PUBLIC);
				}else{//(e.getRoomType().equals(RoomType.MESSENGER) || e.getRoomType().equals(RoomType.SELF)) {
					roomType = List.of(RoomType.MESSENGER, RoomType.SELF);
				}
				
				roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), e.getId())
				.flatMap(bol -> {
					if(bol) {
						return roomInAccountRepository.findByRoomIdAndAccountId(e.getId(), account.getId());
					}else {
						return roomInAccountRepository.findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), e.getWorkspaceId(), roomType)
							.defaultIfEmpty((long)0)
							.flatMap(count -> {
								RoomInAccountEntity roomInAccountEntity = RoomInAccountEntity.builder()
									.roomId(e.getId())
									.accountId(account.getId())
									.orderSort(count + 1)
									.workspaceId(e.getWorkspaceId())
								.build();
								return roomInAccountRepository.save(roomInAccountEntity);
							});
					}
				}).doOnSuccess(result->{
					workspaceBorker.send(
						new ServerSentStreamTemplate<RoomInAccountEntity>(
							result.getWorkspaceId(),
							result.getRoomId(),
							result,
							ServerSentStreamType.ROOM_ACCEPT
						) {}
					);
				})
				.subscribe();
				/*roomRepository.save(
					e.withRoomCode(List.of(accountService.generateAccessToken(account, JwtIssuerType.BOT).getToken()))
					.withCreateAt(null)
				).subscribe();*/
			})
		)//
		.flatMap(room -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response(Result._0, room), Response.class)
		)
		;
	}
	public Mono<ServerResponse> createMySelfRoom(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(e-> workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(workspaceId, e.getId()))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(e->{
				return roomRepository.existsByCreateByAndWorkspaceIdAndRoomType(e.getId(), workspaceId, RoomType.SELF)
				.flatMap(bol -> {
					if(bol) {
						return roomRepository.findByCreateByAndWorkspaceIdAndRoomType(e.getId(), workspaceId, RoomType.SELF);
					}else {
						return roomRepository.save(
							RoomEntity
							.builder()
							.createBy(e.getId())
							.roomName(e.getFullName())
							.workspaceId(workspaceId)
							.roomType(RoomType.SELF)
							.build()
						)
						.doOnSuccess(roomEntity->{
							roomEntity.setCreateBy(null);
							roomInAccountRepository.findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType(e.getId(), workspaceId, List.of(RoomType.SELF))
							.defaultIfEmpty((long)0)
							.flatMap(count -> {
								RoomInAccountEntity roomInAccountEntity = RoomInAccountEntity.builder()
									.roomId(roomEntity.getId())
									.accountId(e.getId())
									.orderSort(count + 1)
									.workspaceId(workspaceId)
								.build();

								return roomInAccountRepository.save(roomInAccountEntity);
							})
							.doOnSuccess(result->{
								workspaceBorker.send(
									new ServerSentStreamTemplate<RoomInAccountEntity>(
										result.getWorkspaceId(),
										result.getRoomId(),
										result,
										ServerSentStreamType.ROOM_ACCEPT
									) {}
								);
							})
							.subscribe();
						})
						;
					}
				});
			})
			.flatMap(e->response(Result._0, e))
		, Response.class);
	}
	
	public Mono<ServerResponse> createRoomInAccount(ServerRequest request){
		return ok()
		//.contentType(MediaType.TEXT_EVENT_STREAM)
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				//Sinks.Many<RoomInAccountDomain.RoomJoinedAccountResponse> sinks = Sinks.many().unicast().onBackpressureBuffer();
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
							.createBy(account.getId())
							.roomJoinedAccountResponse(
								RoomJoinedAccountResponse.builder()
									.roomId(createRoomInAccount.getRoomId())
									.accountName(e.getAccountName())
									.fullName(e.getFullName())
									.job_grade(e.getJobGrade())
									.department(e.getDepartment())
									.roomType(createRoomInAccount.getRoomType())
								.build()
							)
							.build();
							List<RoomType> roomType;
							if(createRoomInAccount.getRoomType().equals(RoomType.ROOM_PRIVATE) || createRoomInAccount.getRoomType().equals(RoomType.ROOM_PRIVATE)) {
								roomType = List.of(RoomType.ROOM_PRIVATE, RoomType.ROOM_PUBLIC);
							}else{//(e.getRoomType().equals(RoomType.MESSENGER) || e.getRoomType().equals(RoomType.SELF)) {
								roomType = List.of(RoomType.MESSENGER, RoomType.SELF);
							}
							return roomInAccountRepository.findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType
								(roomInAccountEntity.getAccountId(), roomInAccountEntity.getWorkspaceId(), roomType)
								.defaultIfEmpty((long)0)
								.map(count -> roomInAccountEntity.withOrderSort(count + 1));
						})
						;
					})
				);
				save.doOnNext((e)-> {
					e.getRoomJoinedAccountResponse().setCreateMils(e.getCreateMils());
					e.getRoomJoinedAccountResponse().setUpdateMils(e.getUpdateMils());
					workspaceBorker.send(
						new ServerSentStreamTemplate<RoomJoinedAccountResponse>(
							e.getWorkspaceId(),
							e.getRoomId(),
							e.getRoomJoinedAccountResponse(),
							ServerSentStreamType.ROOM_IN_ACCOUNT_ACCEPT
						) {}
					);
					
					//e.setAccountId(null);
					
					//sinks.tryEmitNext(e.getRoomJoinedAccountResponse());
					
					e.setRoomJoinedAccountResponse(null);
					workspaceBorker.send(
						new ServerSentStreamTemplate<RoomInAccountEntity>(
							e.getWorkspaceId(),
							e.getRoomId(),
							e,
							ServerSentStreamType.ROOM_ACCEPT
						) {}
					);
					//return e;
				})

				.delayElements(Duration.ofMillis(100))
				//.doFinally((e)->{
				//	sinks.tryEmitComplete();
				//})
				.subscribe();
				//return sinks.asFlux();
				return response(Result._0, null);
			})
		,  Response.class);
		//, RoomInAccountDomain.RoomJoinedAccountResponse.class);
		
	}
	
	public Mono<ServerResponse> updateRoomInAccountOrder(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		//.flatMapMany(account -> 
		.flatMap(account -> {
			roomInAccountRepository.saveAll(
				request.bodyToFlux(RoomInAccountEntity.class)
				.filterWhen(roomInAccount->
					Mono.just(roomInAccount.getId() != null)
					.flatMap(bol->{
						if( ! bol) {
							return Mono.error(new RoomException(Result._300));
						}
						return roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), roomInAccount.getRoomId());
					})
				)
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(roomInAccount -> roomInAccountRepository.findById(roomInAccount.getId())
					.map(newRoomInAccount->
						newRoomInAccount
							.withOrderSort(roomInAccount.getOrderSort())
							//.withUpdateAt(LocalDateTime.now())
					)
				)
			).subscribe();
			return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, null), Response.class);
		})
		;
		/*.collectList()
		.flatMap(roomInAccountList -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response(Result._0, roomInAccountList), Response.class)
		)
		;
		*/
	}
	
	public Mono<ServerResponse> createRoomFavorites(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		.flatMap(account -> request.bodyToMono(RoomFavoritesEntity.class)
			.flatMap(roomFavorites -> 
				roomFavoritesRepository.existsByAccountIdAndRoomId(account.getId(), roomFavorites.getRoomId())
				.flatMap(bol -> {
					if(bol) {
						return roomFavoritesRepository.deleteByAccountIdAndRoomIdAndWorkspaceId(account.getId(), roomFavorites.getRoomId(), roomFavorites.getWorkspaceId());
					}else {
						return roomFavoritesRepository.maxByAccountIdAndWorkspaceId(account.getId(), roomFavorites.getWorkspaceId())
							.defaultIfEmpty((long)0)
							.flatMap(count -> roomFavoritesRepository.save(
									roomFavorites
									.withAccountId(account.getId())
									.withOrderSort(count + 1)
								)
							)
							.map(e->e.withAccountId(null));
					}
				})
				
			)
		)
		.flatMap(roomFavorites -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response(Result._0, roomFavorites), Response.class)
		)
		;
	}

	public Mono<ServerResponse> updateRoomFavoritesOrder(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		//.flatMapMany(account -> 
		.flatMap(account -> {
			roomFavoritesRepository.saveAll(
				request.bodyToFlux(RoomInAccountEntity.class)
				.filterWhen(roomInAccount->
					Mono.just(roomInAccount.getId() != null)
					.flatMap(bol->{
						if( ! bol) {
							return Mono.error(new RoomException(Result._300));
						}
						return roomFavoritesRepository.existsByAccountIdAndRoomId(account.getId(), roomInAccount.getRoomId());
					})
				)
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(roomInAccount -> roomFavoritesRepository.findById(roomInAccount.getId())
					.map(newRoomInAccount->
						newRoomInAccount
							.withOrderSort(roomInAccount.getOrderSort())
							//.withUpdateAt(LocalDateTime.now())
					)
				)
			).subscribe();
			return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, null), Response.class);
		})
		;
		/*.collectList()
		.flatMap(roomInAccountList -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response(Result._0, roomInAccountList), Response.class)
		)
		;
		*/
	}
	
	public Mono<ServerResponse> searchRoom(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.error(new WorkspaceException(Result._200));
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
			//.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.flatMap(list -> response(Result._0, list))
		, Response.class);
	}

	public Mono<ServerResponse> searchRoomMyJoinedAndRoomType(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.error(new WorkspaceException(Result._200));
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
			//.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.flatMap(list -> response(Result._0, list))
		, Response.class);
	}
	public Mono<ServerResponse> searchRoomMyJoinedNameAndRoomType(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.error(new WorkspaceException(Result._200));
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
			//.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.flatMap(list -> response(Result._0, list))
		, Response.class); 
	}
	
	public Mono<ServerResponse> searchRoomFavoritesMyJoined(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.error(new WorkspaceException(Result._200));
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
			//.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.flatMap(list -> response(Result._0, list))
		, Response.class);
	}
	
	public Mono<ServerResponse> searchRoomFavoritesMyJoinedNema(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
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
			.flatMap(list -> response(Result._0, list))
		, Response.class)
		;
	}
	
	public Mono<ServerResponse> getRoomDetail(ServerRequest request){
		Long roomId = Long.valueOf(request.pathVariable("roomId"));
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			roomRepository.findById(roomId)
			.flatMap(e-> response(Result._0, e.withCreateBy(null)))
		, Response.class)
		;
	}
	
	public Mono<ServerResponse> searchRoomInAccountAllList(ServerRequest request){
		/*
		var params = request.queryParams();
		Long roomId = Long.valueOf(params.getFirst("roomId"));
		*/
		Long roomId = Long.valueOf(request.pathVariable("roomId"));
		
		return ok()
		.contentType(MediaType.TEXT_EVENT_STREAM)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(acc -> roomInAccountRepository.existsByAccountIdAndRoomId(acc.getId(), roomId))
			.switchIfEmpty(Mono.error(new RoomException(Result._301)))
			.flatMapMany(account -> {
				Sinks.Many<RoomInAccountDomain.RoomJoinedAccountResponse> sinks = Sinks.many().unicast().onBackpressureBuffer();
				
				roomInAccountRepository.findAllByRoomId(roomId)
				//.cache(Duration.ofDays(1))
				.flatMap(roomInAccount -> 
					accountRepository.findById(roomInAccount.getAccountId())
					//.cache(Duration.ofDays(1))
					.map(targetAccount -> 
						RoomJoinedAccountResponse.builder()
						.roomId(roomInAccount.getRoomId())
						.accountName(targetAccount.getAccountName())
						.fullName(targetAccount.getFullName())
						.job_grade(targetAccount.getJobGrade())
						.department(targetAccount.getDepartment())
						.createMils(roomInAccount.getCreateMils())
						.updateMils(roomInAccount.getUpdateMils())
						.build()
					)
				)
				
				.doOnNext(e->{
					sinks.tryEmitNext(e);
				})
				.delayElements(Duration.ofMillis(20))
				.doFinally(e->{
					sinks.tryEmitComplete();
				})
				.subscribe();
				;
				return sinks.asFlux();
			})
			
		, RoomInAccountDomain.RoomJoinedAccountResponse.class)
		;
	}
	
	public Mono<ServerResponse> isRoomFavorites(ServerRequest request){
		Long roomId = Long.valueOf(request.pathVariable("roomId"));
		
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)	
			.flatMap(account -> 
				roomFavoritesRepository.existsByAccountIdAndRoomId(account.getId(), roomId)
			)
			.flatMap(e-> response(Result._0, e))
		, Response.class);
	}
}
