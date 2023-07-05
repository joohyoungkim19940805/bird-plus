package com.radcns.bird_plus.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class CurrentUserAuthenticationBearer {
    public static Mono<Authentication> create(JwtVerifyHandler.VerificationResult verificationResult) {
    	Claims claims = verificationResult.claims;
    	System.out.println("kjh test <<<<<" + claims.getSubject());
        String subject = claims.getSubject();
        List<String> roles = claims.get("role", List.class);
        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        var principalId = 0L;

        try {
            principalId = Long.parseLong(subject);
        } catch (NumberFormatException ignore) { }

        if (principalId == 0)
            return Mono.empty(); // invalid value for any of jwt auth parts

        var principal = new UserPrincipal(principalId, claims.getIssuer());

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }

}
