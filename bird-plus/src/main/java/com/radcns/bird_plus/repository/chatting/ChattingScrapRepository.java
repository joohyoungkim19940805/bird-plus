package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingScrapEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface ChattingScrapRepository extends ReactiveCrudRepository<ChattingScrapEntity, Long> {}