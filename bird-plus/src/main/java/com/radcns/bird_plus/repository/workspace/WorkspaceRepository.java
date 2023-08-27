package com.radcns.bird_plus.repository.workspace;


import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.workspace.WorkspaceEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceEntity.WorkspaceDomain.SearchWorkspaceListResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceRepository extends ReactiveCrudRepository<WorkspaceEntity, Long> {

	@Query("""
			SELECT
				cw.id,
				cw.workspace_name,
				cw.is_enabled,
				cw.access_filter,
				cw.is_finally_permit,
				(
					SELECT
						count(1)
					FROM
						wo_workspace_members wwm 
					WHERE
						wwm.workspace_id = ww.id
				) AS joined_count
			FROM
				wo_workspace ww
			WHERE
				ww.is_private != true
			AND
				(ww.workspace_name ILIKE concat('%',:#{[0]},'%'))
			OFFSET
				:#{[1].offset}
			LIMIT
				:#{[1].pageSize}
			;
			""")
	Flux<SearchWorkspaceListResponse> findAllByWorkspaceName(String workspaceName, Pageable pageble);
	
	@Query("""
			SELECT
				count(1)
			FROM
				wo_workspace ww
			WHERE
				(ww.workspace_name ILIKE concat('%',:workspaceName,'%'));
			""")
	Mono<Long> countByWorkspaceName(String workspaceName);
	
}
