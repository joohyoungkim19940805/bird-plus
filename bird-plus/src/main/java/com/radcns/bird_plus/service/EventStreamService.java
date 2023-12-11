package com.radcns.bird_plus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain.MyJoinedRoomListResponse;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.room.RoomRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import reactor.core.publisher.Mono;

@Component
@Service
public class EventStreamService {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	
	@Autowired
	private WorkspaceInAccountRepository workspaceInAccountRepository;
	
	public Mono<ServerSentStreamTemplate<?>> chattingEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account) {
		return roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), serverSentTemplate.getWorkspaceId(), serverSentTemplate.getRoomId())
		.flatMap(bol ->{
			if(bol) {
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
	
	/*public Mono<ServerSentStreamTemplate<?>> roomInAccountEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account){
		
		return null;
	}*/
	
	public Mono<ServerSentStreamTemplate<?>> roomEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account){
		RoomInAccountEntity roomInAccountEntity = ServerSentStreamType.ROOM_ACCEPT_CAST_CLASS.cast(serverSentTemplate.getContent());
		if( ! roomInAccountEntity.getAccountId().equals(account.getId())) {
			return Mono.empty();
		}
		
		return roomRepository.findById(roomInAccountEntity.getRoomId())
			.map(roomEntity -> {
				return new ServerSentStreamTemplate<MyJoinedRoomListResponse>(
					roomInAccountEntity.getWorkspaceId(),
					roomInAccountEntity.getRoomId(),
					MyJoinedRoomListResponse.builder()
						.id(roomInAccountEntity.getId())
						.roomId(roomInAccountEntity.getRoomId())
						//.roomCode(roomEntity.getRoomCode())
						.roomName(roomEntity.getRoomName())
						.isEnabled(roomEntity.getIsEnabled())
						.workspaceId(roomEntity.getWorkspaceId())
						.orderSort(roomInAccountEntity.getOrderSort())
						.roomType(roomEntity.getRoomType())
					.build(),
					ServerSentStreamType.ROOM_ACCEPT
				) {};
			});
	}
	
	public Mono<ServerSentStreamTemplate<?>> noticeBoardEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account) {
		return roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), serverSentTemplate.getWorkspaceId(), serverSentTemplate.getRoomId())
		.flatMap(bol ->{
			if(bol) {
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
	public Mono<ServerSentStreamTemplate<?>> chattingReactionEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account) {
		return roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), serverSentTemplate.getWorkspaceId(), serverSentTemplate.getRoomId())
		.flatMap(bol ->{
			if(bol) {
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
	public Mono<ServerSentStreamTemplate<?>> workspacePermitRequestStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account){
		return workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsAdmin(serverSentTemplate.getWorkspaceId(), account.getId(), true)
		.flatMap(bol -> {
			if(bol) {
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
}
