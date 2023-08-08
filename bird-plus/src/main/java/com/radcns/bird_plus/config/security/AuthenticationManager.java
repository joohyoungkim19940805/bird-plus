package com.radcns.bird_plus.config.security;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Component;

import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.AccountException;
import reactor.core.publisher.Mono;


@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired 
	private AccountService accountService;
	
	
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
    	
    	UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

    	return accountService.getUser(principal.getId())
            //.filter(user -> user.getIsEnabled())
            .switchIfEmpty(Mono.error(new AccountException(Result._104)))
            .map(user -> authentication);
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