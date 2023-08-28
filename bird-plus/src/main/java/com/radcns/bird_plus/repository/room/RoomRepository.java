package com.radcns.bird_plus.repository.room;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.radcns.bird_plus.entity.room.RoomEntity;

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
		findAllByAccountIdAndWorkspaceId(Long accountId, Long workspaceId, PageRequest pageReuqest);
	
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
		countByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);
	
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
		findAllByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName, PageRequest pageReuqest);
	
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
		countByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName);
	
}
