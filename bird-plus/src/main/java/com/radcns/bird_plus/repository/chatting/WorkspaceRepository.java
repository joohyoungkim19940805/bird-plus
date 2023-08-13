package com.radcns.bird_plus.repository.chatting;


import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.chatting.WorkspaceEntity;
import com.radcns.bird_plus.entity.chatting.WorkspaceEntity.SearchWorkspaceListDomain.SearchWorkspaceListResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceRepository extends ReactiveCrudRepository<WorkspaceEntity, Long> {

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
						cwm2.workspace_id = cw.id
				) AS joined_count
			FROM
				bird_plus.ch_workspace cw
			INNER JOIN
				bird_plus.ch_workspace_members cwm
			ON
				cwm.workspace_id = cw.id
			WHERE
				(cw.workspace_name ILIKE concat('%',:#{[0]},'%'))
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
				bird_plus.ch_workspace cw
			INNER JOIN
				bird_plus.ch_workspace_members cwm
			ON
				cwm.workspace_id = cw.id
			WHERE
				(cw.workspace_name ILIKE concat('%',:workspaceName,'%'));
			""")
	Mono<Long> findAllByWorkspaceNameCount(String workspaceName);
	
}
