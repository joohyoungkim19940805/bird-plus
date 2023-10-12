package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardGroupEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.repository.notice_board.NoticeBoardGroupRepository;
import com.radcns.bird_plus.repository.notice_board.NoticeBoardRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import reactor.core.publisher.Mono;

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

	public Mono<ServerResponse> createNoticeBoardGroup(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> 
				request.bodyToMono(NoticeBoardGroupEntity.class)
				.filterWhen(noticeBoardGroup -> roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoardGroup.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoardGroup -> {
					if(noticeBoardGroup.getId() == null) {
						noticeBoardGroup.setAccountId(account.getId());
					}
					workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardGroupEntity>(
								noticeBoardGroup.getWorkspaceId(),
								noticeBoardGroup.getRoomId(),
								noticeBoardGroup,
								ServerSentStreamType.NOTICE_BOARD_ACCEPT
							) {}
						);
					return noticeBoardGroupRepository.save(noticeBoardGroup).doOnSuccess(result->{

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
			accountService.convertJwtToAccount(request)
			.flatMap(account -> 
				request.bodyToMono(NoticeBoardEntity.class)
				.filterWhen(noticeBoardGroup -> roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoardGroup.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoardGroup -> {
					if(noticeBoardGroup.getId() == null) {
						noticeBoardGroup.setAccountId(account.getId());
					}
					workspaceBorker.send(
						new ServerSentStreamTemplate<NoticeBoardEntity>(
							noticeBoardGroup.getWorkspaceId(),
							noticeBoardGroup.getRoomId(),
							noticeBoardGroup,
							ServerSentStreamType.NOTICE_BOARD_ACCEPT
						) {}
					);
					return noticeBoardRepository.save(noticeBoardGroup).doOnSuccess(result->{
						
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
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.filterWhen(e-> workspaceInAccountRepository.existsByWorkspaceIdAndAccountId(workspaceId, e.getId()))
			.filterWhen(e->  roomInAccountRepository.existsByAccountIdAndRoomId(roomId, e.getId()))
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._201)))
			.flatMap(account -> {
				Long parentGroupId = Long.valueOf(param.getFirst("parentGroupId"));
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return noticeBoardRepository.findAllByWorkspaceIdAndRoomIdAndParentGroupId(workspaceId, roomId, parentGroupId, pageRequest)
				.collectList()
				.zipWith(noticeBoardRepository.countByWorkspaceIdAndRoomIdAndParentGroupId(workspaceId, roomId, parentGroupId))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			//.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class)
		;
	}
	
}
