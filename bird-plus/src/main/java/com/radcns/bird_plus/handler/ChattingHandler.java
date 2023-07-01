package com.radcns.bird_plus.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.config.security.JwtVerifyHandler;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.repository.chatting.ChattingRepository;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;


@Component
public class ChattingHandler {
	
	@Autowired
	private ChattingRepository chattingRepository;
	
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	
	private Sinks.Many<Object> chattingSink = Sinks.many().replay().all();
	
	public Mono<ServerResponse> addStream(ServerRequest request){
		return request.bodyToMono(Object.class)
				.doOnNext(chatting->{
					System.out.println("kjh !!!! " + chatting);
					chattingSink.tryEmitNext(chatting);
				})
				.map(chatting -> {
					System.out.println("kjh ???? " + chatting);
					String token = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
					System.out.println("kjh test <<<11111");
					System.out.println(token);
					Claims claims = jwtVerifyHandler.getAllClaimsFromToken(token);
					ChattingEntity chattingEntity = ChattingEntity
							.builder()
							.accountId(Long.parseLong(claims.getSubject()))
							.chatting(chatting.toString())
							.build();
					return chattingEntity;
				})
				.flatMap(chattingEntity->{
					
					return ok().contentType(MediaType.APPLICATION_JSON).body(chattingRepository.save(chattingEntity), ChattingEntity.class);
				})
				.onErrorResume(e -> Mono.error(new UnauthorizedException(100)));
				
		/*
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(request.bodyToMono(String.class).map(e->response(Result._00, e)), Response.class)
				.onErrorResume(e -> Mono.error(new UnauthorizedException(100)));
		*/
	}
	public Mono<ServerResponse> getStream(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_NDJSON)
				.body(chattingSink.asFlux(), ChattingEntity.class).log();
	}
}
