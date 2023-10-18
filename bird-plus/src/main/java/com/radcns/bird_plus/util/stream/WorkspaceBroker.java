package com.radcns.bird_plus.util.stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks.EmitResult;

@Component
public class WorkspaceBroker{
	protected static volatile ConcurrentMap<Long, WorkspaceManager> manager = new ConcurrentHashMap<>();	

	public int size() {
		return WorkspaceBroker.manager.size();
	}
	
	public int workspaceSize(Long workspaceId) {
		if( ! WorkspaceBroker.manager.containsKey(workspaceId)) {
			return 0;
		}
		return WorkspaceBroker.manager.get(workspaceId).size();
	}
	/*
	public EmitResult sendChatting(ChattingDomain.ChattingResponse chattingEntity) {
		
		EmitResult result = this.getManager(chattingEntity.getWorkspaceId())
			.getChattingSinks()
			.tryEmitNext(chattingEntity);
		if (result.isFailure()) {
			// do something here, since emission failed
		}
		return result;
	}
	*/
	public <T> EmitResult send(ServerSentStreamTemplate<T> serverSentStreamTemplate) {
		
		EmitResult result = this.getManager(serverSentStreamTemplate.getWorkspaceId())
			.getWorkspaceSinks()
			.tryEmitNext(serverSentStreamTemplate);
		if (result.isFailure()) {
			System.out.println(
				"""
				==========================================================
				emit failure !!
				==========================================================
				"""
			);
			
			// do something here, since emission failed
		}
		return result;
	}
	public WorkspaceManager getManager(Long workspaceId){
		if( ! WorkspaceBroker.manager.containsKey(workspaceId)) {
			var workspaceManager = new WorkspaceManager();
			WorkspaceBroker.manager.put(workspaceId, workspaceManager);
			return workspaceManager;
		}
		return WorkspaceBroker.manager.get(workspaceId);
	}
	
}
