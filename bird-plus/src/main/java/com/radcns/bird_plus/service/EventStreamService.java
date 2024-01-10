package com.radcns.bird_plus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.account.AccountEntity.AccountDomain.SimpleUpdateAccountInfoEventResponse;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingDeleteResponse;
import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomFavoritesEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity.RoomInAccountDomain.MyJoinedRoomListResponse;
import com.radcns.bird_plus.entity.room.constant.RoomType;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.repository.room.RoomFavoritesRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.room.RoomRepository;
import com.radcns.bird_plus.repository.workspace.WorkspaceInAccountRepository;
import com.radcns.bird_plus.util.exception.AccountException;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
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
	private RoomFavoritesRepository roomFavoritesRepository;
	
	@Autowired
	private WorkspaceInAccountRepository workspaceInAccountRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
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
		RoomEntity roomEntity = ServerSentStreamType.ROOM_ACCEPT_CAST_CLASS.cast(serverSentTemplate.getContent());
		List<RoomType> roomType;
		if(roomEntity.getRoomType().equals(RoomType.ROOM_PUBLIC) || roomEntity.getRoomType().equals(RoomType.ROOM_PRIVATE)) {
			roomType = List.of(RoomType.ROOM_PRIVATE, RoomType.ROOM_PUBLIC);
		}else{//(e.getRoomType().equals(RoomType.MESSENGER) || e.getRoomType().equals(RoomType.SELF)) {
			roomType = List.of(RoomType.MESSENGER, RoomType.SELF);
		}
		
		return roomInAccountRepository.existsByAccountIdAndRoomId(account.getId(), roomEntity.getId())
		.flatMap(bol -> {
			if(bol) {
				return roomInAccountRepository.findByRoomIdAndAccountId(roomEntity.getId(), account.getId());
			}else {
				return Mono.empty();
			}
		})
		.flatMap(e->{
			if( ! e.getAccountId().equals(account.getId())) {
				return Mono.empty();
			}
			var newServerSentStreamTemplate = new ServerSentStreamTemplate<MyJoinedRoomListResponse>(
					e.getWorkspaceId(),
					e.getRoomId(),
					MyJoinedRoomListResponse.builder()
						.id(e.getId())
						.roomId(e.getRoomId())
						//.roomCode(roomEntity.getRoomCode())
						.roomName(roomEntity.getRoomName())
						.isEnabled(roomEntity.getIsEnabled())
						.workspaceId(roomEntity.getWorkspaceId())
						.orderSort(e.getOrderSort())
						.roomType(roomEntity.getRoomType()) 
					.build(),
					ServerSentStreamType.ROOM_ACCEPT
				) {};
			
			return roomFavoritesRepository.findByRoomIdAndAccountId(roomEntity.getId(), account.getId())
			.defaultIfEmpty(RoomFavoritesEntity.builder().build())
			.map(roomFavoritesEntity->{
				newServerSentStreamTemplate.getContent().setFavoritesOrderSort(roomFavoritesEntity.getOrderSort());
				return newServerSentStreamTemplate;
			});
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
	public Mono<ServerSentStreamTemplate<?>> workspacePermitRequestEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account){
		return workspaceInAccountRepository.existsByWorkspaceIdAndAccountIdAndIsAdmin(serverSentTemplate.getWorkspaceId(), account.getId(), true)
		.flatMap(bol -> {
			if(bol) {
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
	public Mono<ServerSentStreamTemplate<?>> chattingDeleteEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account){
		return roomInAccountRepository.existsByAccountIdAndWorkspaceIdAndRoomId(account.getId(), serverSentTemplate.getWorkspaceId(), serverSentTemplate.getRoomId())
		.flatMap(bol ->{
			if(bol) {
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
	
	public Mono<ServerSentStreamTemplate<?>> accountInfoChangeEmissionStream(ServerSentStreamTemplate<?> serverSentTemplate, AccountEntity account){
		var simpleUpdateAccountInfoEventResponse = 
				ServerSentStreamType.ACCOUNT_INFO_CHANGE_CAST_CLASS.cast(serverSentTemplate.getContent());
		long targetAccountId = simpleUpdateAccountInfoEventResponse.getAccountId();
		if(account.getId().equals(targetAccountId)){
			return Mono.just(serverSentTemplate);
		}
		
		return accountRepository.isWorkspaceJoinCommonAccessUser(account.getId(), targetAccountId).flatMap(bol->{
			if(bol) {
				simpleUpdateAccountInfoEventResponse.setAccountId(null);
				return Mono.just(serverSentTemplate);
			}else {
				return Mono.empty();
			}
		});
	}
}
