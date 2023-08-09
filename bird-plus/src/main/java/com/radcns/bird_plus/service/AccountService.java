package com.radcns.bird_plus.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonSerializer;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.config.security.JwtIssuerType;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.config.security.Token;
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.account.AccountLogEntity;
import com.radcns.bird_plus.repository.customer.AccountLogRepository;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.AccountException;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public PasswordEncoder passwordEncoder;
    @Autowired
    private AccountLogRepository accountLogRepository;
	@Autowired
	private MailService mailService;
    @Autowired
    private ObjectMapper om;

    @Autowired
    private KeyPair keyPair;
    
    /**
     * 엑세스 토큰을 생성하는 함수
     * @param user
     * @return
     */
    public Token generateAccessToken(AccountEntity account, JwtIssuerType type) {

    	Map<String, List<Role>> claims = Map.of("role", account.getRoles());
    	
        var expirationTimeInMilliseconds = type.getSecond() * 1000;
        var expirationDate = new Date(new Date().getTime() + expirationTimeInMilliseconds);
        var createdDate = new Date();
        
        var token = Jwts.builder()
        		.serializeToJsonWith(new JacksonSerializer<Map<String,?>>(this.om))
                .setClaims(claims)
                .setIssuer(account.getAccountName())
                .setSubject(account.getEmail())
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                //.setHeaderParams(Map.of("typ", "jwt", "alg", "HS256"))
                //.signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .setHeaderParams(Map.of(
                		"typ", "jwt", 
                		"alg", "RS256", 
                		"name", account.getFullName(),
                		"jwtIssuerType", type.name())
                )
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256);
                
        if( ! type.equals(JwtIssuerType.BOT)) {
        	token.setExpiration( new Date(new Date().getTime() + LocalDate.of(9000, 12, 31).atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli()));
        }
        return Token.builder()
                .token(token.compact())
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .isDifferentIp(account.getIsDifferentIp())
                .isFirstLogin(account.getIsFirstLogin())
                .build();
    }

    public Mono<Token> authenticate(Mono<AccountEntity> accountEntityMono, Optional<InetSocketAddress> optional) {
    	return accountEntityMono.flatMap(accountInfo -> {
    		return accountRepository.findByAccountName(accountInfo.getAccountName())
	    		.switchIfEmpty(
	    			Mono.error(new AccountException(Result._103))
	    		).flatMap(account -> {
	    			System.out.println("kjh test <<<");
	    			System.out.println(account);
	    			System.out.println(account.getUpdateMils());
	    			System.out.println(account.getUpdateMils());
	    			
	        		if ( ! account.getIsEnabled()) {
	        			// 이메일 전송이 오래걸리므로 응답에 3~6초씩 걸림
						// 병목이 발생하지 않도록 별도 스레드를 통해 처리한다.
						Mono.just(account)
						.publishOn(Schedulers.boundedElastic())
						.subscribe(e->{
							mailService.sendAccountVerifyTemplate(e);
						});
	    				return Mono.error(new AccountException(Result._101));
	    			}else if(!passwordEncoder.encode(accountInfo.getPassword()).equals(account.getPassword())) {
	    				return Mono.error(new AccountException(Result._102));
	             	}else {
	             		AccountLogEntity accountLogEntity = AccountLogEntity.builder()
	             			.accountId(account.getId())
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
	             		}).flatMap(e -> Mono.just(generateAccessToken(account, JwtIssuerType.ACCOUNT).toBuilder()
	                     			.userId(account.getId())
	                     			.build())
	             		);
	             	}
	        	});
    	});
    }
    
    public Mono<AccountEntity> createUser(Mono<AccountEntity> accountMono) {
    	return accountMono
    			.map(account -> account.toBuilder()
    					.password(passwordEncoder.encode(account.getPassword()))
        				.roles(List.of(Role.ROLE_USER, Role.ROLE_GUEST))
        			    .isEnabled(false)
        			    .build())
    			.flatMap(account -> accountRepository.save(account));
    	/*
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
    	*/
    }

    public Mono<AccountEntity> getUser(Long userId) {
        return accountRepository.findById(userId);
    }
    public Mono<AccountEntity> getUser(String email) {
        return accountRepository.findByEmail(email);
    }
    
}
