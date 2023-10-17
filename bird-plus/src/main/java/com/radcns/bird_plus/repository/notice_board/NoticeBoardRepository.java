package com.radcns.bird_plus.repository.notice_board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity.NoticeBoardDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoticeBoardRepository extends ReactiveCrudRepository<NoticeBoardEntity, Long>{

	Flux<NoticeBoardEntity> findAllByWorkspaceIdAndRoomIdAndParentGroupId(Long workspaceId, Long roomId, Long parentGroupId);

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
	
	Flux<NoticeBoardEntity> findAllByWorkspaceIdAndRoomIdAndTitle(Long workspaceId, Long roomId, String title);
	
}