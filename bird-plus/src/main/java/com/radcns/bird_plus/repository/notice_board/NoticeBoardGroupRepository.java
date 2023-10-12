package com.radcns.bird_plus.repository.notice_board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardGroupEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity.NoticeBoardDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoticeBoardGroupRepository extends ReactiveCrudRepository<NoticeBoardGroupEntity, Long>{

	/*@Query("""
			SELECT
				nnbg.id,
				nnbg.room_id,
				nnbg.workspace_id,
				nnbg.chatting,
				nnbg.create_at,
				nnbg.update_at,
				nnbg.full_name,
			FROM
				no_notice_board_group nnbg
			WHERE
				nnbg.workspace_id = :#{[0]}
			AND
				nnbg.room_id = :#{[1]}
			AND
				nngb.group_id = :#{[2]}
			ORDER BY
				nnbg.create_at 
			DESC
			OFFSET
				:#{[3].offset}
			LIMIT
				:#{[3].pageSize}
			;
			""")*/
	Flux<NoticeBoardGroupEntity> findAllByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId, Pageable pageble);

	/*@Query("""
			SELECT
				count(1)
			FROM
				no_notice_board_group nnbg
			WHERE
				nnbg.workspace_id = :#{[0]}
			AND
				nnbg.room_id = :#{[1]}
			AND
				nnbg.group_id = :#{[2]}
			;
			""")*/
	Mono<Long> countByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId);
	
	@Query("""
			SELECT
				MAX(nnb.order_sort)
			FROM
				no_notice_board_group nnbg
			WHERE
				nnbg.workspace_id = :#{[0]}
			AND
				nnbg.room_id = :#{[1]}
			AND
				nnbg.parent_group_id = :#{[2]}
			""")
	Mono<Long> findMaxByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId);
}