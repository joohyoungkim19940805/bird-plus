package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ChattingRepository extends ReactiveCrudRepository<ChattingEntity, Long> {
    /*
	'createAt', ccr.create_at,
	'reactionList', (
		SELECT
			json_agg( json_build_object( 
				'fullName', aa2.full_name 
			) )
		FROM 
			ch_chatting_reaction_count ccrc 
		INNER JOIN
			ac_account aa2
		ON
			ccrc.account_id = aa2.id 
		WHERE 
			ccrc.reaction_id = ccr.id
	)
    */
	@Query("""
    SELECT
    	cc.id,
    	cc.room_id,
    	cc.workspace_id,
    	cc.chatting,
    	cc.create_at,
    	cc.update_at,
    	cc.page_sequence,
    	aa.full_name,
    	aa.account_name,
    	(
    		SELECT 
    			json_agg(json_build_object(
    				'emoticon', sedp.emoticon,
    				'emoticonType', sedp.emoticon_type,
    				'description', sedp.description,
    				'groupTitle', sedp.group_title ,
    				'subgroupTitle', sedp.subgroup_title,
    				'reactionId', ccr.id,
    				'createAt', ccr.create_at,
    				'reactionList', (
						SELECT
							json_agg( json_build_object( 
								'fullName', aa2.full_name 
							) )
						FROM 
							ch_chatting_reaction_count ccrc 
						INNER JOIN
							ac_account aa2
						ON
							ccrc.account_id = aa2.id 
						WHERE 
							ccrc.reaction_id = ccr.id
					)
    			))
    		FROM 
    			ch_chatting_reaction ccr
    		INNER JOIN
    			sy_emoticon_duplication_processing sedp 
    		ON
    			ccr.emoticon_id = sedp.id
    		WHERE
    			ccr.chatting_id = cc.id
    		GROUP BY
    			ccr.chatting_id
    	) AS reaction
    FROM
    	ch_chatting cc
    INNER JOIN
    	ac_account aa
    ON
    	cc.account_id = aa.id
    WHERE
    	cc.workspace_id = :#{[0]}
    AND
    	cc.room_id = :#{[1]}
    AND
    	cc.page_sequence  <= :#{[3]}
    AND
    	cc.page_sequence  >= :#{[4]}
    ORDER BY
    	cc.create_at
    DESC
    ;
    """)
    /*OFFSET
	:#{[2].offset}
	LIMIT
	:#{[2].pageSize}*/
    Flux<ChattingDomain.ChattingResponse> findAllJoinAccountByWorkspaceIdAndRoomId(Long workspaceId, Long roomId, Long accountId, Long startNo, Long endNo);

    /*@Query("""
	    SELECT
	    	count(1)
	    FROM
	    	cc_chatting cc
	    INNER JOIN
	    	ac_account aa
	    ON
	    	cc.account_id = aa.id
	    WHERE
	    	cc.workspace_id = :#{[0]}
		AND
    		cc.room_id = :#{[1]}
    ;
	""")
    */
    Mono<Long> countByWorkspaceIdAndRoomId(Long workspaceId, Long roomId);
    
    @Query("""
    SELECT
    	MAX(cc.page_sequence)
    FROM
    	ch_chatting cc
    WHERE
    	cc.workspace_id = :#{[0]}
    AND	
    	cc.room_id = :#{[1]}
    AND
    	cc.is_delete = false
    """)
    
    Mono<Long> findMaxByWorkspaceIdAndRoomId(Long workspaceId, Long roomId);
	
    @Query("""
	 SELECT
    	cc.id,
    	cc.room_id,
    	cc.workspace_id,
    	cc.chatting,
    	cc.create_at,
    	cc.update_at,
    	cc.page_sequence,
    	aa.full_name,
    	aa.account_name,
    	aa.profile_image,
    	(
    		SELECT 
    			json_agg(json_build_object(
    				'emoticon', sedp.emoticon,
    				'emoticonType', sedp.emoticon_type,
    				'description', sedp.description,
    				'groupTitle', sedp.group_title ,
    				'subgroupTitle', sedp.subgroup_title,
    				'reactionId', ccr.id,
    				'createAt', ccr.create_at,
    				'reactionList', (
						SELECT
							json_agg( json_build_object( 
								'fullName', aa2.full_name 
							) )
						FROM 
							ch_chatting_reaction_count ccrc 
						INNER JOIN
							ac_account aa2
						ON
							ccrc.account_id = aa2.id 
						WHERE 
							ccrc.reaction_id = ccr.id
					)
    			))
    		FROM 
    			ch_chatting_reaction ccr
    		INNER JOIN
    			sy_emoticon_duplication_processing sedp 
    		ON
    			ccr.emoticon_id = sedp.id
    		WHERE
    			ccr.chatting_id = cc.id
    		GROUP BY
    			ccr.chatting_id
    	) AS reaction
    FROM
    	ch_chatting cc
    INNER JOIN
    	ac_account aa
    ON
    	cc.account_id = aa.id
    WHERE
    	cc.workspace_id = :#{[0]}
    AND
    	cc.room_id = :#{[1]}
    AND 
		cc.id = :#{[2]}
    ;
    """)
	Mono<ChattingDomain.ChattingResponse> findIdWithChattingResponse(Long workspaceId, Long roomId, Long chattingId);
}