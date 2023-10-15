package com.radcns.bird_plus.web.handler;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.service.EventStreamService;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import reactor.core.publisher.Mono;

@Component
public class EventStreamHandler {
	@Autowired
	private WorkspaceBroker workspaceBorker;
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private EventStreamService eventStreamService;
	
	public Mono<ServerResponse> emissionStream(ServerRequest request) {
		Long workspaceId = Long.valueOf(request.pathVariable("workspaceId"));
		
		return ok().contentType(MediaType.TEXT_EVENT_STREAM)
			.body(
				workspaceBorker.getManager(workspaceId).getWorkspaceSinks().asFlux()
				//.filter(serverSentTemplate -> serverSentTemplate.getWorkspaceId().equals(workspaceId) && serverSentTemplate.getServerSentStreamType().equals(ServerSentStreamType.CHTTING_ACCEPT))
				.flatMap(serverSentTemplate -> {
					return accountService.convertJwtToAccount(request)
						.flatMap(account -> {
							if(serverSentTemplate.getServerSentStreamType().equals(ServerSentStreamType.CHTTING_ACCEPT)) {
								return eventStreamService.chattingEmissionStream(serverSentTemplate, account);	
							}else if(serverSentTemplate.getServerSentStreamType().equals(ServerSentStreamType.ROOM_ACCEPT)) {
								return eventStreamService.roomEmissionStream(serverSentTemplate, account);
							}else if(serverSentTemplate.getServerSentStreamType().equals(ServerSentStreamType.ROOM_IN_ACCOUNT_ACCEPT)){
								return Mono.just(serverSentTemplate);
							}else if(serverSentTemplate.getServerSentStreamType().equals(ServerSentStreamType.NOTICE_BOARD_ACCEPT) ) {
								return eventStreamService.noticeBoardEmissionStream(serverSentTemplate, account);	
							}
							return Mono.empty();
						});
				})
			, new ParameterizedTypeReference<ServerSentStreamTemplate<?>>() {}
			)
			;
	}
	
}
