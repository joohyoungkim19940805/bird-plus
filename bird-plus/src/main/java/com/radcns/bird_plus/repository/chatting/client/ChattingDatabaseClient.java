package com.radcns.bird_plus.repository.chatting.client;

import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@Repository
@RequiredArgsConstructor
public class ChattingDatabaseClient {
    private final DatabaseClient databaseClient;
    public static record SearchChattingRecord(
    	Long workspaceId,
    	Long roomId,
    	Long accountId,
    	Long chattingId,
    	Long startNo,
    	Long endNo,
    	String searchText
    ){}
    public Flux<ChattingDomain.ChattingResponse> searchChatting(SearchChattingRecord searchChattingRecord) {
    	var sql = databaseClient.sql(() -> {
    		String accountIdQuery = """
    				AND
    					cc.account_id = :accountId 
    				""";
    		String chattingIdQuery = """
    				AND
    					cc.id = :chattingId
    				""";
			String select = """
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
			""";
			String from = """
			FROM
				ch_chatting cc
		    INNER JOIN
		    	ac_account aa
			ON
				cc.account_id = aa.id
			""";
			String where = """
			    WHERE
			    	cc.workspace_id = :workspaceId
			    AND
			    	cc.room_id = :roomId
			    AND 
					cc.is_delete = false
			    AND
			    	cc.page_sequence  <= :startNo
			    AND
			    	cc.page_sequence  >= :endNo
			    %s
			    %s
			""".formatted(
				searchChattingRecord.chattingId != null ? accountIdQuery : "",
				searchChattingRecord.accountId != null ? chattingIdQuery : ""
			);
			
    		return select + from + where;
    	})
    	.bind("workspaceId", searchChattingRecord.workspaceId)
    	.bind("roomId", searchChattingRecord.roomId)
    	.bind("startNo", searchChattingRecord.startNo)
    	.bind("endNo", searchChattingRecord.endNo);
    	
    	if(searchChattingRecord.accountId != null) sql.bind("accountId", searchChattingRecord.accountId);
    	if(searchChattingRecord.chattingId != null) sql.bind("chattingId", searchChattingRecord.chattingId);
    	
    	;
    	return sql.map( (row, rowMetaData) -> 
    		ChattingResponse.builder()
    			.id(row.get("id", Long.class))
    			.roomId(row.get("room_id", Long.class))
    			.workspaceId(row.get("workspace_id", Long.class))
    			.fullName(row.get("full_name", String.class))
    			.accountName(row.get("account_name", String.class))
    			.profileImage(row.get("profile_image", String.class))
    			.chatting(row.get("chatting", Json.class))
    			.createAt(row.get("create_at", LocalDateTime.class))
    			.updateAt(row.get("update_at", LocalDateTime.class))
    			.reaction(row.get("reaction", Json.class))
    			.pageSequence(row.get("page_sequence", Long.class))
    		.build()
    	).all();
    }
    public Mono<Long> searchChattingCount(SearchChattingRecord searchChattingRecord) {
    	
    	var sql = databaseClient.sql(() -> {
    		String accountIdQuery = """
    				AND
    					cc.account_id = :accountId 
    				""";
    		String chattingIdQuery = """
    				AND
    					cc.id = :chattingId
    				""";
			String select = """
			SELECT
		    	count(1) as count
			""";
			String from = """
			FROM
				ch_chatting cc
		    INNER JOIN
		    	ac_account aa
			ON
				cc.account_id = aa.id
			""";
			String where = """
			    WHERE
			    	cc.workspace_id = :workspaceId
			    AND
			    	cc.room_id = :roomId
			    AND 
					cc.is_delete = false
			    %s
			    %s
			""".formatted(
				searchChattingRecord.chattingId != null ? accountIdQuery : "",
				searchChattingRecord.accountId != null ? chattingIdQuery : ""
			);
			
    		return select + from + where;
    	})
    	.bind("workspaceId", searchChattingRecord.workspaceId)
    	.bind("roomId", searchChattingRecord.roomId);
    	
    	if(searchChattingRecord.accountId != null) sql.bind("accountId", searchChattingRecord.accountId);
    	if(searchChattingRecord.chattingId != null) sql.bind("chattingId", searchChattingRecord.chattingId);
    	
    	;
    	return sql.map( (row, rowMetaData) -> 
    		row.get("count", Long.class)
    	).one();
    }
}