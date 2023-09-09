package com.radcns.bird_plus.web.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.config.security.JwtVerifyHandler;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.repository.chatting.ChattingRepository;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.service.AccountService;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;


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
	public Mono<ServerResponse> sendStream(ServerRequest request){
		//chattingSink.emitComplete(null);
		//chattingSink.currentSubscriberCount();
		return request.bodyToMono(String.class)
				/*.doOnNext(chatting->{
					chattingSink.tryEmitNext(chatting);
				})*/
				//chattingSink.actuals()
				.flatMap(chatting -> {
					String token = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
					Claims claims = jwtVerifyHandler.getJwt(token).getBody();
					ChattingEntity chattingEntity = ChattingEntity
							.builder()
							//.accountId(Long.parseLong(claims.getSubject()))
							.accountName(claims.getIssuer())
							.chatting(chatting)
							.build();
					
					EmitResult result = chattingSink.tryEmitNext(chattingEntity);
					//chattingSink.
					if (result.isFailure()) {
						// do something here, since emission failed
					}
					
					return accountRepository.findByAccountNameAndEmail(claims.getIssuer(), claims.getSubject())
							.map(e->chattingEntity.withAccountId(e.getId()));
				})
				.flatMap(chattingEntity->{
					return ok().contentType(MediaType.APPLICATION_JSON).body(
							chattingRepository.save(chattingEntity).map(e->e.withAccountId(null)), ChattingEntity.class
					);
				})
				//.log()
				//.onErrorResume(e -> Mono.error(new UnauthorizedException(Result._999)))
				;
				
		/*
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(request.bodyToMono(String.class).map(e->response(Result._00, e)), Response.class)
				.onErrorResume(e -> Mono.error(new UnauthorizedException(Result._100)));
		*/
	}
	public Mono<ServerResponse> emissionStream(ServerRequest request) {
		return ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(chattingSink.asFlux().map(e->{
					accountService.convertJwtToAccount(request).subscribe(acc->{
						System.out.println(acc);	
						
					});
					e.setAccountId(null);
					return e;
				}), ChattingEntity.class)
				//.log();
				;
	}
	
}
