package com.radcns.bird_plus.repository.customer;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.account.AccountLogEntity;

import reactor.core.publisher.Mono;

public interface AccountLogRepository extends ReactiveCrudRepository<AccountLogEntity, Long>{
	Mono<Boolean> existsByIp(String ip);
}
