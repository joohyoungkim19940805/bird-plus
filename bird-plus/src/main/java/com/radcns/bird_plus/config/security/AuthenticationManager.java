package com.radcns.bird_plus.config.security;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;


@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
    	
    	/*
    	return accountRepository.findByEmail(principal.getId())
            //.filter(user -> user.getIsEnabled())
            .switchIfEmpty(Mono.error(new AccountException(Result._104)))
            .map(user -> authentication);
        */
    	return Mono.just(authentication.getCredentials().toString())
		.flatMap(jwtVerifyHandler::check)
		.flatMap(verificationResult-> {
			Claims claims = verificationResult.claims;
			String subject = claims.getSubject();
			@SuppressWarnings("unchecked")
			List<String> roles = claims.get("role", List.class);
			var authorities = roles.stream()
					.map(SimpleGrantedAuthority::new)
					.toList();
			
			if (subject == null)
				return Mono.empty(); // invalid value for any of jwt auth parts
			
			var principal = new UserPrincipal(subject, claims.getIssuer());
			Authentication auth = new UsernamePasswordAuthenticationToken(principal, verificationResult.getToken(), authorities);
			ReactiveSecurityContextHolder.withAuthentication(auth);
			
			return Mono.justOrEmpty(auth);
		});
    	//return null;
    }
    /*
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String accountName = jwtUtil.getUsernameFromToken(authToken);
        return Mono.just(jwtUtil.validateToken(authToken))
            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty())
            .map(valid -> {
                Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                List<String> rolesMap = claims.get("role", List.class);
                return new UsernamePasswordAuthenticationToken(
                    accountName,
                    null,
                    rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
            });
    }
    */
    
}