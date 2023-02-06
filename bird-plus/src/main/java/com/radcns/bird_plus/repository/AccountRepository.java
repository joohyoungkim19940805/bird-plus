package com.radcns.bird_plus.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.AccountEntity;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {

	Mono<AccountEntity> findByUsername(String username);
	
}
