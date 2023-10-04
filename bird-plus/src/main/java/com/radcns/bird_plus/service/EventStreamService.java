package com.radcns.bird_plus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;

import reactor.core.publisher.Mono;

@Component
@Service
public class EventStreamService {
	
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	
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
	
}
