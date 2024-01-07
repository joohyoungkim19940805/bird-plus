package com.radcns.bird_plus.util.stream;

import com.radcns.bird_plus.entity.account.AccountEntity.AccountDomain.SimpleUpdateAccountInfoEventResponse;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingDeleteResponse;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity.ChattingReactionDomain.ChattingReactionResponse;
import com.radcns.bird_plus.entity.notice.NoticeBoardDetailEntity;
import com.radcns.bird_plus.entity.notice.NoticeBoardEntity;
import com.radcns.bird_plus.entity.notice.NoticeBoardInheritsTable;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain.RoomJoinedAccountResponse;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WokrspaceInAccountPermitListResponse;
import com.radcns.bird_plus.entity.workspace.WorkspaceInAccountEntity.WorkspaceMembersDomain.WorkspaceInAccountPermitRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ServerSentStreamTemplate<T> {
	
	@Getter 
	private final Long workspaceId;
	
	@Getter
	private final Long roomId;
	
	@Getter
	private T content;
	
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
		VOID, 
		ACCOUNT_INFO_CHANGE_ACCEPT,
		
		CHATTING_ACCEPT, 
		CHATTING_REACTION_ACCEPT,
		CHATTING_DELETE_ACCEPT,
		
		ROOM_ACCEPT, 
		ROOM_IN_ACCOUNT_ACCEPT, 
		
		NOTICE_BOARD_ACCEPT, 
		NOTICE_BOARD_DELETE_ACCEPT,
		NOTICE_BOARD_DETAIL_ACCEPT,
		
		WORKSPACE_PERMIT_REQUEST_ACCEPT,
		WORKSPACE_PERMIT_RESULT_ACCEPT
		;
		public final static Class<SimpleUpdateAccountInfoEventResponse> ACCOUNT_INFO_CHANGE_CAST_CLASS = SimpleUpdateAccountInfoEventResponse.class; 
		
		public final static Class<ChattingResponse> CHATTING_ACCEPT_CAST_CLASS = ChattingResponse.class;
		public final static Class<ChattingReactionResponse> CHATTING_REACTION_ACCEPT_CAST_CLASS = ChattingReactionResponse.class;
		public final static Class<ChattingDeleteResponse> CHATTING_DELETE_ACCEPT_CAST_CLASS = ChattingDeleteResponse.class;
		
		public final static Class<RoomInAccountEntity> ROOM_ACCEPT_CAST_CLASS = RoomInAccountEntity.class;
		public final static Class<RoomJoinedAccountResponse> ROOM_IN_ACCOUNT_ACCEPT_CAST_CLASS = RoomJoinedAccountResponse.class;
		
		public final static Class<NoticeBoardInheritsTable> NOTICE_BOARD_ACCEPT_CAST_CLASS = NoticeBoardInheritsTable.class;
		public final static Class<NoticeBoardInheritsTable> NOTICE_BOARD_DELETE_ACCEPT_CAST_CLASS = NoticeBoardInheritsTable.class;
		public final static Class<NoticeBoardDetailEntity> NOTICE_BOARD_DETAIL_ACCEPT_CAST_CLASS = NoticeBoardDetailEntity.class;
		
		public final static Class<WokrspaceInAccountPermitListResponse> WORKSPACE_PERMIT_REQUEST_ACCEPT_CAST_CLASS = WokrspaceInAccountPermitListResponse.class;
		public final static Class<WorkspaceInAccountPermitRequest> WORKSPACE_PERMIT_RESULT_ACCEPT_CAST_CLASS = WorkspaceInAccountPermitRequest.class;
		
	}
}
