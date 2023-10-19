package com.radcns.bird_plus.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class WebFluxFilter implements HandlerFilterFunction<ServerResponse, ServerResponse>  {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        System.out.println("kjh testaaa <<<");
        
        return next.handle(request).doOnNext(resonse -> {
        	if(resonse.cookies().get(HttpHeaders.AUTHORIZATION).size() == 0) {
            	String auth = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
            	if(auth != null && ! auth.isEmpty()) {
            		resonse.cookies().add(HttpHeaders.AUTHORIZATION, ResponseCookie.from(HttpHeaders.AUTHORIZATION, auth)
            				.httpOnly(true)
							.secure(true)
							.sameSite("Strict")
            				.build());
            	}
        	}
        });
    }
}
