package com.radcns.bird_plus.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.config.security.Token;
import com.radcns.bird_plus.entity.AccountEntity;
import com.radcns.bird_plus.entity.AccountLogEntity;
import com.radcns.bird_plus.repository.AccountLogRepository;
import com.radcns.bird_plus.repository.AccountRepository;
import com.radcns.bird_plus.util.exception.AuthException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("serial")
@Component
@Service
public class AccountService implements Serializable {
    @Autowired
	private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountLogRepository accountLogRepository;
    @Autowired
    private ObjectMapper om;
    
    @Value("${jjwt.password.secret}")
    private String secret;
    
    @Autowired
    private KeyPair keyPair;
    
    @Value("${jjwt.password.expiration}")
    private String defaultExpirationTimeInSecondsConf;

    /**
     * 엑세스 토큰을 생성하는 함수
     * @param user
     * @return
     */
    private Token generateAccessToken(AccountEntity user) {
    	
    	Map<String, List<Role>> claims = Map.of("role", user.getRoles());
    	String issuer = user.getAccountName();
    	String subject = user.getId().toString();
    	
        var expirationTimeInMilliseconds = Long.parseLong(defaultExpirationTimeInSecondsConf) * 1000;
        var expirationDate = new Date(new Date().getTime() + expirationTimeInMilliseconds);
        var createdDate = new Date();
        var token = Jwts.builder()
        		.serializeToJsonWith(new JacksonSerializer<Map<String,?>>(this.om))
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                //.setHeaderParams(Map.of("typ", "jwt", "alg", "HS256"))
                //.signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .setHeaderParams(Map.of("typ", "jwt", "alg", "RS256"))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();

        return Token.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .isDifferentIp(user.getIsDifferentIp())
                .isFirstLogin(user.getIsFirstLogin())
                .build();
    }

    public Mono<Token> authenticate(Mono<AccountEntity> accountEntityMono, Optional<InetSocketAddress> optional) {
    	return accountEntityMono.flatMap(accountInfo -> {
    		return accountRepository.findByAccountName(accountInfo.getAccountName()).doOnSuccess(account->{
    			if(account == null) {
    				throw new AuthException(103);
    			}
    		}).flatMap(account -> {
        		if ( ! account.getIsEnabled()) {
    				return Mono.error(new AuthException(101));
    			}else if(!passwordEncoder.encode(accountInfo.getPassword()).equals(account.getPassword())) {
    				return Mono.error(new AuthException(102));
             	}else {
             		AccountLogEntity accountLogEntity = AccountLogEntity.builder()
             			.id(account.getId())
             			.ip(optional.get().getAddress().getHostAddress())
             			.build();
             		return accountLogRepository.existsByIp(accountLogEntity.getIp()).flatMap(existsByIp->{
             			account.setIsDifferentIp( ! existsByIp);
             			if(account.getIsFirstLogin()) {
             				if(existsByIp) {
             					account.setIsFirstLogin(false);
             				}
             				account.setIsDifferentIp(false);
             			}
             			return accountRepository.save(account)
             				.flatMap(e->accountLogRepository.save(accountLogEntity));
             		}).flatMap(e -> Mono.just(generateAccessToken(account).toBuilder()
                     			.userId(account.getId())
                     			.build())
             		);
             	}
        	});
    	});
    }
    
    public Flux<AccountEntity> createUser(Mono<AccountEntity> accountMono) {
    	
    	return accountRepository.saveAll(
			accountMono.map(account -> 
    	    	account.toBuilder()
    				.password(passwordEncoder.encode(account.getPassword()))
    				.roles(List.of(Role.ROLE_USER, Role.ROLE_GUEST))
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
