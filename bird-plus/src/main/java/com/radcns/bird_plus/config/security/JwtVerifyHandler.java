package com.radcns.bird_plus.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.radcns.bird_plus.util.exception.UnauthorizedException;

public class JwtVerifyHandler {
	@Value("${jjwt.password.secret}")
	private String secret;

    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify(String token) {
        var claims = getAllClaimsFromToken(token);
        final Date expiration = claims.getExpiration();

        if (expiration.before(new Date()))
            throw new RuntimeException("Token expired");

        return new VerificationResult(claims, token);
    }

    public Claims getAllClaimsFromToken(String token) {
    	System.out.println("kjh test <<<<<<<<" + Base64.getEncoder().encodeToString(secret.getBytes()));
        return Jwts.parserBuilder()
        		.setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}

