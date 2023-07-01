package com.radcns.bird_plus.repository.chatting;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.customer.AccountEntity;

import reactor.core.publisher.Mono;

public interface ChattingRepository extends ReactiveCrudRepository<ChattingEntity, Long> {

}