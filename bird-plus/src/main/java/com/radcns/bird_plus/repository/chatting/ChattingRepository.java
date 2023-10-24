package com.radcns.bird_plus.repository.chatting;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ChattingRepository extends ReactiveCrudRepository<ChattingEntity, Long> {
    @Query("""
    SELECT
    	ch.id,
    	ch.room_id,
    	ch.workspace_id,
    	ch.chatting,
    	ch.create_at,
    	ch.update_at,
    	aa.full_name,
    	aa.account_name
    FROM
    	ch_chatting ch
    INNER JOIN
    	ac_account aa
    ON
    	ch.account_id = aa.id
    WHERE
    	ch.workspace_id = :#{[0]}
    AND
    	ch.room_id = :#{[1]}
    ORDER BY
    	ch.create_at
    DESC
    OFFSET
    	:#{[2].offset}
    LIMIT
    	:#{[2].pageSize}
    ;
    """)
    Flux<ChattingDomain.ChattingResponse> findAllJoinAccountByWorkspaceIdAndRoomId(Long workspaceId, Long roomId, Pageable pageble);

    @Query("""
    SELECT
    	count(1)
    FROM
    	ch_chatting ch
    INNER JOIN
    	ac_account aa
    ON
    	ch.account_id = aa.id
    WHERE
    	ch.workspace_id = :#{[0]}
    AND
    	ch.room_id = :#{[1]}
    ;
    """)
    Mono<Long> countJoinAccountByWorkspaceIdAndRoomId(Long workspaceId, Long roomId);
}