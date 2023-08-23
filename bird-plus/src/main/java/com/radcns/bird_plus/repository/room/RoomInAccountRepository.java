package com.radcns.bird_plus.repository.room;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;

import reactor.core.publisher.Flux;

public interface RoomInAccountRepository extends ReactiveCrudRepository<RoomInAccountEntity, Long>{


	@Query("""
			SELECT
				rria.room_id,
				rr.room_code,
				rr.room_namme,
				rr.is_enabled,
				rr.workspace_id
			FROM
				ro_room_in_account rria
			INNER JOIN
				ro_room rr
			ON
				rria.room_id = rr.id
			WHERE
				rria.account_id = :#{[0]}
			ORDER BY
				rr.updated_at DESC
			OFFSET
				:#{[1].offset}
			LIMIT
				:#{[1].pageSize}
			;
			""")
	Flux<RoomInAccountDomain.MyJoinedRoomListResponse> findAllByAccountId(Long accountId, Pageable pageble);
	
	@Query("""
			SELECT
				count(1)
			FROM
				ro_room_in_account rria
			INNER JOIN
				ro_room rr
			ON
				rria.room_id = rr.id
			WHERE
				rria.account_id = :#{[0]}
			;
			""")
	Flux<Long> countByAccountId(Long accountId);
	
}
