package com.radcns.bird_plus.repository.customer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.account.AccountEntity;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {

	Mono<AccountEntity> findByAccountName(String accountName);
	
	Mono<AccountEntity> findByEmail(String email);

	Mono<AccountEntity> findByAccountNameAndEmail(String accountName, String email);
	
	
}