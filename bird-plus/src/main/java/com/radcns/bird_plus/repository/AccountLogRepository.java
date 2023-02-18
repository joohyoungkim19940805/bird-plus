package com.radcns.bird_plus.repository;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.AccountLogEntity;

import reactor.core.publisher.Mono;

public interface AccountLogRepository extends ReactiveCrudRepository<AccountLogEntity, Long>{
	Mono<Boolean> existsByIp(String ip);
}
