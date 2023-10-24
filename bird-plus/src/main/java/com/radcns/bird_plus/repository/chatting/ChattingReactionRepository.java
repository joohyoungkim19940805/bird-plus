package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface ChattingReactionRepository extends ReactiveCrudRepository<ChattingReactionEntity, Long> {}