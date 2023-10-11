package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.repository.notice_board.NoticeBoardGroupRepository;
import com.radcns.bird_plus.repository.notice_board.NoticeBoardRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.exception.RoomException;
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
	
	public Mono<ServerResponse> createNoticeBoardGroup(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> 
				request.bodyToMono(NoticeBoardEntity.class)
				.filterWhen(noticeBoard -> roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), noticeBoard.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(noticeBoard -> {
					if(noticeBoard.getId() != null) {
						noticeBoard.setAccountId(account.getId());
					}
					return noticeBoardGroupRepository.save(noticeBoard).doOnSuccess(result->{
						workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardEntity>(
								noticeBoard.getWorkspaceId(),
								noticeBoard.getRoomId(),
								noticeBoard,
								ServerSentStreamType.NOTICE_BOARD_ACCEPT
							) {}
						);
					});
				})
			)
			.map(e-> response(Result._0, null))
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
					if(noticeBoardGroup.getId() != null) {
						noticeBoardGroup.setAccountId(account.getId());
					}
					return noticeBoardRepository.save(noticeBoardGroup).doOnSuccess(result->{
						workspaceBorker.send(
							new ServerSentStreamTemplate<NoticeBoardEntity>(
								noticeBoardGroup.getWorkspaceId(),
								noticeBoardGroup.getRoomId(),
								noticeBoardGroup,
								ServerSentStreamType.NOTICE_BOARD_ACCEPT
							) {}
						);
					});
				})
			)
			.map(e-> response(Result._0, null))
		, Response.class)
		;
	}
	
	public Mono<ServerResponse> searchNoticeBoardAndGroup(ServerRequest request){
		ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
		null
		, Object.class);
		return null;
	}
}
