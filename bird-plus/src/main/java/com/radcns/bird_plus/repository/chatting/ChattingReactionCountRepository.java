package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingReactionCountEntity;
import com.radcns.bird_plus.entity.chatting.ChattingReactionCountEntity.ChattingReactionCountDomain.ChattingReactionCountResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChattingReactionCountRepository extends ReactiveCrudRepository<ChattingReactionCountEntity, Long> {

	Mono<Boolean> existsByChattingIdAndAccountIdAndEmoticonIdAndReactionId(Long chattingId, Long accountId, Long emoticonId, Long reactionId);

	Mono<Long> countByChattingIdAndEmoticonIdAndReactionId(Long chattingId, Long emoticonId, Long reactionId);
	
	@Query("""
			SELECT
				aa.full_name,
				aa.id as account_id
			FROM
				ch_chatting_reaction_count ccrc
			INNER JOIN
				ac_account aa
			ON	
				ccrc.account_id = aa.id
			WHERE
				ccrc.chatting_id = :#{[0]}
			AND
				ccrc.emoticon_id = :#{[1]}
			AND
				ccrc.reaction_id = :#{[2]}
			""")
	Flux<ChattingReactionCountResponse> findJoinByChattingIdAndEmoticonIdAndReactionId(Long chattingId, Long emoticonId, Long reactionId);
	
	Mono<Void> deleteByChattingIdAndAccountIdAndEmoticonIdAndReactionId(Long chattingId, Long accountId, Long emoticonId, Long reactionId);
	
}