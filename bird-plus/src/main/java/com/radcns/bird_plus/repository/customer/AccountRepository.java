package com.radcns.bird_plus.repository.customer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.customer.AccountEntity;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {

	Mono<AccountEntity> findByAccountName(String accountName);
	
}