package com.radcns.bird_plus.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
    	//throw new UnsupportedOperationException("save is auto implement. so not used this method.");
        
    	return ServerHttpBearerAuthenticationConverter.extract(swe)
    	.map(token -> new UsernamePasswordAuthenticationToken(token, token))
    	.flatMap(tokenAuth -> this.authenticationManager.authenticate(tokenAuth))
    	.flatMap(auth -> {
    		sc.setAuthentication(auth);
    		return Mono.empty();
    	});
    	
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return ServerHttpBearerAuthenticationConverter.extract(swe)
        	.flatMap(token-> {
        		Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        		return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
        	});
    }
}