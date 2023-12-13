package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingCommentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface ChattingCommentRepository extends ReactiveCrudRepository<ChattingCommentEntity, Long> {}