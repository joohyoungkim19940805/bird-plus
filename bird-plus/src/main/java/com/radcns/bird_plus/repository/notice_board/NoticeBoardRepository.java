package com.radcns.bird_plus.repository.notice_board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity.NoticeBoardDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoticeBoardRepository extends ReactiveCrudRepository<NoticeBoardEntity, Long>{

	/*@Query("""
			SELECT
				nnb.id,
				nnb.room_id,
				nnb.workspace_id,
				nnb.chatting,
				nnb.create_at,
				nnb.update_at,
				nnb.full_name,
			FROM
				no_notice_board nnb
			WHERE
				nnb.workspace_id = :#{[0]}
			AND
				nnb.room_id = :#{[1]}
			AND
				nnb.group_id = :#{[2]}
			ORDER BY
				nnb.create_at 
			DESC
			OFFSET
				:#{[3].offset}
			LIMIT
				:#{[3].pageSize}
			;
			""")*/
	Flux<NoticeBoardEntity> findAllByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId, Pageable pageble);
	
	/*@Query("""
			SELECT
				count(1)
			FROM
				no_notice_board nnb
			WHERE
				nnb.workspace_id = :#{[0]}
			AND
				nnb.room_id = :#{[1]}
			AND
				nnb.group_id = :#{[2]}
			;
			""")*/
	Mono<Long> countByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId);
	
	@Query("""
			SELECT
				MAX(nnb.order_sort)
			FROM
				no_notice_board nnb
			WHERE
				nnb.workspace_id = :#{[0]}
			AND
				nnb.room_id = :#{[1]}
			AND
				nnb.parent_group_id = :#{[2]}
			""")
	Mono<Long> findMaxByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId);
}