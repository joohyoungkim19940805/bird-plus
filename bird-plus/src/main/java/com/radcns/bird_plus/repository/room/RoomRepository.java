package com.radcns.bird_plus.repository.room;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface RoomRepository extends ReactiveCrudRepository<RoomEntity, Long> {
    Mono<Boolean> existsByCreateByAndWorkspaceIdAndRoomType(Long createBy, Long workspaceId, RoomType roomType);
    
    Mono<Boolean> existsByCreateByAndId(Long createBy, Long id);
    
    Mono<RoomEntity> findByCreateByAndWorkspaceIdAndRoomType(Long createBy, Long workspaceId, RoomType roonType);
    
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
    Flux<RoomEntity> findAllJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, RoomType roomType, PageRequest pageReuqest);

    @Query("""
    SELECT
    	count(1)
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
    Mono<Long> countJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, RoomType roomType);

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
    Flux<RoomEntity> findAllJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(Long accountId, Long workspaceId, String roomName, RoomType roomType, PageRequest pageReuqest);

    @Query("""
    SELECT
    	count(1)
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
    Mono<Long> countJoinWorkspaceByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(Long accountId, Long workspaceId, String roomName, RoomType roomType);
}