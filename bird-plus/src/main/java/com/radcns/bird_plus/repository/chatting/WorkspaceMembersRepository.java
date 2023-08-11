package com.radcns.bird_plus.repository.chatting;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.chatting.WorkspaceMembersEntity;

public interface WorkspaceMembersRepository extends ReactiveCrudRepository<WorkspaceMembersEntity, Long> {
	
}
