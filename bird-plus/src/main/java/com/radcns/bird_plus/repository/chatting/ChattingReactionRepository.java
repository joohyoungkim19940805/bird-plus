package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity.ChattingReactionDomain.ChattingReactionResponse;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
public interface ChattingReactionRepository extends ReactiveCrudRepository<ChattingReactionEntity, Long> {
	
	Mono<ChattingReactionEntity> findByWorkspaceIdAndRoomIdAndChattingIdAndEmoticonId(Long workspaceId, Long roomId, Long chattingId, Long emoticonId);
	
	@Query("""
			SELECT 
    			json_agg(json_build_object(
    				'emoticon', sedp.emoticon,
    				'emoticon_type', sedp.emoticon_type,
    				'description', sedp.description,
    				'group_title', sedp.group_title ,
    				'subgroup_title', sedp.subgroup_title,
    				'reaction_id', ccr.id,
    				'reactionList', (
    					SELECT
    						json_agg( json_build_object( 
    							'fullName', aa2.full_name 
    						) )
    					FROM 
    						bird_plus.ch_chatting_reaction_count ccrc 
    					inner join
    						bird_plus.ac_account aa2
    					on
    						ccrc.account_id = aa2.id 
    					WHERE 
    						ccrc.reaction_id = ccr.id
    				)
    			))
    		FROM 
    			bird_plus.ch_chatting_reaction ccr
    		INNER JOIN
    			bird_plus.sy_emoticon_duplication_processing sedp 
    		ON
    			ccr.emoticon_id = sedp.id
    		WHERE
    			ccr.chatting_id = :#{[0]}
    		GROUP BY
    			ccr.chatting_id
			""")
	Mono<ChattingReactionResponse> findJoinByChattingId(Long chattingId);
}