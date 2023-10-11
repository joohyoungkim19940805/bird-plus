package com.radcns.bird_plus.util.stream;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.data.annotation.Transient;

import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;
import com.radcns.bird_plus.entity.notice_board.NoticeBoardEntity;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain.RoomJoinedAccountResponse;

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
		CHTTING_ACCEPT, ROOM_ACCEPT, ROOM_IN_ACCOUNT_ACCEPT, NOTICE_BOARD_ACCEPT;
		public final static Class<ChattingResponse> CHTTING_ACCEPT_CAST_CLASS = ChattingResponse.class;
		public final static Class<RoomInAccountEntity> ROOM_ACCEPT_CAST_CLASS = RoomInAccountEntity.class;
		public final static Class<RoomJoinedAccountResponse> ROOM_IN_ACCOUNT_ACCEPT_CAST_CLASS = RoomJoinedAccountResponse.class;
		public final static Class<NoticeBoardEntity> NOTICE_BOARD_ACCEPT_CAST_CLASS = NoticeBoardEntity.class;
	}
}
