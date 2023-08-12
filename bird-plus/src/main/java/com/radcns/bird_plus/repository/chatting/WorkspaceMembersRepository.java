package com.radcns.bird_plus.repository.chatting;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.chatting.WorkspaceMembersEntity;
import com.radcns.bird_plus.entity.chatting.WorkspaceMembersEntity.MyJoinedWorkspaceListResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceMembersRepository extends ReactiveCrudRepository<WorkspaceMembersEntity, Long> {
	
	Mono<Boolean> existsByAccountId(Long accountId);
	
	@Query("""
			SELECT
				workspace_id,
				workspace_name,
				is_enabled,
				access_filter,
				is_finally_permit,
				(
					SELECT 
						count(1) 
					FROM 
						bird_plus.ch_workspace_members cwm2 
					WHERE 
						cwm2.workspace_id = cwm.workspace_id
				) AS joined_count
			FROM
				bird_plus.ch_workspace_members cwm
			INNER JOIN
				bird_plus.ch_workspace cw
			ON
				cwm.workspace_id = cw.id
			WHERE
				cwm.account_id = :#{[0]}
			OFFSET
				:#{[1].offset}
			LIMIT
				:#{[1].pageSize};
			""")
	Flux<MyJoinedWorkspaceListResponse> findAllByAccountId(Long accountId, Pageable pageable);
	
	@Query("""	
			SELECT
				count(1)
			FROM
				bird_plus.ch_workspace_members cwm
			INNER JOIN
				bird_plus.ch_workspace cw
			ON
				cwm.workspace_id = cw.id
			WHERE
				cwm.account_id = :accountId;
			""")
	Mono<Long> findAllByAccountIdCount(Long accountId);
}
