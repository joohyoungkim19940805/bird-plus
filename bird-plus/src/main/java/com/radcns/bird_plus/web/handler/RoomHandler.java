package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.room.RoomEntity;
import com.radcns.bird_plus.entity.room.RoomInAccountEntity;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.repository.room.RoomFavoritesRepository;
import com.radcns.bird_plus.repository.room.RoomInAccountRepository;
import com.radcns.bird_plus.repository.room.RoomRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.exception.WorkspaceException;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;

import reactor.core.publisher.Mono;

@Component
public class RoomHandler {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RoomInAccountRepository roomInAccountRepository;
	
	@Autowired
	private RoomFavoritesRepository roomFavoritesRepository;
	
	public Mono<ServerResponse> createRoom(ServerRequest request){
		return accountService.convertJwtToAccount(request)
		.flatMap(account -> request.bodyToMono(RoomEntity.class)
			.flatMap(room ->{
				room.setCreateBy(account.getId());
				return roomRepository.save(room);
			})
			.doOnSuccess(e->
				roomInAccountRepository.save(
					RoomInAccountEntity.builder()
					.roomId(e.getId())
					.accountId(account.getId())
					.build()
				).subscribe()
			)
		)
		.flatMap(room -> ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, room)), Response.class)
		)
		;
	}
	
	public Mono<ServerResponse> searchRoomJoined(ServerRequest request){
		ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertJwtToAccount(request)
			.flatMap(account -> {
				var param = request.queryParams();
				Long workspaceId = Long.valueOf(param.getFirst("workspaceId"));
				if(workspaceId == null) {
					return Mono.empty();
				}
				
				PageRequest pageRequest = PageRequest.of(
					Integer.valueOf(param.getOrDefault("page", List.of("1")).get(0)),
					Integer.valueOf(param.getOrDefault("size", List.of("10")).get(0))	
				);
				
				return roomInAccountRepository.findAllByAccountIdAndWorkspaceId(account.getId(), workspaceId, pageRequest)
				.collectList()
				.zipWith(roomInAccountRepository.countByAccountIdAndWorkspaceId(account.getId(), workspaceId))
				.map(entityTuples -> 
					new PageImpl<>(entityTuples.getT1(), pageRequest, entityTuples.getT2())
				)
				;
			})
			.switchIfEmpty(Mono.error(new WorkspaceException(Result._200)))
			.map(list -> response(Result._0, list))
		, Response.class);
		return null;
	}
}
