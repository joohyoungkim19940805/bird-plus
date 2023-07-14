package com.radcns.bird_plus.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;

import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import reactor.core.publisher.Mono;

import java.security.KeyPair;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.util.exception.AuthException;
import com.radcns.bird_plus.util.exception.UnauthorizedException;

public class JwtVerifyHandler {
	private KeyPair keyPair;
    private ObjectMapper om;
	public JwtVerifyHandler(KeyPair keyPair, ObjectMapper om){
		this.keyPair = keyPair;
		this.om = om;
	}
	
	/**
	 * 토큰의 유효성과 만료시간을 체크하는 함수
	 * @param accessToken
	 * @return
	 */
    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> {
                	return Mono.error(new UnauthorizedException(100));
                });
    }

    private VerificationResult verify(String token) {
        var claims = getAllClaimsFromToken(token);
        final Date expiration = claims.getExpiration();
        /**
         * Jwts 라이브러리에서 만료 시간 확인 후 throw 중인데 필요한 로직인지?
         */
        if (expiration.before(new Date())) {
            throw new UnauthorizedException(100);
        }

        return new VerificationResult(claims, token);
    }

    public Claims getAllClaimsFromToken(String token) {
    	try {
    		return Jwts.parserBuilder()
        		.deserializeJsonWith(new JacksonDeserializer<Map<String,?>>(this.om))
        		.setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    	}catch(ExpiredJwtException e) {
    		throw new AuthException(100, e);
    	}catch(UnsupportedJwtException e) {
    		throw new AuthException(105, e);
    	}catch(MalformedJwtException e) {
    		throw new AuthException(106, e);
    	}catch( SignatureException e) {
    		throw new AuthException(107, e);
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

