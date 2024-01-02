package com.radcns.bird_plus.util.stream;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.radcns.bird_plus.entity.chatting.ChattingEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;
import com.radcns.bird_plus.service.MailService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks.EmitResult;

@Component
public class WorkspaceBroker{
	private static final Logger logger = LoggerFactory.getLogger(WorkspaceBroker.class);
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
	public <T> Flux<EmitResult> sendGlobal(ServerSentStreamTemplate<T> serverSentStreamTemplate) {
		return Flux.fromIterable(WorkspaceBroker.manager.entrySet()).flatMap(e->{
			Long workspaceId = e.getKey();
			WorkspaceManager workspaceManager = e.getValue();
			EmitResult result = workspaceManager.getWorkspaceSinks().tryEmitNext(serverSentStreamTemplate);

			if(result.isFailure()) {
				logger.error(
					"""
					==========================================================
					emit failure !! 
					:: Accept Type = %s
					:: Workspace Id = %d
					:: Room Id = %d
					==========================================================
					""".formatted(serverSentStreamTemplate.getServerSentStreamType(), workspaceId, serverSentStreamTemplate.getRoomId())
				);
			}
			return Mono.just(result);
		})
		.delayElements(Duration.ofMillis(200));
		
	}
	public <T> EmitResult send(ServerSentStreamTemplate<T> serverSentStreamTemplate) {
		
		EmitResult result = this.getManager(serverSentStreamTemplate.getWorkspaceId())
			.getWorkspaceSinks()
			.tryEmitNext(serverSentStreamTemplate);
		if (result.isFailure()) {
			logger.error(
				"""
				==========================================================
				emit failure !! 
				:: Accept Type = %s
				:: Workspace Id = %d
				:: Room Id = %d
				==========================================================
				""".formatted(serverSentStreamTemplate.getServerSentStreamType(), serverSentStreamTemplate.getWorkspaceId(), serverSentStreamTemplate.getRoomId())
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
