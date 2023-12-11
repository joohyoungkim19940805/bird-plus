package com.radcns.bird_plus.repository.workspace;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WokrspaceInAccountPermitListResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface WorkspaceInAccountRepository extends ReactiveCrudRepository<WorkspaceInAccountEntity, Long> {
    Mono<Boolean> existsByAccountId(Long accountId);
    Mono<Boolean> existsByAccountIdAndIsEnabled(Long accountId, Boolean isEnabled);

    Mono<Boolean> existsByWorkspaceIdAndAccountId(Long workspaceId, Long accountId);
    Mono<Boolean> existsByWorkspaceIdAndAccountIdAndIsEnabled(Long workspaceId, Long accountId, Boolean isEnabled);

    Mono<Boolean> existsByWorkspaceIdAndAccountIdAndIsAdmin(Long workspaceId, Long accountId, Boolean isAdmin);
    
    @Query("""
    SELECT
    	wwia.workspace_id,
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
    AND
    	wwia.is_enabled = true
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
    	wwia.account_id = :accountId
    AND 
    	wwia.is_enabled = true
    """)
    Mono<Long> countByAccountId(Long accountId);

    @Query("""
    SELECT
    	wwia.workspace_id,
    	wwia.id,
    	wwia.is_admin,
    	aa.account_name,
    	aa.full_name,
    	aa.job_grade,
    	aa.department
    FROM
    	wo_workspace_in_account wwia
    INNER JOIN
    	ac_account aa
    ON
    	wwia.account_id = aa.id
    AND
    	aa.is_delete = FALSE
    WHERE
    	wwia.workspace_id = :#{[0]}
    AND
    	wwia.account_id != :#{[2]}
    AND NOT EXISTS(
    	SELECT 
    		1
    	FROM
    		ro_room_in_account rria 
    	WHERE 
    		room_id = 
    				CASE 
    					WHEN :#{[1]} = 0 then null
						ELSE :#{[1]}
					END
    	AND
    		aa.id = rria.account_id
    )
    OFFSET
    	:#{[3].offset}
    LIMIT
    	:#{[3].pageSize}
    ;
    """)
    Flux<WorkspaceMembersDomain.WorkspaceInAccountListResponse> findAllJoinAccountByWorkspaceIdAndNotAccountId(
    Long workspaceId, Long roomId, Long accountId, Pageable pageble);

    @Query("""
    SELECT
    	count(1)
    FROM
    	wo_workspace_in_account wwia
    INNER JOIN
    	ac_account aa
    ON
    	wwia.account_id = aa.id
    AND
    	aa.is_delete = FALSE
    WHERE
    	wwia.workspace_id = :#{[0]}
    AND
    	wwia.account_id != :#{[2]}
    AND NOT EXISTS(
    	SELECT 
    		1
    	FROM
    		ro_room_in_account rria 
    	WHERE 
    		room_id = 
    				CASE 
    					WHEN :#{[1]} = 0 then null
						ELSE :#{[1]}
					END
    	AND
    		aa.id = rria.account_id
    )
    ;
    """)
    Mono<Long> countJoinAccountByWorkspaceIdAndNotAccountId(Long workpsaceId, Long roomId, Long accountId);

    @Query("""
    SELECT
    	wwia.workspace_id,
    	wwia.id,
    	wwia.is_admin,
    	aa.account_name,
    	aa.full_name,
    	aa.job_grade,
    	aa.department
    FROM
    	wo_workspace_in_account wwia
    INNER JOIN
    	ac_account aa
    ON
    	wwia.account_id = aa.id
    AND
    	aa.is_delete = FALSE
    WHERE
    	wwia.workspace_id = :#{[0]}
    AND
    	wwia.account_id != :#{[2]}
    AND NOT EXISTS(
    	SELECT 
    		1
    	FROM
    		ro_room_in_account rria 
    	WHERE 
    		room_id = 
    				CASE 
    					WHEN :#{[1]} = 0 then null
						ELSE :#{[1]}
					END
    	AND
    		aa.id = rria.account_id
    )
    AND
    	(aa.full_name ILIKE concat('%', :#{[3]}, '%'))
    OFFSET
    	:#{[4].offset}
    LIMIT
    	:#{[4].pageSize}
    ;
    """)
    Flux<WorkspaceMembersDomain.WorkspaceInAccountListResponse> findAllJoinAccountByWorkspaceIdAndNotAccountIdAndFullName(// like 문 추가 할 예정
    Long workspaceId, Long roomId, Long accountId, String fullName, Pageable pageble);

    @Query("""
    SELECT
    	count(1)
    FROM
    	wo_workspace_in_account wwia
    INNER JOIN
    	ac_account aa
    ON
    	wwia.account_id = aa.id
    AND
    	aa.is_delete = FALSE
    WHERE
    	wwia.workspace_id = :#{[0]}
    AND
    	wwia.account_id != :#{[2]}
    AND NOT EXISTS(
    	SELECT 
    		1
    	FROM
    		ro_room_in_account rria 
    	WHERE 
    		room_id = 
    				CASE 
    					WHEN :#{[1]} = 0 then null
						ELSE :#{[1]}
					END
    	AND
    		aa.id = rria.account_id
    )
    AND
    	(aa.full_name ILIKE concat('%', :#{[3]}, '%'))
    ;
    """)
    Mono<Long> countJoinAccountByWorkspaceIdAndNotAccountIdAndFullName(
    		Long workpsaceId, Long roomId, Long accountId, String fullName);
    
    
    @Query("""
    	SELECT 
    		wwia.id,
    		wwia.workspace_id,
    		aa.account_name,
    		aa.email,
    		aa.full_name,
    		aa.job_grade,
    		aa.department
    	FROM
    		wo_workspace_in_account wwia
    	INNER JOIN
    		ac_account aa
    	ON 
    		wwia.account_id = aa.id
    	WHERE
    		wwia.workspace_id = :#{[0]}
    	AND
    		wwia.is_enabled = false
    	;
    		""")
    Flux<WokrspaceInAccountPermitListResponse> findAllPermitList(Long workspaceId);

    
}