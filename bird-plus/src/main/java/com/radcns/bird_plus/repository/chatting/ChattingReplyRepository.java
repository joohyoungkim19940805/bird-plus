package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingReplyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface ChattingReplyRepository extends ReactiveCrudRepository<ChattingReplyEntity, Long> {}