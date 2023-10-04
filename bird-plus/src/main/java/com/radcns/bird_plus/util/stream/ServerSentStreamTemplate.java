package com.radcns.bird_plus.util.stream;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.data.annotation.Transient;

import lombok.Getter;

public abstract class ServerSentStreamTemplate<T> {
	
	@Getter 
	private final Long workspaceId;
	
	@Getter
	private final Long roomId;
	
	@Getter
	private final T content;
	
	@Getter
	private final ServerSentStreamType serverSentStreamType;
	
	//@Getter
	//@Transient
	//private final Class<?> dataClass;

	public ServerSentStreamTemplate(Long workspaceId, Long roomId, T content, ServerSentStreamType serverSentStreamType) {
		//Type type = this.getClass().getGenericSuperclass();
		//this.dataClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
		
		this.workspaceId = workspaceId;
		this.roomId = roomId;
		this.content = content;
		this.serverSentStreamType = serverSentStreamType;
	}
	
	public enum ServerSentStreamType{
		CHTTING_ACCEPT, ROOM_ACCEPT;

	}
}
