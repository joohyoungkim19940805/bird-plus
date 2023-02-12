package com.radcns.bird_plus.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.config.security.Token;
import com.radcns.bird_plus.entity.AccountEntity;
import com.radcns.bird_plus.repository.AccountRepository;
import com.radcns.bird_plus.util.exception.AuthException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@SuppressWarnings("serial")
@Component
@Service
public class AccountService implements Serializable {
    @Autowired
	private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper om;
    @Value("${jjwt.password.secret}")
    private String secret;

    @Value("${jjwt.password.expiration}")
    private String defaultExpirationTimeInSecondsConf;

    public Token generateAccessToken(AccountEntity user) {
    	Map<String, List<Role>> claims = Map.of("role", user.getRoles());

        return doGenerateToken(claims, user.getUsername(), user.getId().toString());
    }

    private Token doGenerateToken(Map<String, List<Role>> claims, String issuer, String subject) {
        var expirationTimeInMilliseconds = Long.parseLong(defaultExpirationTimeInSecondsConf) * 1000;
        var expirationDate = new Date(new Date().getTime() + expirationTimeInMilliseconds);

        return doGenerateToken(expirationDate, claims, issuer, subject);
    }

    private Token doGenerateToken(Date expirationDate, Map<String, List<Role>> claims, String issuer, String subject) {
        var createdDate = new Date();
        var token = Jwts.builder()
        		.serializeToJsonWith(new JacksonSerializer<Map<String,?>>(this.om))
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .setHeaderParams(Map.of("typ", "jwt", "alg", "HS256"))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

    public Mono<Token> authenticate(Mono<AccountEntity> accountEntityMono) {
    	return accountEntityMono.flatMap(accountInfo -> {
    		System.out.println("kjh test <<<");
    		System.out.println(accountInfo);
    		return accountRepository.findByUsername(accountInfo.getUsername()).doOnSuccess(account->{
    			System.out.println(account);
    			if(account == null) {
    				throw new AuthException(103);
    			}
    		}).flatMap(account -> {
        		if ( ! account.getIsEnabled()) {
    				return Mono.error(new AuthException(101));
    			}else if(!passwordEncoder.encode(accountInfo.getPassword()).equals(account.getPassword())) {
    				return Mono.error(new AuthException(102));
             	}else {
             		return Mono.just(generateAccessToken(account).toBuilder()
             			.userId(account.getId())
             			.build());
             	}
        	});
    	});
    }
    
    public Flux<AccountEntity> createUser(Mono<AccountEntity> accountMono) {
    	
    	return accountRepository.saveAll(
			accountMono.map(account -> 
    	    	account.toBuilder()
    				.password(passwordEncoder.encode(account.getPassword()))
    				.roles(List.of(Role.ROLE_USER, Role.ROLE_ACCESS))
    			    .isEnabled(true)
    			    .createAt(LocalDateTime.now())
    			    .build()
        	)
    	);
    }

    public Mono<AccountEntity> getUser(Long userId) {
        return accountRepository.findById(userId);
    }
    
   
}
