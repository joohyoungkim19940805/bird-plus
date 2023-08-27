package com.radcns.bird_plus.repository.room;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;

import reactor.core.publisher.Flux;

public interface RoomFavoritesRepository extends ReactiveCrudRepository<RoomFavoritesEntity, Long>{
	@Query("""
			SELECT
				rria.room_id,
				rr.room_code,
				rr.room_namme,
				rr.is_enabled,
				rr.workspace_id
			FROM
				ro_room_favorites rrf
			INNER JOIN
				ro_room rr
			ON
				rrf.room_id = rr.id
			WHERE
				rrf.account_id = :#{[0]}
			AMD
				rr.workspace_id = :#{[1]}
			ORDER BY
				rrf.order_sort ASC
			OFFSET
				:#{[2].offset}
			LIMIT
				:#{[2].pageSize}
			;
			""")
	Flux<RoomInAccountDomain.MyJoinedRoomListResponse> findAllByAccountIdAndWorkspaceId(Long accountId, Long workspaceId, Pageable pageble);
	
	@Query("""
			SELECT
				count(1)
			FROM
				ro_room_in_account rria
			INNER JOIN
				ro_room rr
			ON
				rrf.room_id = rr.id
			WHERE
				rrf.account_id = :#{[0]}
			AND
				rr.workspace_id = :#{[1]}
			;
			""")
	Flux<Long> countByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);
}
