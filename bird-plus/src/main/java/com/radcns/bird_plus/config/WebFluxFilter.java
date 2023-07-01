package com.radcns.bird_plus.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
/*
@Component
public class WebFluxFilter implements HandlerFilterFunction<ServerResponse, ServerResponse>  {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        System.out.println("kjh test <<<");
        
        return next.handle(request);
    }
}
*/