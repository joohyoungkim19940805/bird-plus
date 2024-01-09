package com.radcns.bird_plus.repository.room;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
// Flux<RoomInAccountEntity>
public interface RoomInAccountRepository extends ReactiveCrudRepository<RoomInAccountEntity, Long> {
    Mono<Long> countByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);

    Mono<Long> countByRoomId(Long roomId);
    
    Mono<Boolean> existsByAccountIdAndRoomId(Long accountId, Long roomId);

    Mono<Boolean> existsByAccountIdAndWorkspaceIdAndRoomId(Long accountId, Long workspaceId, Long roomId);

    Flux<RoomInAccountEntity> findAllByRoomId(Long roomId);

    Mono<RoomInAccountEntity> findByRoomIdAndAccountId(Long roomId, Long accountId);

    /* @Query("""
    SELECT
    rria.id,
    rria.room_id,
    rria.order_sort,
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
    rria.order_sort DESC
    OFFSET
    :#{[2].offset}
    LIMIT
    :#{[2].pageSize}
    ;
    """)
    Flux<RoomInAccountDomain.MyJoinedRoomListResponse> 
    findAllJoinRoomByAccountIdAndWorkspaceId(Long accountId, Long workspaceId, Pageable pageble);

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
    countJoinRoomByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);

    @Query("""
    SELECT
    rria.id,
    rria.room_id,
    rria.order_sort,
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
    ORDER BY
    rria.order_sort DESC
    OFFSET
    :#{[3].offset}
    LIMIT
    :#{[3].pageSize}
    ;
    """)
    Flux<RoomInAccountDomain.MyJoinedRoomListResponse> 
    findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName, Pageable pageble);

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
    countJoinRoomByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName);
     */
    @Query("""
    SELECT
    	rria.id,
    	rria.room_id,
    	rria.order_sort,
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
    	rr.room_type IN (:#{[2]})
    ORDER BY
    	rria.order_sort DESC
    OFFSET
    	:#{[3].offset}
    LIMIT
    	:#{[3].pageSize}
    ;
    """)
    Flux<RoomInAccountDomain.MyJoinedRoomListResponse> findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, List<RoomType> roomType, Pageable pageble);

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
    	rr.room_type IN (:#{[2]})
    ;
    """)
    Mono<Long> countJoinRoomByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, List<RoomType> roomType);

    @Query("""
    SELECT
    	max(rria.order_sort)
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
    	rr.room_type IN (:#{[2]})
    ;
    """)
    Mono<Long> findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, List<RoomType> roomType);

    @Query("""
    SELECT
    	rria.id,
    	rria.room_id,
    	rria.order_sort,
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
    	rr.room_type IN (:#{[3]})
    AND
    	(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
    ORDER BY
    	rria.order_sort DESC
    OFFSET
    	:#{[4].offset}
    LIMIT
    	:#{[4].pageSize}
    ;
    """)
    Flux<RoomInAccountDomain.MyJoinedRoomListResponse> findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(Long accountId, Long workspaceId, String workspaceName, List<RoomType> roomType, Pageable pageble);

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
    	rr.room_type IN (:#{[3]})
    AND
    	(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
    ;
    """)
    Mono<Long> countJoinRoomByAccountIdAndWorkspaceIdAndRoomNameAndRoomType(Long accountId, Long workspaceId, String roomName, List<RoomType> roomType);

    @Query("""
	 SELECT 
    	array_to_string(array_agg(aa.full_name), ',')
	 FROM
	 	ro_room_in_account rria
	 INNER JOIN 
    	ac_account aa
	 ON 
    	rria.account_id = aa.id
	 WHERE 
	 	room_id = :#{[0]}
    """)
    Mono<String> findGroupMessengerRoomName(Long roomId);
    
    @Query("""
    	SELECT t.room_id
    	FROM(
    		SELECT
    		  	rria.room_id, 
    		  	count(1) as count
    		FROM
    		  	ro_room_in_account rria
    		INNER JOIN
    		  	ro_room rr
    		ON
    		  	rria.room_id = rr.id
    		AND
    		  	rr.room_type = :roomType
    		AND
    		  	rr.workspace_id = :workspaceId
    		WHERE
    		  	rria.account_id in(:accountList)
    		GROUP BY
    			rria.room_id having count(1) = :count
    	) t
    	WHERE
    		t.count = (
    		  	SELECT
    		  		count(1)
    		  	FROM
    		  		ro_room_in_account rria2
    		  	WHERE
    		  		rria2.room_id = t.room_id
    		)
    	LIMIT 1
    """)
    Mono<Long> findRoomIdWithExistsInviteAccount(RoomType roomType, Long workspaceId, List<Long> accountList, Integer count);
    
}