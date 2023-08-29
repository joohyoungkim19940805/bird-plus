package com.radcns.bird_plus.repository.room;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.constant.RoomType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomRepository extends ReactiveCrudRepository<RoomEntity,Long>{

	@Query("""
			SELECT
				*
			FROM
				ro_room rr 
			INNER JOIN
				wo_workspace ww 
			ON
				rr.workspace_id = ww.id 
			WHERE 
				rr.workspace_id = :#{[1]}
			AND 
				rr.room_type = :#{[2]}
			AND
				EXISTS (
					SELECT
						1
					FROM
						wo_workspace_in_account wwia
					WHERE 
						wwia.account_id = :#{[0]}
				)
			OFFSET
				:#{[3].offset}
			LIMIT
				:#{[3].pageSize}
			;
			""")
	Flux<RoomEntity>
		findAllByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, RoomType roomType, PageRequest pageReuqest);
	
	@Query("""
			SELECT
				*
			FROM
				ro_room rr 
			INNER JOIN
				wo_workspace ww 
			ON
				rr.workspace_id = ww.id 
			WHERE 
				rr.workspace_id = :#{[1]}
			AND 
				rr.room_type = :#{[2]}
			AND
				EXISTS (
					SELECT
						1
					FROM
						wo_workspace_in_account wwia
					WHERE 
						wwia.account_id = :#{[0]}
				)
			""")
	Mono<Long>
		countByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, RoomType roomType);
	
	@Query("""
			SELECT
				*
			FROM
				ro_room rr 
			INNER JOIN
				wo_workspace ww 
			ON
				rr.workspace_id = ww.id 
			WHERE 
				rr.workspace_id = :#{[1]}
			AND 
				rr.room_type = :#{[3]}
			AND
				EXISTS (
					SELECT
						1
					FROM
						wo_workspace_in_account wwia
					WHERE 
						wwia.account_id = :#{[0]}
				)
			AND	
				(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
			OFFSET
				:#{[4].offset}
			LIMIT
				:#{[4].pageSize}
			;
			""")
	Flux<RoomEntity>
		findAllByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(Long accountId, Long workspaceId, String roomName, RoomType roomType, PageRequest pageReuqest);
	
	@Query("""
			SELECT
				*
			FROM
				ro_room rr 
			INNER JOIN
				wo_workspace ww 
			ON
				rr.workspace_id = ww.id 
			WHERE 
				rr.workspace_id = :#{[1]}
			AND 
				rr.room_type = :#{[3]}
			AND
				EXISTS (
					SELECT
						1
					FROM
						wo_workspace_in_account wwia
					WHERE 
						wwia.account_id = :#{[0]}
				)
			AND	
				(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
			""")
	Mono<Long>
		countByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(Long accountId, Long workspaceId, String roomName, RoomType roomType);
	
}
