package com.radcns.bird_plus.repository.room;
import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;
import com.radcns.bird_plus.entity.room.RoomFavoritesEntity.RoomFavoritesDomain;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface RoomFavoritesRepository extends ReactiveCrudRepository<RoomFavoritesEntity, Long> {
    Mono<Long> countByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);

    Mono<Boolean> existsByAccountIdAndRoomId(Long accountId, Long roomId);

    Mono<Void> deleteByAccountIdAndRoomIdAndWorkspaceId(Long accountId, Long roomId, Long workspaceId);

    @Query("""
    SELECT
    	max(rrf.order_sort)
    FROM
    	ro_room_favorites rrf
    WHERE
    	rrf.account_id = :#{[0]}
    AND
    	rrf.workspace_id = :#{[1]}
    """)
    Mono<Long> maxByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);

    @Query("""
    SELECT
    	count(1)
    FROM
    	ro_room_favorites rrf
    INNER JOIN
    	ro_room rr
    ON
    	rrf.room_id = rr.id
    WHERE
    	rrf.account_id = :#{[0]}
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
    	ro_room_favorites rrf
    INNER JOIN
    	ro_room rr
    ON
    	rrf.room_id = rr.id
    WHERE
    	rrf.account_id = :#{[0]}
    AND
    	rr.workspace_id = :#{[1]}
    AND
    	rr.room_type IN (:#{[2]})
    ;
    """)
    Mono<Long> findMaxJoinRoomByAccountIdAndWorkspaceIdAndRoomType(Long accountId, Long workspaceId, List<RoomType> roomType);

    @Query("""
    SELECT
    	rrf.id,
    	rrf.room_id,
    	rrf.order_sort,
    	rr.room_code,
    	rr.room_name,
    	rr.is_enabled,
    	rr.workspace_id,
    	rr.room_type
    FROM
    	ro_room_favorites rrf
    INNER JOIN
    	ro_room rr
    ON
    	rrf.room_id = rr.id
    WHERE
    	rrf.account_id = :#{[0]}
    AND
    	rr.workspace_id = :#{[1]}
    ORDER BY
    	rrf.order_sort DESC
    OFFSET
    	:#{[2].offset}
    LIMIT
    	:#{[2].pageSize}
    ;
    """)
    Flux<RoomFavoritesDomain.MyFavoritesRoomListResponse> findAllJoinRoomByAccountIdAndWorkspaceId(Long accountId, Long workspaceId, Pageable pageble);

    @Query("""
    SELECT
    	count(1)
    FROM
    	ro_room_favorites rrf
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
    Mono<Long> countJoinRoomByAccountIdAndWorkspaceId(Long accountId, Long workspaceId);

    @Query("""
    SELECT
    	rrf.id,
    	rrf.room_id,
    	rrf.order_sort,
    	rr.room_code,
    	rr.room_name,
    	rr.is_enabled,
    	rr.workspace_id,
    	rr.room_type
    FROM
    	ro_room_favorites rrf
    INNER JOIN
    	ro_room rr
    ON
    	rrf.room_id = rr.id
    WHERE
    	rrf.account_id = :#{[0]}
    AND
    	rr.workspace_id = :#{[1]}
    AND
    	(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
    ORDER BY
    	rrf.order_sort DESC
    OFFSET
    	:#{[3].offset}
    LIMIT
    	:#{[3].pageSize}
    ;
    """)
    Flux<RoomFavoritesDomain.MyFavoritesRoomListResponse> findAllJoinRoomByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName, Pageable pageble);

    @Query("""
    SELECT
    	count(1)
    FROM
    	ro_room_favorites rrf
    INNER JOIN
    	ro_room rr
    ON
    	rrf.room_id = rr.id
    WHERE
    	rrf.account_id = :#{[0]}
    AND
    	rr.workspace_id = :#{[1]}
    AND
    	(rr.room_name ILIKE concat('%', :#{[2]}, '%'))
    ;
    """)
    Mono<Long> countJoinRoomByAccountIdAndWorkspaceIdAndRoomName(Long accountId, Long workspaceId, String roomName);
}