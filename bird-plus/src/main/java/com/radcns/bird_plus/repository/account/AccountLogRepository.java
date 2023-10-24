package com.radcns.bird_plus.repository.account;
import com.radcns.bird_plus.entity.account.AccountLogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface AccountLogRepository extends ReactiveCrudRepository<AccountLogEntity, Long> {
    Mono<Boolean> existsByIp(String ip);
}