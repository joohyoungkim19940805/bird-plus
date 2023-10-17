package com.radcns.bird_plus.config.security;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private static final String BEARER_TEXT = "bearer-";
    private static final Integer BEARER_TEXT_LENGTH = BEARER_TEXT.length();
    
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
    		HttpCookie obj = serverWebExchange.getRequest().getCookies().getFirst(HttpHeaders.AUTHORIZATION);
    		if(obj != null) {
    			auth = obj.getValue();
    			if(auth.isEmpty()) {
    				auth = null;
    			}
    		}else {
	    		String[] paths = serverWebExchange.getRequest().getPath().pathWithinApplication().value().split("/");
	    		
	    		auth = paths.length == 0 ? "" : paths[paths.length - 1];

	    		if(auth.contains(BEARER_TEXT)) {
	    			auth = auth.substring(BEARER_TEXT_LENGTH);
	    		}else {
	    			auth = null;
	    		}

    		}
    	}
    	//System.out.println("kjh test <<< auth");
    	//System.out.println(auth);
    	//Authorization
        return Mono.justOrEmpty(auth);
    }

	@Override
	public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
		// TODO Auto-generated method stub
		return Mono.justOrEmpty(serverWebExchange)
            .flatMap(ServerHttpBearerAuthenticationConverter::extract)
            .map(token -> new UsernamePasswordAuthenticationToken(token, token) );
            //.flatMap(jwtVerifier::check)
            //.flatMap(CurrentUserAuthenticationBearer::create);
	}
	
}
