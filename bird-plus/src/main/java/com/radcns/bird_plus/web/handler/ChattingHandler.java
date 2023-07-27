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
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


@Component
public class ChattingHandler {
	
	@Autowired
	private ChattingRepository chattingRepository;
	
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	
	@Autowired
	private AccountRepository accountRepository;
	
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
	
	public Mono<ServerResponse> addStream(ServerRequest request){
		return request.bodyToMono(String.class)
				/*.doOnNext(chatting->{
					chattingSink.tryEmitNext(chatting);
				})*/
				.flatMap(chatting -> {
					String token = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
					Claims claims = jwtVerifyHandler.getJwt(token).getBody();
					ChattingEntity chattingEntity = ChattingEntity
							.builder()
							//.accountId(Long.parseLong(claims.getSubject()))
							.accountName(claims.getIssuer())
							.chatting(chatting)
							.build();
					chattingSink.tryEmitNext(chattingEntity);
					
					return accountRepository.findByAccountNameAndEmail(claims.getIssuer(), claims.getSubject())
							.map(e->chattingEntity.withAccountId(e.getId()));
				})
				.flatMap(chattingEntity->{
					return ok().contentType(MediaType.APPLICATION_JSON).body(
							chattingRepository.save(chattingEntity).map(e->e.withAccountId(null)), ChattingEntity.class
					);
				})
				//.log()
				.onErrorResume(e -> Mono.error(new UnauthorizedException(Result._999)))
				;
				
		/*
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(request.bodyToMono(String.class).map(e->response(Result._00, e)), Response.class)
				.onErrorResume(e -> Mono.error(new UnauthorizedException(Result._100)));
		*/
	}
	public Mono<ServerResponse> emissionStream(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(chattingSink.asFlux().map(e->{
					e.setAccountId(null);
					return e;
				}), ChattingEntity.class)
				//.log();
				;
	}

}
