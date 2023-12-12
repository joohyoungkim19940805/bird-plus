package com.radcns.bird_plus.web.handler;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.chatting.ChattingReactionCountEntity;
import com.radcns.bird_plus.entity.chatting.ChattingReactionCountEntity.ChattingReactionCountDomain.ChattingReactionCountResponse;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity.ChattingReactionDomain.ChattingReactionRequest;
import com.radcns.bird_plus.entity.chatting.ChattingReactionEntity.ChattingReactionDomain.ChattingReactionResponse;
import com.radcns.bird_plus.entity.emoticon.EmoticonDuplicationProcessingEntity;
import com.radcns.bird_plus.repository.chatting.ChattingReactionCountRepository;
import com.radcns.bird_plus.repository.chatting.ChattingReactionRepository;
import com.radcns.bird_plus.repository.emoticon.EmoticonDuplicationProcessingRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import io.r2dbc.postgresql.codec.Json;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks.EmitResult;

@Component
public class EmoticonHandler {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private EmoticonDuplicationProcessingRepository emoticonDuplicationProcessingRepository;
	
	@Autowired
	private ChattingReactionRepository chattingReactionRepository;
	
	@Autowired
	private ChattingReactionCountRepository chattingReactionCountRepository;
	
	@Autowired
	private WorkspaceBroker workspaceBroker;
	
	public Mono<ServerResponse> createEmoticonReaction(ServerRequest request){
		var createResult = accountService.convertRequestToAccount(request)
		.flatMap(account -> 
			request.bodyToMono(ChattingReactionRequest.class)
			.flatMap(emoticon -> emoticonDuplicationProcessingRepository.findByEmoticon(emoticon.getEmoticon())
				.switchIfEmpty(emoticonDuplicationProcessingRepository.save(
					EmoticonDuplicationProcessingEntity.builder()
					.emoticon(emoticon.getEmoticon())
					.emoticonType(emoticon.getEmoticonType())
					.description(emoticon.getDescription())
					.groupTitle(emoticon.getGroupTitle())
					.subgroupTitle(emoticon.getSubgroupTitle())
					.build()
				))
				.flatMap(emoticonDuplicationProcessingEntity -> {
					return chattingReactionRepository.findByWorkspaceIdAndRoomIdAndChattingIdAndEmoticonId(
						emoticon.getWorkspaceId(),
						emoticon.getRoomId(),
						emoticon.getChattingId(),
						emoticonDuplicationProcessingEntity.getId()
					)
					.switchIfEmpty(
						chattingReactionRepository.save(
							ChattingReactionEntity.builder()
							.workspaceId(emoticon.getWorkspaceId())
							.roomId(emoticon.getRoomId())
							.chattingId(emoticon.getChattingId())
							.emoticonId(emoticonDuplicationProcessingEntity.getId())
							.build()
						)
					)
					.flatMap(reaction -> 
						chattingReactionCountRepository.existsByChattingIdAndAccountIdAndEmoticonIdAndReactionId(
							emoticon.getChattingId(), 
							account.getId(), 
							emoticonDuplicationProcessingEntity.getId(), 
							reaction.getId()
						)
						//.switchIfEmpty(Mono.just(Boolean.FALSE))
						.flatMap(exists -> {
							int count;
							Mono<?> result;
							if( ! exists) {
								count = 1;
								result = chattingReactionCountRepository.save(
									ChattingReactionCountEntity.builder()
									.chattingId(emoticon.getChattingId())
									.accountId(account.getId())
									.emoticonId(emoticonDuplicationProcessingEntity.getId())
									.reactionId(reaction.getId())
									.build()
								);
							}else {
								result = chattingReactionCountRepository.deleteByChattingIdAndAccountIdAndEmoticonIdAndReactionId(
									emoticon.getChattingId(), 
									account.getId(), 
									emoticonDuplicationProcessingEntity.getId(), 
									reaction.getId()
								);
								count = 0;//-1;
							}
							
							return chattingReactionCountRepository.findJoinByChattingIdAndEmoticonIdAndReactionId(
								emoticon.getChattingId(), 
								emoticonDuplicationProcessingEntity.getId(), 
								reaction.getId()
							)
							.filter(e-> ! e.getAccountId().equals(account.getId()))
							.map(e->{
								e.setAccountId(null);
								return e;
							})
							.collectList()
							.map(e-> {
								long totalCount = e.size() + count;
								if(totalCount <= 0) {
									chattingReactionRepository.deleteById(reaction.getId())
									.subscribe();
								}
								if(count == 1) {
									e.add(ChattingReactionCountResponse.builder().fullName(account.getFullName()).build());
								}
								result.doOnSuccess(s->{
									EmitResult emitResult = workspaceBroker.send(
										new ServerSentStreamTemplate<ChattingReactionResponse>(
											emoticon.getWorkspaceId(),
											emoticon.getRoomId(),
											ChattingReactionResponse.builder()
												.emoticon(emoticon.getEmoticon())
												.emoticonType(emoticonDuplicationProcessingEntity.getEmoticonType())
												.workspaceId(emoticon.getWorkspaceId())
												.roomId(emoticon.getRoomId())
												.chattingId(emoticon.getChattingId())
												.count(totalCount)
												.reactionId(reaction.getId())
												.groupTitle(emoticonDuplicationProcessingEntity.getGroupTitle())
												.subgroupTitle(emoticonDuplicationProcessingEntity.getSubgroupTitle())
												.reactionList(e)
												.build(),
											ServerSentStreamType.CHATTING_REACTION_ACCEPT
										) {}
									);
									if (emitResult.isFailure()) {
										// do something here, since emission failed
									}
								}).subscribe();
								return totalCount;
							});
						})
					);
					//.flatMap(existsIsDeleteSignal -> chattingReactionRepository.deleteById(existsIsDeleteSignal.getId()))
					//.subscribe();
					//
					//;
					
					//return null;
				})
			)
		)
		;
		
		//reqeust.bodyToMono(null)
		return createResult.flatMap(count -> ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, count), ResponseWrapper.class));
				
	}

	public Mono<ServerResponse> deleteEmoticon(ServerRequest request){

		return null;
	}

	public Mono<ServerResponse> getIsReaction(ServerRequest request){
		
		return null;
	}
}
