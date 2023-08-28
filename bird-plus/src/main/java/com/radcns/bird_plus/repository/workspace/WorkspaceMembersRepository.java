package com.radcns.bird_plus.repository.workspace;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.workspace.WorkspaceMembersEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceMembersEntity.WorkspaceMembersDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceMembersRepository extends ReactiveCrudRepository<WorkspaceMembersEntity, Long> {
	
	Mono<Boolean> existsByAccountId(Long accountId);
	
	Mono<Boolean> existsByWorkspaceIdAndAccountId(Long workspaceId, Long accountId);
	
	@Query("""
			SELECT
				wwm.workspace_id,
				ww.workspace_name,
				ww.is_enabled,
				ww.access_filter,
				ww.is_finally_permit,
				(
					SELECT 
						count(1) 
					FROM 
						wo_workspace_in_account wwia2 
					WHERE 
						wwia2.workspace_id = ww.id
				) AS joined_count
			FROM
				wo_workspace_in_account wwia
			INNER JOIN
				wo_workspace ww
			ON
				wwia.workspace_id = ww.id
			WHERE
				wwia.account_id = :#{[0]}
			ORDER BY
				ww.create_at DESC
			OFFSET
				:#{[1].offset}
			LIMIT
				:#{[1].pageSize}
			;
			""")
	Flux<WorkspaceMembersDomain.MyJoinedWorkspaceListResponse> findAllByAccountId(Long accountId, Pageable pageable);
	
	@Query("""	
			SELECT
				count(1)
			FROM
				wo_workspace_in_account wwia
			INNER JOIN
				wo_workspace ww
			ON
				wwia.workspace_id = ww.id
			WHERE
				wwia.account_id = :accountId;
			""")
	Mono<Long> countByAccountId(Long accountId);
}
