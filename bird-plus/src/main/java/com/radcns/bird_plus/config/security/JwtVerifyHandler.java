package com.radcns.bird_plus.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.util.exception.UnauthorizedException;

public class JwtVerifyHandler {
	private String secret;
    private ObjectMapper om;
	public JwtVerifyHandler(String secret, ObjectMapper om){
		this.secret = secret;
		this.om = om;
	}
	
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
    	try {
        return Jwts.parserBuilder()
        		.deserializeJsonWith(new JacksonDeserializer<Map<String,?>>(this.om))
        		.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
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

