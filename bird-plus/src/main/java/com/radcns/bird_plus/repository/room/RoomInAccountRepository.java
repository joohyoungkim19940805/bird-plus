package com.radcns.bird_plus.repository.room;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.entity.room.constant.RoomType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomInAccountRepository extends ReactiveCrudRepository<RoomInAccountEntity, Long>{

	@Query("""
			SELECT
				rria.room_id,
				rr.room_code,
				rr.room_name,
				rr.is_enabled,
				rr.workspace_id,
				rr.room_type
			FROM
				ro_room_in_account rria
			INNER JOIN
				ro_room rr
			ON
				rria.room_id = rr.id
			WHERE
				rria.account_id = :#{[0]}
			AND	
				rr.workspace_id = :#{[1]}
			ORDER BY
				rria.order_sort ASC
			OFFSET
				:#{[2].offset}
			LIMIT
				:#{[2].pageSize}
			;
			""")
	Flux<RoomInAccountDomain.MyJoinedRoomListResponse> 
		findAllByAccountIdAndWorkspaceId(Long accountId, Long workspaceId, Pageable pageble);
	
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
			AND
				rr.workspace_id = :#{[1]}
			;
			""")
	Mono<Long> 
		countByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);
	

	@Query("""
			SELECT
				rria.room_id,
				rr.room_code,
				rr.room_name,
				rr.is_enabled,
				rr.workspace_id,
				rr.room_type
			FROM
				ro_room_in_account rria
			INNER JOIN
				ro_room rr
			ON
				rria.room_id = rr.id
			WHERE
				rria.account_id = :#{[0]}
			AND	
				rr.workspace_id = :#{[1]}
			AND	
				(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
			OFFSET
				:#{[3].offset}
			LIMIT
				:#{[3].pageSize}
			;
			""")
	Flux<RoomInAccountDomain.MyJoinedRoomListResponse> 
		findAllByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName, Pageable pageble);
	
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
			AND
				rr.workspace_id = :#{[1]}
			AND	
				(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
			;
			""")
	Mono<Long> 
		countByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName);
	
	//Flux<RoomInAccountEntity>
}
