package com.radcns.bird_plus.util.stream;

import lombok.Getter;
import reactor.core.publisher.Sinks;

public class WorkspaceManager {
	
	//@Getter
	//private Sinks.Many<ChattingDomain.ChattingResponse> chattingSinks = Sinks.many().multicast().directAllOrNothing();
	
	@Getter
	private Sinks.Many<ServerSentStreamTemplate<? extends Object>> workspaceSinks = Sinks.many().multicast().directAllOrNothing();

	public int size() {
		workspaceSinks.asFlux();
		return this.workspaceSinks.currentSubscriberCount();
	}
}
