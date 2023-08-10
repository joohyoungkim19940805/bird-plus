package com.radcns.bird_plus.repository.chatting;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.radcns.bird_plus.entity.chatting.WorkspaceEntity;


public interface WorkspaceRepository extends ReactiveCrudRepository<WorkspaceEntity, Long> {

}
