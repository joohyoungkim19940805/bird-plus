package com.radcns.bird_plus.repository.chatting;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;

import reactor.core.publisher.Mono;

public interface ChattingRepository extends ReactiveCrudRepository<ChattingEntity, Long> {

}