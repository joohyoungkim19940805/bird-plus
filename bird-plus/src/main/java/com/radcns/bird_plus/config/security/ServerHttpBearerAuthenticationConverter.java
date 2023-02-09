package com.radcns.bird_plus.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtVerifyHandler jwtVerifier;

    public ServerHttpBearerAuthenticationConverter(JwtVerifyHandler jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    public static Mono<String> extract(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }

	@Override
	public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
		// TODO Auto-generated method stub
		return Mono.justOrEmpty(serverWebExchange)
            .flatMap(ServerHttpBearerAuthenticationConverter::extract)
            .flatMap(jwtVerifier::check)
            .flatMap(CurrentUserAuthenticationBearer::create);
	}
}
