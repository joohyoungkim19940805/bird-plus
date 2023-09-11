package com.radcns.bird_plus.repository.chatting;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChattingRepository extends ReactiveCrudRepository<ChattingEntity, Long> {
	@Query("""
			SELECT
				ch.workspace_id,
				ch.room_id,
				ch.chatting,
				ch.create_at,
				ch.update_at,
				aa.full_name,
				aa.account_name,
				aa.job_grade,
				aa.department,
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
				ch.create_at DESC
			OFFSET
				:#{[2].offset}
			LIMIT
				:#{[2].pageSize}
			;
			""")
	Flux<ChattingEntity> findAllJoinAccountByWokrpsaceIdAndRoomId(Long workspaceId, Long roomId, Pageable pageble);
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
			ORDER BY
				ch.create_at DESC
			;
			""")
	Mono<Long> countJoinAccountByWokrpsaceIdAndRoomId(Long workspaceId, Long roomId);
}