package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomEntity.RoomDomain.CreateRoomRequest;
import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain.RoomJoinedAccountResponse;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.repository.chatting.ChattingRepository;
import com.radcns.bird_plus.repository.room.RoomFavoritesRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.room.RoomRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import io.r2dbc.postgresql.codec.Json;
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
	private WorkspaceBroker workspaceBroker;
	@Autowired
	private ChattingRepository chattingRepository;
	public Mono<ServerResponse> createRoom(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		.flatMap(account -> 
			request.bodyToMono(CreateRoomRequest.class)
			.filterWhen(e-> workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(e.getWorkspaceId(), account.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(createRoomRequest->{
				RoomEntity roomEntity = RoomEntity.builder()
					.roomName(createRoomRequest.getRoomName())
					.workspaceId(createRoomRequest.getWorkspaceId())
					.roomType(createRoomRequest.getRoomType())
				.build();
				if(roomEntity.getRoomType().equals(RoomType.MESSENGER)) {
					return Flux.fromIterable(createRoomRequest.getInviteAccountList())
						.flatMap(e->accountRepository.findByAccountName(e))
						.map(inviteAccount -> inviteAccount.getId())
						.collectList()
						.flatMap(idList->{
							idList.add(account.getId());
							return roomInAccountRepository.findRoomIdWithExistsInviteAccount(createRoomRequest.getRoomType(), createRoomRequest.getWorkspaceId(), idList, idList.size())
								.flatMap(roomRepository::findById);
						})
						.defaultIfEmpty(roomEntity.withCreateBy(account.getId()))
						.flatMap(e->roomRepository.save(e))
						;
				}
				return roomRepository.save(roomEntity.withCreateBy(account.getId()));
			})
			.doOnSuccess(e-> {
				e.setCreateBy(null);
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
					workspaceBroker.send(
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
			.body(response(Result._0, room), ResponseWrapper.class)
		)
		;
	}

	public Mono<ServerResponse> createMySelfRoom(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(e-> workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(workspaceId, e.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(account->{
				return roomRepository.existsByCreateByAndWorkspaceIdAndRoomType(account.getId(), workspaceId, RoomType.SELF)
				.flatMap(bol -> {
					if(bol) {
						return roomRepository.findByCreateByAndWorkspaceIdAndRoomType(account.getId(), workspaceId, RoomType.SELF)
							.flatMap(roomEntity -> {
								if( ! roomEntity.getRoomName().equals( account.getFullName() )) {
									return roomRepository.save(roomEntity.withRoomName(account.getFullName()))
											.doOnSuccess(e->{
												roomInAccountRepository.findByRoomIdAndAccountId(e.getId(), account.getId())
												.doOnNext(roomInAccount->{
													workspaceBroker.send(
														new ServerSentStreamTemplate<RoomInAccountEntity>(
															roomInAccount.getWorkspaceId(),
															roomInAccount.getRoomId(),
															roomInAccount,
															ServerSentStreamType.ROOM_ACCEPT
														) {}
													);
												})
												.subscribe();
											});
								}else {
									return Mono.just(roomEntity);
								}
							});
					}else {
						return roomRepository.save(
							RoomEntity
							.builder()
							.createBy(account.getId())
							.roomName(account.getFullName())
							.workspaceId(workspaceId)
							.roomType(RoomType.SELF)
							.build()
						)
						.doOnSuccess(roomEntity->{
							roomEntity.setCreateBy(null);
							roomInAccountRepository.findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, List.of(RoomType.SELF))
							.defaultIfEmpty((long)0)
							.flatMap(count -> {
								RoomInAccountEntity roomInAccountEntity = RoomInAccountEntity.builder()
									.roomId(roomEntity.getId())
									.accountId(account.getId())
									.orderSort(count + 1)
									.workspaceId(workspaceId)
								.build();

								return roomInAccountRepository.save(roomInAccountEntity);
							})
							.doOnSuccess(result->{
								workspaceBroker.send(
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
		, ResponseWrapper.class);
	}
	
	public Mono<ServerResponse> createRoomInAccount(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		.flatMap(account -> {
			
			request.bodyToFlux(RoomInAccountDomain.CreateRoomInAccountRequest.class).flatMap(createRoomInAccount -> 
				accountRepository.findByAccountName(createRoomInAccount.getAccountName())
				.filterWhen(e -> 
					workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(createRoomInAccount.getWorkspaceId(), e.getId(), true)
				)
				.filterWhen(e->
					roomInAccountRepository.existsByAccountIdAndRoomId(e.getId(), createRoomInAccount.getRoomId()).map(bol->!bol)
				)
				.flatMap(inviteAccount -> {
					RoomInAccountEntity roomInAccountEntity = RoomInAccountEntity.builder()
					.accountId(inviteAccount.getId())
					.workspaceId(createRoomInAccount.getWorkspaceId())
					.roomId(createRoomInAccount.getRoomId())
					.createBy(account.getId())
					.build();
					List<RoomType> roomType;
					if(createRoomInAccount.getRoomType().equals(RoomType.ROOM_PRIVATE) || createRoomInAccount.getRoomType().equals(RoomType.ROOM_PRIVATE)) {
						roomType = List.of(RoomType.ROOM_PRIVATE, RoomType.ROOM_PUBLIC);
					}else{//(e.getRoomType().equals(RoomType.MESSENGER) || e.getRoomType().equals(RoomType.SELF)) {
						roomType = List.of(RoomType.MESSENGER, RoomType.SELF);
					}
					
					return roomInAccountRepository.findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType(roomInAccountEntity.getAccountId(), roomInAccountEntity.getWorkspaceId(), roomType)
					.defaultIfEmpty((long)0)
					.map(count -> roomInAccountEntity.withOrderSort(count + 1))
					.flatMap(e->roomInAccountRepository.save(e))
					.doOnSuccess(e->{

						workspaceBroker.send(
							new ServerSentStreamTemplate<RoomJoinedAccountResponse>(
								e.getWorkspaceId(),
								e.getRoomId(),
								RoomJoinedAccountResponse.builder()
									.roomId(createRoomInAccount.getRoomId())
									.workspaceId(createRoomInAccount.getWorkspaceId())
									.accountName(inviteAccount.getAccountName())
									.fullName(inviteAccount.getFullName())
									.job_grade(inviteAccount.getJobGrade())
									.department(inviteAccount.getDepartment())
									.roomType(createRoomInAccount.getRoomType())
									.createMils(e.getCreateMils())
									.updateMils(e.getUpdateMils())
								.build(),
								ServerSentStreamType.ROOM_IN_ACCOUNT_ACCEPT
							) {}
						);

						workspaceBroker.send(
							new ServerSentStreamTemplate<RoomInAccountEntity>(
								e.getWorkspaceId(),
								e.getRoomId(),
								e,
								ServerSentStreamType.ROOM_ACCEPT
							) {}
						);
						
						ChattingEntity chattingEntity = ChattingEntity.builder()
						.accountId(account.getId())
						.roomId(createRoomInAccount.getRoomId())
						.workspaceId(createRoomInAccount.getWorkspaceId())
						.createBy(account.getId())
						.build();
						chattingEntity.setChatting(ChattingEntity.INVITE_ROOM_IN_ACCOUNT_NOTIFICATION.formatted(account.getFullName(), inviteAccount.getFullName()));

						chattingRepository.save(chattingEntity).doOnSuccess(ss->{
							workspaceBroker.send(
								new ServerSentStreamTemplate<ChattingResponse>(
									createRoomInAccount.getWorkspaceId(),
									createRoomInAccount.getRoomId(),
									ChattingResponse.builder()
										.id(ss.getId())
										.roomId(createRoomInAccount.getRoomId())
										.workspaceId(createRoomInAccount.getWorkspaceId())
										.chatting(Json.of(chattingEntity.getChatting()))
										.createAt(LocalDateTime.now())
										.updateAt(LocalDateTime.now())
										.fullName(account.getFullName())
										.accountName(account.getAccountName())
									.build(),
									ServerSentStreamType.CHATTING_ACCEPT
								) {}
							);
						})
						.subscribe();
					})
					.doFinally(f->{
						
					})
					.delayElement(Duration.ofMillis(1000))
					;
				})
			)
			.subscribe();
			
			return ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(response(Result._0, null), ResponseWrapper.class);
		})
		;

		/*
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
							workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsEnabled(createRoomInAccount.getWorkspaceId(), e.getId(), true)
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
								.map(count -> roomInAccountEntity.withOrderSort(count + 1)).doOnSuccess((s)->{
									ChattingEntity chattingEntity = ChattingEntity.builder()
									.id(account.getId())
									.roomId(createRoomInAccount.getRoomId())
									.workspaceId(createRoomInAccount.getWorkspaceId())
									.createBy(account.getId())
									.build();
									chattingEntity.setChatting(ChattingEntity.INVITE_ROOM_IN_ACCOUNT_NOTIFICATION.formatted(account.getFullName(),e.getFullName()));

									chattingRepository.save(chattingEntity).doOnSubscribe(ss->{
										workspaceBroker.send(
											new ServerSentStreamTemplate<ChattingResponse>(
												createRoomInAccount.getWorkspaceId(),
												createRoomInAccount.getRoomId(),
												ChattingResponse.builder()
													.id(account.getId())
													.roomId(createRoomInAccount.getRoomId())
													.workspaceId(createRoomInAccount.getWorkspaceId())
													.chatting(Json.of(chattingEntity.getChatting()))
													.createAt(LocalDateTime.now())
													.updateAt(LocalDateTime.now())
													.fullName(account.getFullName())
													.accountName(account.getAccountName())
												.build(),
												ServerSentStreamType.CHATTING_ACCEPT
											) {}
										);
									})
									.delayElement(Duration.ofMinutes(1))
									.subscribe();
								});
						})
						;
					})
				);
				save.doOnNext((e)-> {
					e.getRoomJoinedAccountResponse().setCreateMils(e.getCreateMils());
					e.getRoomJoinedAccountResponse().setUpdateMils(e.getUpdateMils());
					workspaceBroker.send(
						new ServerSentStreamTemplate<RoomJoinedAccountResponse>(
							e.getWorkspaceId(),
							e.getRoomId(),
							e.getRoomJoinedAccountResponse(),
							ServerSentStreamType.ROOM_IN_ACCOUNT_ACCEPT
						) {}
					);

					
					e.setRoomJoinedAccountResponse(null);
					workspaceBroker.send(
						new ServerSentStreamTemplate<RoomInAccountEntity>(
							e.getWorkspaceId(),
							e.getRoomId(),
							e,
							ServerSentStreamType.ROOM_ACCEPT
						) {}
					);

				})
				.delayElements(Duration.ofMillis(100))
				.subscribe();
				return response(Result._0, null);
			})
		,  ResponseWrapper.class);
		//, RoomInAccountDomain.RoomJoinedAccountResponse.class);
		*/
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
					.map(newRoomInAccount-> newRoomInAccount
						.withOrderSort(roomInAccount.getOrderSort())
						.withUpdateAt(LocalDateTime.now())
					)
				)
			).subscribe();
			return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, null), ResponseWrapper.class);
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
			.body(response(Result._0, roomFavorites), ResponseWrapper.class)
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
					.map(newRoomInAccount-> newRoomInAccount
							.withOrderSort(roomInAccount.getOrderSort())
							.withUpdateAt(LocalDateTime.now())
					)
				)
			).subscribe();
			return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, null), ResponseWrapper.class);
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
	
	public Mono<ServerResponse> searchRoomList(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(account -> workspaceInAccountRepository.existsByAccountIdAndIsEnabled(account.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(account -> {
				var param = request.queryParams();
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
		, ResponseWrapper.class);
	}

	public Mono<ServerResponse> searchMyJoinedRoomList(ServerRequest request){
		
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));

		var response = accountService.convertRequestToAccount(request)
			.filterWhen(account -> workspaceInAccountRepository.existsByAccountIdAndIsEnabled(account.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(account -> {
				var param = request.queryParams();
				List<RoomType> roomType;
				try {
					roomType = param.get("roomType").stream().map(RoomType::valueOf).toList();
				}catch(IllegalArgumentException e) {
					return Mono.error(new RoomException(Result._300));
				}
				String roomName = param.getOrDefault("roomName", List.of("")).get(0);
				PageRequest pageRequest = PageRequest.of(
						Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
						Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
					);
				
				if(roomName.isBlank()) {
					return roomInAccountRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, roomType, pageRequest)
						/*.flatMap(e-> {
							if( ! e.getRoomType().equals(RoomType.MESSENGER)) {
								return Mono.just(e);
							}
							return roomInAccountRepository.findGroupMessengerRoomName(e.getRoomId(), account.getId()).map(messengerRoomName->e.withRoomName(messengerRoomName));
						})*/
						.collectList()
						.zipWith(roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomType(account.getId(), workspaceId, roomType))
						.map(entityTuples -> 
							new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
						)
						;
				}
				
				return roomInAccountRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(account.getId(), workspaceId, roomName, roomType, pageRequest)
					/*.flatMap(e-> {
						if( ! e.getRoomType().equals(RoomType.MESSENGER)) {
							return Mono.just(e);
						}
						return roomInAccountRepository.findGroupMessengerRoomName(e.getRoomId(), account.getId()).map(messengerRoomName->e.withRoomName(messengerRoomName));
					})*/
					.collectList()
					.zipWith(roomInAccountRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(account.getId(), workspaceId, roomName, roomType))
					.map(entityTuples -> 
						new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
					)
					;
			})
			//.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.flatMap(list -> response(Result._0, list))
		;
		
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(response, ResponseWrapper.class)
		; 
	}
	
	public Mono<ServerResponse> searchRoomFavoritesList(ServerRequest request){
		
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		var response = accountService.convertRequestToAccount(request)
			.filterWhen(account -> workspaceInAccountRepository.existsByAccountIdAndIsEnabled(account.getId(), true))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(account -> {
				var param = request.queryParams();
				String roomName = param.getOrDefault("roomName", List.of("")).get(0);
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				if(roomName.isBlank()) {
					return roomFavoritesRepository.findAllJoinRoomByAccountIdAndWorkspaceId(account.getId(), workspaceId, pageRequest)
						/*.flatMap(e-> {
							if( ! e.getRoomType().equals(RoomType.MESSENGER)) {
								return Mono.just(e);
							}
							return roomInAccountRepository.findGroupMessengerRoomName(e.getRoomId(), account.getId()).map(messengerRoomName->e.withRoomName(messengerRoomName));
						})*/
						.collectList()
						.zipWith(roomFavoritesRepository.countJoinRoomByAccountIdAndWorkspaceId(account.getId(), workspaceId))
						.map(entityTuples -> 
							new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
						)
						;
				}
				
				return roomFavoritesRepository.findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomName(account.getId(), workspaceId, roomName, pageRequest)
					/*.flatMap(e-> {
						if( ! e.getRoomType().equals(RoomType.MESSENGER)) {
							return Mono.just(e);
						}
						return roomInAccountRepository.findGroupMessengerRoomName(e.getRoomId(), account.getId()).map(messengerRoomName->e.withRoomName(messengerRoomName));
					})*/
					.collectList()
					.zipWith(roomFavoritesRepository.countJoinRoomByAccountIdAndWorkspaceIdAndRoomName(account.getId(), workspaceId, roomName))
					.map(entityTuples -> 
						new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
					)
					;
			})
			.flatMap(list -> response(Result._0, list))
		;
		
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(response, ResponseWrapper.class)
		;
	}
	
	public Mono<ServerResponse> getRoomDetail(ServerRequest request){
		Long roomId = Long.valueOf(request.pathVariable("roomId"));
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			roomRepository.findById(roomId)
			.flatMap(e-> response(Result._0, e.withCreateBy(null)))
		, ResponseWrapper.class)
		;
		/*return accountService.convertRequestToAccount(request).flatMap(account ->
			ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(
				roomRepository.findById(roomId)
				.flatMap(e->{
					if(e.getRoomType().equals(RoomType.MESSENGER)) {
						return roomInAccountRepository.findGroupMessengerRoomName(roomId, account.getId())
								.map(roomName -> e.withRoomName(roomName));
					}else {
						return Mono.just(e);
					}
				})
				.flatMap(e-> response(Result._0, e.withCreateBy(null)))
			, ResponseWrapper.class)
		)
		;*/
	}
	
	public Mono<ServerResponse> searchRoomJoinedAccountList(ServerRequest request){
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
						.workspaceId(roomInAccount.getWorkspaceId())
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
				//.delayElements(Duration.ofMillis(20))
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
		, ResponseWrapper.class);
	}

}
