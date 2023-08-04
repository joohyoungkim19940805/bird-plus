package com.radcns.bird_plus.web.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.Response;

import reactor.core.publisher.Mono;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class LoginHandler {
	
	public Mono<ServerResponse> isLogin(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(response(Result._0, null)), Response.class);
	}

}
