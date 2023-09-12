package com.radcns.bird_plus.util;

import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain;

import lombok.Getter;
import reactor.core.publisher.Sinks;

public class WorkspaceManager {
	
	@Getter
	private Sinks.Many<ChattingDomain.ChattingResponse> chattingSinks = Sinks.many().multicast().directAllOrNothing();

	public int size() {
		return this.chattingSinks.currentSubscriberCount();
	}
}
