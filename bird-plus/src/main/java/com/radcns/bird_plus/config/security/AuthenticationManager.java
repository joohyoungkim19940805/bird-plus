package com.radcns.bird_plus.config.security;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Component;

import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.AccountException;
import reactor.core.publisher.Mono;


@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
	private AccountRepository accountRepository;
	
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
    	System.out.println("kjh test <<<<123 ");
    	System.out.println(authentication.getCredentials());
    	System.out.println(authentication.getDetails());
    	System.out.println(authentication.getName());
    	System.out.println(authentication.getAuthorities());
    	System.out.println(authentication.getAuthorities().toArray()[0]);
    	System.out.println(((org.springframework.security.core.authority.SimpleGrantedAuthority)authentication.getAuthorities().toArray()[0]).getAuthority())	;
    	System.out.println(authentication.getAuthorities().stream().anyMatch(e->e.getAuthority().equals(Role.ROLE_GUEST.name())));
    	authentication.getAuthorities().stream().anyMatch(e->e.getAuthority().equals(Role.ROLE_BOT.name()));
    	UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    	System.out.println(principal.getId());
    	System.out.println(principal.getName());
    	return accountRepository.findByEmail(principal.getId())
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