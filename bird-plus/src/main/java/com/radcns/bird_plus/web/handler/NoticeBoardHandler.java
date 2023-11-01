package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.entity.notice.NoticeBoardDetailEntity;
import com.radcns.bird_plus.entity.notice.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice.NoticeBoardGroupEntity;
import com.radcns.bird_plus.entity.notice.NoticeBoardInheritsTable;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.repository.notice.NoticeBoardDetailRepository;
import com.radcns.bird_plus.repository.notice.NoticeBoardGroupRepository;
import com.radcns.bird_plus.repository.notice.NoticeBoardRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.AccountException;
import com.radcns.bird_plus.util.exception.NoticeBoardException;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.sun.mail.iap.Response;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class NoticeBoardHandler {
	@Autowired
	private NoticeBoardRepository noticeBoardRepository;
	@Autowired
	private NoticeBoardGroupRepository noticeBoardGroupRepository;
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	@Autowired
	private AccountService accountService;
	@Autowired
	private WorkspaceBroker workspaceBorker;
	@Autowired
	private NoticeBoardDetailRepository noticeBoardDetailRepository;

	//roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(e.getId(), workspaceId, roomId)
	public Mono<ServerResponse> createNoticeBoardGroup(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> 
				request.bodyToMono(NoticeBoardGroupEntity.class)
				.filterWhen(noticeBoardGroup -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), noticeBoardGroup.getWorkspaceId(), noticeBoardGroup.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoardGroup -> {
					Mono<NoticeBoardGroupEntity> save;
					Mono<Long> maxOrderSortMono;
					if(noticeBoardGroup.getParentGroupId() == null) {
						maxOrderSortMono = noticeBoardRepository.findMaxByWorkspaceIdAndRoomIdAndParentGroupId(
							noticeBoardGroup.getWorkspaceId(), noticeBoardGroup.getRoomId()
						).defaultIfEmpty((long)0);
					}else {
						maxOrderSortMono = noticeBoardRepository.findMaxByWorkspaceIdAndRoomIdAndParentGroupId(
							noticeBoardGroup.getWorkspaceId(), noticeBoardGroup.getRoomId(), noticeBoardGroup.getParentGroupId()
						).defaultIfEmpty((long)0);
					}
					
					if(noticeBoardGroup.getGroupId() == null) {
						noticeBoardGroup.setAccountId(account.getId());
						noticeBoardGroup.setFullName(account.getFullName());
						save = maxOrderSortMono
								.flatMap(maxOrderSort -> noticeBoardGroupRepository.save(noticeBoardGroup.withOrderSort(maxOrderSort + 1)));
					}else {
						save = maxOrderSortMono.flatMap(maxOrderSort -> 
							noticeBoardGroupRepository.findById(noticeBoardGroup.getGroupId()).flatMap((e) -> {
								e.setTitle(noticeBoardGroup.getTitle());
								//e.setOrderSort(maxOrderSort + 1);
								return noticeBoardGroupRepository.save(e);
							})
						);
					}
					return save.doOnSuccess(result->{
						result.setAccountId(null);
						workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
								result.getWorkspaceId(),
								result.getRoomId(),
								result,
								ServerSentStreamType.NOTICE_BOARD_ACCEPT
							) {}
						);
					});
				})
			)
			.flatMap(e-> response(Result._0, e))
		, ResponseWrapper.class)
		;
	}
	//
	public Mono<ServerResponse> createNoticeBoard(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> 
				request.bodyToMono(NoticeBoardEntity.class)
				.filterWhen(noticeBoard-> roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoard.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoard -> {
					Mono<NoticeBoardEntity> save;
					Mono<Long> maxOrderSortMono;
					if(noticeBoard.getParentGroupId() == null) {
						maxOrderSortMono = noticeBoardRepository.findMaxByWorkspaceIdAndRoomIdAndParentGroupId(
								noticeBoard.getWorkspaceId(), noticeBoard.getRoomId()
						).defaultIfEmpty((long)0);
					}else {
						maxOrderSortMono = noticeBoardRepository.findMaxByWorkspaceIdAndRoomIdAndParentGroupId(
								noticeBoard.getWorkspaceId(), noticeBoard.getRoomId(), noticeBoard.getParentGroupId()
						).defaultIfEmpty((long)0);
					}
					if(noticeBoard.getId() == null) {
						noticeBoard.setAccountId(account.getId());
						noticeBoard.setFullName(account.getFullName());
						save = maxOrderSortMono.flatMap(maxOrderSort -> noticeBoardRepository.save(noticeBoard.withOrderSort(maxOrderSort + 1)));
					}else {
						save = maxOrderSortMono.flatMap(maxOrderSort -> 
							noticeBoardRepository.findById(noticeBoard.getId()).flatMap((e) -> {
								e.setTitle(noticeBoard.getTitle());
								//e.setOrderSort(maxOrderSort + 1);
								return noticeBoardRepository.save(e);
							})
						);
					}

					return save.doOnSuccess(result->{
						result.setAccountId(null);
						workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
								result.getWorkspaceId(),
								result.getRoomId(),
								result,
								ServerSentStreamType.NOTICE_BOARD_ACCEPT
							) {}
						);
					});
				})
			)
			.flatMap(e-> response(Result._0, e))
		, ResponseWrapper.class)
		;
	}
	
	public Mono<ServerResponse> deleteNoticeBoardGroup(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request).flatMap(account -> request.bodyToMono(NoticeBoardGroupEntity.class)
				.filterWhen(noticeBoardGroup -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), noticeBoardGroup.getWorkspaceId(), noticeBoardGroup.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoardGroup -> {
					if(noticeBoardGroup.getGroupId() == null) {
						return Mono.error(new NoticeBoardException(Result._400));
					}
					noticeBoardGroupRepository.deleteById(noticeBoardGroup.getGroupId()).doOnSuccess(result->{
						noticeBoardGroup.setAccountId(null);
						workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
								noticeBoardGroup.getWorkspaceId(),
								noticeBoardGroup.getRoomId(),
								noticeBoardGroup,
								ServerSentStreamType.NOTICE_BOARD_DELETE_ACCEPT
							) {}
						);
					}).subscribe();
					return response(Result._0);
				})
			)
		, ResponseWrapper.class)
		;
	}
	
	public Mono<ServerResponse> deleteNoticeBoard(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request).flatMap(account -> request.bodyToMono(NoticeBoardEntity.class)
				.filterWhen(noticeBoard -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), noticeBoard.getWorkspaceId(), noticeBoard.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoard -> {
					if(noticeBoard.getId() == null) {
						return Mono.error(new NoticeBoardException(Result._400));
					}
					noticeBoardRepository.deleteById(noticeBoard.getId()).doOnSuccess(result->{
						noticeBoard.setAccountId(null);
						workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
								noticeBoard.getWorkspaceId(),
								noticeBoard.getRoomId(),
								noticeBoard,
								ServerSentStreamType.NOTICE_BOARD_DELETE_ACCEPT
							) {}
						);
					}).subscribe();
					return response(Result._0);
				})
			)
		, ResponseWrapper.class)
		;
	}
	
	public Mono<ServerResponse> searchNoticeBoardList(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		Long roomId = Long.valueOf(request.pathVariable("roomId"));

		return ok()
		.contentType(MediaType.TEXT_EVENT_STREAM)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(e->  roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(e.getId(), workspaceId, roomId))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._301)))
			.flatMapMany(account -> {
				var param = request.queryParams();
				String parentGroupIdObject = param.getFirst("parentGroupId");
				Long parentGroupId = null;
				if(parentGroupIdObject != null) {
					parentGroupId = Long.valueOf(parentGroupIdObject);
				}
				Sinks.Many<NoticeBoardEntity> sinks = Sinks.many().unicast().onBackpressureBuffer();
				
				noticeBoardRepository.findAllByWorkspaceIdAndRoomIdAndParentGroupId(workspaceId, roomId, parentGroupId)
				.doOnNext(e-> {
					sinks.tryEmitNext(e);
				})
				//.delayElements(Duration.ofMillis(50))
				.doFinally(e->{
					sinks.tryEmitComplete();
				})
				.subscribe();
				
				return sinks.asFlux();
			})
		, NoticeBoardEntity.class)
		;
	}

	public Mono<ServerResponse> updateNoticeBoardOrder(ServerRequest request){
		return accountService.convertRequestToAccount(request)
		//.flatMapMany(account -> 
		.flatMap(account -> {
			noticeBoardRepository.saveAll(
				request.bodyToFlux(NoticeBoardEntity.class)
				.filterWhen(noticeBoardEntity->
					Mono.just(noticeBoardEntity.getId() != null)
					.flatMap(bol->{
						if( ! bol) {
							return Mono.error(new RoomException(Result._300));
						}
						return roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoardEntity.getRoomId());
					})
				)
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoardEntity -> {
					return noticeBoardRepository.findById(noticeBoardEntity.getId()).map(e-> e
							.withOrderSort(noticeBoardEntity.getOrderSort())
							.withParentGroupId(noticeBoardEntity.getParentGroupId())
							.withUpdateAt(LocalDateTime.now())
					);
				})
			)
			.doOnNext(e->{
				workspaceBorker.send(
					new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
						e.getWorkspaceId(),
						e.getRoomId(),
						e.withAccountId(null),
						ServerSentStreamType.NOTICE_BOARD_ACCEPT
					) {}
				);
			})
			.subscribe();
			
			return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0), ResponseWrapper.class);
		})
		;
	}
	public Mono<ServerResponse> createNoticeBoardDetail(ServerRequest request){
		
		var response = accountService.convertRequestToAccount(request).flatMap(account -> request.bodyToMono(NoticeBoardDetailEntity.class)
			.filterWhen(noticeBoardDetail -> roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoardDetail.getRoomId()))
			.switchIfEmpty(Mono.error(new RoomException(Result._301)))
			.flatMap(noticeBoardDetail -> {
				/*Mono<Long> maxOrderSortMono = noticeBoardDetailRepository
					.findMaxByWorkspaceIdAndRoomIdAndNoticeBoardId(
						noticeBoardDetail.getWorkspaceId(), noticeBoardDetail.getRoomId(), noticeBoardDetail.getNoticeBoardId()
					).defaultIfEmpty((long)0);*/
				Mono<NoticeBoardDetailEntity> save;
				if(noticeBoardDetail.getId() == null) {
					save = noticeBoardDetailRepository.save(
						noticeBoardDetail
							.withCreateBy(account.getId())
							.withUpdateBy(account.getId())
					);

				}else {
					save = noticeBoardDetailRepository.findById(noticeBoardDetail.getId())
					.flatMap(entity -> 
						noticeBoardDetailRepository.save(
							entity
								.withUpdateBy(account.getId())
								.withUpdateAt(LocalDateTime.now())
								.withEmptyLineCount(noticeBoardDetail.getEmptyLineCount())
								.withContent(noticeBoardDetail.getContent())
						)
					);
				}
				return save.doOnSuccess(result -> {
					workspaceBorker.send(
						new ServerSentStreamTemplate<NoticeBoardDetailEntity>(
							result.getWorkspaceId(),
							result.getRoomId(),
							result.withCreateBy(null).withUpdateBy(null),
							ServerSentStreamType.NOTICE_BOARD_DETAIL_ACCEPT
						) {}
					);
				})
				.flatMap(e->response(Result._0))
				;
			})
		);
			
		return ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(response, ResponseWrapper.class);
	}
	
	public Mono<ServerResponse> updateNoticeBoardDetailOrder(ServerRequest request){
		
		accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				noticeBoardDetailRepository.saveAll(
					request.bodyToFlux(NoticeBoardDetailEntity.class)
					.filterWhen(noticeBoardDetailEntity -> 
						Mono.just(noticeBoardDetailEntity.getId() != null)
						.flatMap(bol -> {
							if( ! bol) {
								return Mono.error(new RoomException(Result._300));
							}
							return roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoardDetailEntity.getRoomId());
						})
					)
					.switchIfEmpty(Mono.error(new RoomException(Result._301)))
					.flatMap(noticeBoardDetail-> {
						return noticeBoardDetailRepository.findById(noticeBoardDetail.getId()).map(e-> e
								.withOrderSort(noticeBoardDetail.getOrderSort())
								.withUpdateAt(LocalDateTime.now())
						);
					})
				)
				.doOnNext(e->{
					workspaceBorker.send(
						new ServerSentStreamTemplate<NoticeBoardDetailEntity>(
							e.getWorkspaceId(),
							e.getRoomId(),
							e.withCreateBy(null).withUpdateBy(null),
							ServerSentStreamType.NOTICE_BOARD_DETAIL_ACCEPT
						) {}
					);
				});
				
				return null;
			});
		
		return ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Result._0, ResponseWrapper.class);
	}
	
	public Mono<ServerResponse> searchNoticeBoardDetailList(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		Long roomId = Long.valueOf(request.pathVariable("roomId"));
		Long noticeBoardId = Long.valueOf(request.pathVariable("noticeBoardId"));

		return ok()
			.contentType(MediaType.TEXT_EVENT_STREAM)
			.body(
				accountService.convertRequestToAccount(request)
				.filterWhen(account -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), workspaceId, roomId))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMapMany(account -> {
					Sinks.Many<NoticeBoardDetailEntity> sinks = Sinks.many().unicast().onBackpressureBuffer();
					noticeBoardDetailRepository.findAllByNoticeBoardId(noticeBoardId)
					.doOnNext(e-> {
						sinks.tryEmitNext(e);
					})
					//.delayElements(Duration.ofMillis(50))
					.doFinally(e->{
						sinks.tryEmitComplete();
					})
					.subscribe();
					
					return sinks.asFlux();
				})
			, NoticeBoardDetailEntity.class)
			;
	}
	
}
