package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardGroupEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardInheritsTable;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.repository.notice_board.NoticeBoardGroupRepository;
import com.radcns.bird_plus.repository.notice_board.NoticeBoardRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.exception.NoticeBoardException;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
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
	private WorkspaceInAccountRepository workspaceInAccountRepository;

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
					Mono<Long> maxOrderSortMono = noticeBoardRepository.findMaxByWorkspaceIdAndRoomIdAndParentGroupId(
						noticeBoardGroup.getWorkspaceId(), noticeBoardGroup.getRoomId(), noticeBoardGroup.getParentGroupId()
					).defaultIfEmpty((long)0);
					if(noticeBoardGroup.getGroupId() == null) {
						noticeBoardGroup.setAccountId(account.getId());
						noticeBoardGroup.setFullName(account.getFullName());
						save = maxOrderSortMono.flatMap(maxOrderSort -> noticeBoardGroupRepository.save(noticeBoardGroup.withOrderSort(maxOrderSort + 1)));
					}else {
						save = maxOrderSortMono.flatMap(maxOrderSort -> 
							noticeBoardGroupRepository.findById(noticeBoardGroup.getGroupId()).flatMap((e) -> {
								e.setTitle(noticeBoardGroup.getTitle());
								e.setOrderSort(maxOrderSort + 1);
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
			.map(e-> response(Result._0, e))
		, Response.class)
		;
	}
	
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
					Mono<Long> maxOrderSortMono = noticeBoardRepository.findMaxByWorkspaceIdAndRoomIdAndParentGroupId(
							noticeBoard.getWorkspaceId(), noticeBoard.getRoomId(), noticeBoard.getParentGroupId()
						).defaultIfEmpty((long)0);
					if(noticeBoard.getId() == null) {
						noticeBoard.setAccountId(account.getId());
						noticeBoard.setFullName(account.getFullName());
						save = maxOrderSortMono.flatMap(maxOrderSort -> noticeBoardRepository.save(noticeBoard.withOrderSort(maxOrderSort + 1)));
					}else {
						save = maxOrderSortMono.flatMap(maxOrderSort -> 
							noticeBoardRepository.findById(noticeBoard.getId()).flatMap((e) -> {
								e.setTitle(noticeBoard.getTitle());
								e.setOrderSort(maxOrderSort + 1);
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
			.map(e-> response(Result._0, e))
		, Response.class)
		;
	}
	
	public Mono<ServerResponse> deleteNoticeBoardGroup(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> 
			request.bodyToMono(NoticeBoardGroupEntity.class)
			.filterWhen(noticeBoardGroup -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), noticeBoardGroup.getWorkspaceId(), noticeBoardGroup.getRoomId()))
			.switchIfEmpty(Mono.error(new RoomException(Result._301)))
			.flatMap(noticeBoardGroup -> {
				if(noticeBoardGroup.getGroupId() == null) {
					return Mono.error(new NoticeBoardException(Result._400));
				}
				return noticeBoardGroupRepository.deleteById(noticeBoardGroup.getGroupId()).doOnSuccess(result->{
					noticeBoardGroup.setAccountId(null);
					workspaceBorker.send(
						new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
							noticeBoardGroup.getWorkspaceId(),
							noticeBoardGroup.getRoomId(),
							noticeBoardGroup,
							ServerSentStreamType.NOTICE_BOARD_DELETE_ACCEPT
						) {}
					);
				});
			})
		)
		.map(e-> response(Result._0, e))
		, Response.class)
		;
	}
	
	public Mono<ServerResponse> deleteNoticeBoard(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> 
			request.bodyToMono(NoticeBoardEntity.class)
			.filterWhen(noticeBoard -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), noticeBoard.getWorkspaceId(), noticeBoard.getRoomId()))
			.switchIfEmpty(Mono.error(new RoomException(Result._301)))
			.flatMap(noticeBoard -> {
				if(noticeBoard.getId() == null) {
					return Mono.error(new NoticeBoardException(Result._400));
				}
				return noticeBoardRepository.deleteById(noticeBoard.getId()).doOnSuccess(result->{
					noticeBoard.setAccountId(null);
					workspaceBorker.send(
						new ServerSentStreamTemplate<NoticeBoardInheritsTable>(
							noticeBoard.getWorkspaceId(),
							noticeBoard.getRoomId(),
							noticeBoard,
							ServerSentStreamType.NOTICE_BOARD_DELETE_ACCEPT
						) {}
					);
				});
			})
		)
		.map(e-> response(Result._0, e))
		, Response.class)
		;
	}
	
	public Mono<ServerResponse> searchNoticeBoardAndGroup(ServerRequest request){
		var param = request.queryParams();
		Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
		Long roomId = Long.valueOf(param.getFirst("roomId"));
		if(workspaceId == null) {
			throw new WorkspaceException(Result._200);
		}else if(roomId == null) {
			throw new RoomException(Result._300);
		}

		return ok()
		.contentType(MediaType.TEXT_EVENT_STREAM)
		.body(
			accountService.convertRequestToAccount(request)
			.filterWhen(e->  roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(e.getId(), workspaceId, roomId))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._301)))
			.flatMapMany(account -> {
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
				.delayElements(Duration.ofMillis(50))
				.doFinally(e->{
					sinks.tryEmitComplete();
				})
				.subscribe();
				
				return sinks.asFlux();
			})
		, NoticeBoardEntity.class)
		;
	}

}
