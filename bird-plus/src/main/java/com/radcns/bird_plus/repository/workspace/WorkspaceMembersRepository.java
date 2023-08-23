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
						wo_workspace_in_account wwm2 
					WHERE 
						wwm2.workspace_id = cwm.workspace_id
				) AS joined_count
			FROM
				wo_workspace_in_account wwm
			INNER JOIN
				wo_workspace cw
			ON
				wwm.workspace_id = cw.id
			WHERE
				wwm.account_id = :#{[0]}
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
				wo_workspace_in_account wwm
			INNER JOIN
				wo_workspace ww
			ON
				wwm.workspace_id = ww.id
			WHERE
				wwm.account_id = :accountId;
			""")
	Mono<Long> countByAccountId(Long accountId);
}
