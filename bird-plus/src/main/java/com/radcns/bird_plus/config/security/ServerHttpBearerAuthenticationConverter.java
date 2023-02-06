package com.radcns.bird_plus.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {
    private static final String BEARER = "Bearer ";
    private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String, Mono<String>> isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length()));
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
            .filter(matchBearerLength)
            .flatMap(isolateBearerValue)
            .flatMap(jwtVerifier::check)
            .flatMap(CurrentUserAuthenticationBearer::create);
	}
}
