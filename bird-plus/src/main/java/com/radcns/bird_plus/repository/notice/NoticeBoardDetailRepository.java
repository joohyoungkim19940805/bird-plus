package com.radcns.bird_plus.repository.notice;
import com.radcns.bird_plus.entity.notice.NoticeBoardDetailEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface NoticeBoardDetailRepository extends ReactiveCrudRepository<NoticeBoardDetailEntity, Long> {
    Flux<NoticeBoardDetailEntity> findAllByNoticeBoardId(Long noticeBoardId);

    @Query("""
       SELECT
       	MAX(nnbd.order_sort)
       FROM
       	no_notice_board_detail nnbd
       WHERE
       	nnbd.workspace_id = :#{[0]}
       AND
       	nnbd.room_id = :#{[1]}
       AND
       	nnbd.notice_board_id = :#{[2]}
    """)
    Mono<Long> findMaxByWorkspaceIdAndRoomIdAndNoticeBoardId(Long workspaceId, Long roomId, Long noticeBoardId);
}