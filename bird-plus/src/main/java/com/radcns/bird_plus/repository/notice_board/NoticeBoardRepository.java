package com.radcns.bird_plus.repository.notice_board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity.NoticeBoardDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoticeBoardRepository extends ReactiveCrudRepository<NoticeBoardEntity, Long>{

	@Query("""
			SELECT
				nnb.id,
				nnb.room_id,
				nnb.workspace_id,
				nnb.chatting,
				nnb.create_at,
				nnb.update_at,
				aa.full_name,
				aa.account_name
			FROM
				no_notice_board nnb
			INNER JOIN
				ac_account aa
			ON
				nnb.account_id = aa.id
			WHERE
				nnb.workspace_id = :#{[0]}
			AND
				nnb.room_id = :#{[1]}
			ORDER BY
				nnb.create_at 
			DESC
			OFFSET
				:#{[2].offset}
			LIMIT
				:#{[2].pageSize}
			;
			""")
	Flux<NoticeBoardDomain.NoticeBoardResponse> findAllJoinAccountByWokrpsaceIdAndRoomId(Long workspaceId, Long roomId, Pageable pageble);
	@Query("""
			SELECT
				count(1)
			FROM
				no_notice_board nnb
			INNER JOIN
				ac_account aa
			ON
				nnb.account_id = aa.id
			WHERE
				nnb.workspace_id = :#{[0]}
			AND
				nnb.room_id = :#{[1]}
			;
			""")
	Mono<Long> countJoinAccountByWokrpsaceIdAndRoomId(Long workspaceId, Long roomId);
}