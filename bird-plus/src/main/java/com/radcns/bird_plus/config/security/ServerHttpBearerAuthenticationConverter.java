package com.radcns.bird_plus.config.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 
 * @author oozu1
 *
 */
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtVerifyHandler jwtVerifier;

    public ServerHttpBearerAuthenticationConverter(JwtVerifyHandler jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    public static Mono<String> extract(ServerWebExchange serverWebExchange) {
    	//serverWebExchange.getRequest().getCookies().set("", null);
    	//
    	/*
    	System.out.println("kjh test<<< ");
    	System.out.println(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    	System.out.println(serverWebExchange.getRequest().getCookies());
    	*/
    	String auth = serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    	if(auth == null || auth.isEmpty()) {
    		String[] paths = serverWebExchange.getRequest().getPath().pathWithinApplication().value().split("/");
    		auth = paths[paths.length - 1];

    		if(auth.contains("bearer-")) {
    			auth = auth.replace("bearer-","");
    		}else {
    			auth = null;
    		}
    	}
    	System.out.println("kjh test <<<");
    	System.out.println(auth);
    	System.out.println(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    	
    	//Authorization
        return Mono.justOrEmpty(auth);
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
