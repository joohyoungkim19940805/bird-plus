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
import com.radcns.bird_plus.repository.chatting.ChattingRepository;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import io.jsonwebtoken.Claims;
import io.r2dbc.postgresql.codec.Json;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;

import static com.radcns.bird_plus.util.Response.response;
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
	/*
		Map(멀티스레드용 맵에 workspaceId-key, sinks-value로 저장)
		클라이언트에서 workspaceId와 roomId 보내줌
		서버에서 roomId에 해당하는 참여자가 아닌 경우를 필터링
		reuqest는 ChattingEntity로 받도록 변경 필요
		
		채팅의 경우 mainPageRenderer에 엔터 누를시 현재 캐럿 위치의 다음 next요소들을 긁어서 다음 라인 요소에 집어넣는 로직 개발 필요
		
		채팅 저장시 페이징용 max 순번 저장 (room 기준으로)
		동일 순번이 저장될 수 있으므로 클라이언트에서는 id나 or createAt(createMils)으로 정렬 필요
	*/
	//ConcurrentMap<K, V>
	//ConcurrentHashMap<K, V>
	public Mono<ServerResponse> sendStream(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				return request.bodyToMono(ChattingEntity.class)
				.flatMap(chattingEntity -> 
					chattingRepository.save(
						chattingEntity
						.withAccountId(account.getId())
						.withCreateBy(account.getId())
						.withUpdateBy(account.getId())
						.withAccountId(account.getId())
						.withUpdateByArray(List.of(account.getId()))
						.withAccountName(account.getAccountName())
					)
				)
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
							ServerSentStreamType.CHTTING_ACCEPT
						) {}
					);
					if (result.isFailure()) {
						// do something here, since emission failed
					}
				})
				;
			})
			.map(e-> response(Result._0, e))
		, Response.class);
	}

	public Mono<ServerResponse> searchChattingList(ServerRequest request){
		
		var result = accountService.convertJwtToAccount(request)
		.flatMap(account -> {
			var param = request.queryParams();
			Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
			Long roomId = Long.valueOf(param.getFirst("roomId"));
			if(workspaceId == null || roomId == null) {
				return Mono.empty();
			}
			
			PageRequest pageRequest = PageRequest.of(
				Integer.valueOf(param.getOrDefault("page", List.of("0")).get(0)),
				Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
			);
			return chattingRepository.findAllJoinAccountByWokrpsaceIdAndRoomId(workspaceId, roomId, pageRequest)
				.collectList()
				.zipWith(chattingRepository.countJoinAccountByWokrpsaceIdAndRoomId(workspaceId, roomId))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
		});

		return ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(result.map(e-> response(Result._0, e)), Response.class);
	}
}
