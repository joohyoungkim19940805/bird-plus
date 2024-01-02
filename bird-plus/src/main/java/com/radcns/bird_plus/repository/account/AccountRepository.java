package com.radcns.bird_plus.repository.account;
import com.radcns.bird_plus.entity.account.AccountEntity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {
    Mono<AccountEntity> findByAccountName(String accountName);

    Mono<AccountEntity> findByEmail(String email);

    Mono<AccountEntity> findByAccountNameAndEmail(String accountName, String email);
    
    @Query("""
		SELECT 
    		EXISTS (
				SELECT 1
				FROM 
	    			wo_workspace_in_account wwia 
				INNER JOIN(
					SELECT
    					workspace_id
					FROM
    					wo_workspace_in_account 
					WHERE
    					account_id = :targetAccountId
				) as t
				ON
    		   		t.workspace_id = wwia.workspace_id 
				WHERE
    				wwia.account_id = :myAccountId
			)
	""")
    Mono<Boolean> isWorkspaceJoinCommonAccessUser(Long myAccountId, Long targetAccountId);

}