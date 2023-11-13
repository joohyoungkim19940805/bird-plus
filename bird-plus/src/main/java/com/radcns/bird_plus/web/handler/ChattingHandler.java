package com.radcns.bird_plus.web.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.handler.FilteringWebHandler;

import com.radcns.bird_plus.config.security.JwtVerifyHandler;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.repository.chatting.ChattingRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;
import com.radcns.bird_plus.util.exception.RoomException;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;

import io.jsonwebtoken.Claims;
import io.r2dbc.postgresql.codec.Json;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


@Component
public class ChattingHandler {
	
	@Autowired
	private ChattingRepository chattingRepository;
	
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WorkspaceBroker workspaceBorker;
	
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	
	/**
	unicast() : 하나의 Subscriber 만 허용한다. 즉, 하나의 Client 만 연결할 수 있다.
	multicast() : 여러 Subscriber 를 허용한다.
	replay() : 여러 Subscriber 를 허용하되, 이전에 발행된 이벤트들을 기억해 추가로 연결되는 Subscriber 에게 전달한다.
	multicast().onBackpressureBuffer() : Subscriber 가 없을 때 발행된 이벤트들에 대해서 그 다음 구독하는 Subscriber 에게 전달한다.
	multicast().directAllOrNothing() : Subscriber 는 자신이 구독한 시점에서부터의 이벤트만 받는다.
	many().multicast(): a sink that will transmit only newly pushed data to its subscribers, honoring their backpressure
	many().unicast(): same as above, with the twist that data pushed before the first subscriber registers is buffered.
	many().replay(): a sink that will replay a specified history size of pushed data to new subscribers then continue pushing new data live.
	one(): a sink that will play a single element to its subscribers
	empty(): a sink that will play a terminal signal only to its subscribers (error or complete)
	 */
	private Sinks.Many<ChattingEntity> chattingSink = Sinks.many().multicast().directAllOrNothing();

	public Mono<ServerResponse> sendStream(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request)
			.flatMap(account -> {
				return request.bodyToMono(ChattingEntity.class)
				.filterWhen(chattingEntity -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), chattingEntity.getWorkspaceId(), chattingEntity.getRoomId()))
				.switchIfEmpty(Mono.error(new RoomException(Result._301)))
				.flatMap(chattingEntity -> {
					Mono<ChattingEntity> save;
					if(chattingEntity.getId() != null) {
						save = chattingRepository.findById(chattingEntity.getId()).flatMap(e->{
							e.setUpdateBy(account.getId());
							e.setUpdateAt(LocalDateTime.now());
							e.setChatting(chattingEntity.getChatting());
							return chattingRepository.save(e);
						});
					}else{
						save = chattingRepository.save(
							chattingEntity
							.withAccountId(account.getId())
							.withCreateBy(account.getId())
							.withUpdateBy(account.getId())
							.withAccountName(account.getAccountName())
						);
					}
					
					return save;
				})
				.doOnSuccess(e->{/*
					EmitResult result = workspaceBorker.sendChatting(
						ChattingResponse.builder()
							.id(e.getId())
							.roomId(e.getRoomId())
							.workspaceId(e.getWorkspaceId())
							.chatting(Json.of(e.getChatting()))
							.createAt(LocalDateTime.now())
							.updateAt(LocalDateTime.now())
							.fullName(account.getFullName())
							.accountName(account.getAccountName())
						.build()
					);*/
					EmitResult result = workspaceBorker.send(
						new ServerSentStreamTemplate<ChattingResponse>(
							e.getWorkspaceId(),
							e.getRoomId(),
							ChattingResponse.builder()
								.id(e.getId())
								.roomId(e.getRoomId())
								.workspaceId(e.getWorkspaceId())
								.chatting(Json.of(e.getChatting()))
								.createAt(LocalDateTime.now())
								.updateAt(LocalDateTime.now())
								.fullName(account.getFullName())
								.accountName(account.getAccountName())
							.build(),
							ServerSentStreamType.CHATTING_ACCEPT
						) {}
					);
					if (result.isFailure()) {
						// do something here, since emission failed
					}
				})
				;
			})
			.flatMap(e-> response(Result._0, e))
		, ResponseWrapper.class);
	}

	public Mono<ServerResponse> searchChattingList(ServerRequest request){
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		Long roomId = Long.valueOf(request.pathVariable("roomId"));
		
		var result = accountService.convertRequestToAccount(request)
		.filterWhen(account -> roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), workspaceId, roomId))
		.switchIfEmpty(Mono.error(new RoomException(Result._301)))
		.flatMap(account -> {
			var param = request.queryParams();
			
			PageRequest pageRequest = PageRequest.of(
				Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
				Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
			);
			return chattingRepository.findAllJoinAccountByWorkspaceIdAndRoomId(workspaceId, roomId, pageRequest)
				.collectList()
				.zipWith(chattingRepository.countJoinAccountByWorkspaceIdAndRoomId(workspaceId, roomId))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
		});

		return ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(result.flatMap(e-> response(Result._0, e)), ResponseWrapper.class);
	}
}
