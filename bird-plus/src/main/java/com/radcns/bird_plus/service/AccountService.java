package com.radcns.bird_plus.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.config.security.JwtIssuerType;
import com.radcns.bird_plus.config.security.JwtVerifyHandler;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.config.security.Token;
import com.radcns.bird_plus.config.security.TokenTemplate;
import com.radcns.bird_plus.config.security.UserPrincipal;
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.account.AccountLogEntity;
import com.radcns.bird_plus.repository.account.AccountLogRepository;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.AccountException;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.time.LocalDate;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Component
@Service
public class AccountService  {
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
    
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	
    /**
     * 엑세스 토큰을 생성하는 함수
     * @param user
     * @return
     */
    public Token generateAccessToken(TokenTemplate tokenTemplate, JwtIssuerType type) {

    	Map<String, List<Role>> claims = Map.of("role", tokenTemplate.getRoles());
    	
        var createdDate = new Date();
        
        var token = Jwts.builder()
        		.serializeToJsonWith(new JacksonSerializer<Map<String,?>>(this.om))
                .setClaims(claims)
                .setIssuer(tokenTemplate.getIssuer())
                .setSubject(tokenTemplate.getSubject())
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                //.setHeaderParams(Map.of("typ", "jwt", "alg", "HS256"))
                //.signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .setHeaderParams(Map.of(
                		"typ", "jwt", 
                		"alg", "RS256", 
                		"name", tokenTemplate.getName(),
                		"jwtIssuerType", type.name())
                )
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256);
        
        Date expirationDate = null;
        if(type.equals(JwtIssuerType.BOT)) {
        	expirationDate = new Date(new Date().getTime() + LocalDate.of(9000, 12, 31).atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
        	token.setExpiration(expirationDate);
        }else {
        	var expirationTimeInMilliseconds = type.getSecond() * 1000;
            expirationDate = new Date(new Date().getTime() + expirationTimeInMilliseconds);
        	token.setExpiration(expirationDate);
        }
        return Token.builder()
                .token(token.compact())
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                //.isDifferentIp(account.getIsDifferentIp())
                //.isFirstLogin(account.getIsFirstLogin())
                .build();
    }

    public Mono<Token> authenticate(Mono<AccountEntity> accountEntityMono, Optional<InetSocketAddress> optional) {
    	return accountEntityMono.flatMap(accountInfo -> {
    		return accountRepository.findByAccountName(accountInfo.getAccountName())
	    		.switchIfEmpty(
	    			Mono.error(new AccountException(Result._103))
	    		).flatMap(account -> {
	    			if ( ! account.getIsEnabled()) {
	        			// 이메일 전송이 오래걸리므로 응답에 3~6초씩 걸림
						// 병목이 발생하지 않도록 별도 스레드를 통해 처리한다.
	        			Mono.fromRunnable(()->{
	        				mailService.sendAccountVerifyTemplate(account);
	        			})
	        			.subscribeOn(Schedulers.boundedElastic())
	        			.subscribe();
	        			/*
	        			Mono.just(account)
						.publishOn(Schedulers.boundedElastic())
						.subscribe(e->{
							mailService.sendAccountVerifyTemplate(e);
						});
						*/
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
	                     			//.userId(account.getId())
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
    
    public Mono<AccountEntity> convertRequestToAccount(ServerRequest request) {
    	
    	return ReactiveSecurityContextHolder.getContext()
    			.map(SecurityContext::getAuthentication)
    			.flatMap(auth->{
    				UserPrincipal userPrincipal = (UserPrincipal)auth.getPrincipal();
	    			return accountRepository.findByEmail(userPrincipal.getId())
	    			.switchIfEmpty(accountRepository.findByAccountName(userPrincipal.getName()));
    			});
    }
}
