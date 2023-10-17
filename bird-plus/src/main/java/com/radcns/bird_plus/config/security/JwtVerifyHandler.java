package com.radcns.bird_plus.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;

import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import reactor.core.publisher.Mono;

import java.security.KeyPair;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.AccountException;


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
                	return Mono.error(new AccountException(Result._100));
                });
    }

    private VerificationResult verify(String token) {
        var claims = getJwt(token).getBody();
        final Date expiration = claims.getExpiration();
        /**
         * Jwts 라이브러리에서 만료 시간 확인 후 throw 중인데 필요한 로직인지?
         */
        if (expiration.before(new Date())) {
            throw new AccountException(Result._100);
        }

        return new VerificationResult(claims, token);
    }

    public Jws<Claims> getJwt(String token) {
    	try {
    		return Jwts.parserBuilder()
        		.deserializeJsonWith(new JacksonDeserializer<Map<String,?>>(this.om))
        		.setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token);
    	}catch(ExpiredJwtException e) {
    		//e.printStackTrace();
    		throw new AccountException(Result._100);
    	}catch(UnsupportedJwtException e) {
    		//e.printStackTrace();
    		throw new AccountException(Result._105);
    	}catch(MalformedJwtException e) {
    		//e.printStackTrace();
    		throw new AccountException(Result._106);
    	}catch(SignatureException e) {
    		//e.printStackTrace();
    		throw new AccountException(Result._107);
    	}
    }

    @Getter
    public class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}

