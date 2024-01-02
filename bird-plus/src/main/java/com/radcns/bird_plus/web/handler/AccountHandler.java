package com.radcns.bird_plus.web.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.account.AccountEntity.AccountDomain.SimpleUpdateAccountInfoEventResponse;
import com.radcns.bird_plus.entity.account.AccountEntity.AccountDomain.SimpleUpdateAccountInfoRequest;
import com.radcns.bird_plus.entity.chatting.ChattingEntity.ChattingDomain.ChattingResponse;
import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ResponseWrapper;
import com.radcns.bird_plus.util.exception.BirdPlusException.Result;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate;
import com.radcns.bird_plus.util.stream.WorkspaceBroker;
import com.radcns.bird_plus.util.stream.ServerSentStreamTemplate.ServerSentStreamType;

import io.r2dbc.postgresql.codec.Json;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks.EmitResult;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AccountHandler {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WorkspaceBroker workspaceBroker;
	
	public Mono<ServerResponse> isLogin(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0), ResponseWrapper.class);
	}

	public Mono<ServerResponse> getAccountInfo(ServerRequest request){
		return ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(
			accountService.convertRequestToAccount(request).doOnNext(e->e.withId(null)).flatMap(e-> response(Result._0, e)), ResponseWrapper.class
		);
	}
	
	public Mono<ServerResponse> updateSimpleAccountInfo(ServerRequest request){
		return accountService.convertRequestToAccount(request).flatMap(account->
			request.bodyToMono(SimpleUpdateAccountInfoRequest.class).flatMap(accountRequest -> {
				return accountRepository.save(
					account.withFullName(accountRequest.getFullName())
					.withJobGrade(accountRequest.getJobGrade())
					.withDepartment(accountRequest.getDepartment())
					.withProfileImage(accountRequest.getProfileImage())
				).doOnSuccess(s -> {
					workspaceBroker.sendGlobal(
						new ServerSentStreamTemplate<SimpleUpdateAccountInfoEventResponse>(
							(long)0,
							(long)0,
							SimpleUpdateAccountInfoEventResponse.builder()
								.fullName(s.getFullName())
								.jobGrade(s.getJobGrade())
								.department(s.getDepartment())
								.profileImage(s.getProfileImage())
								.accountId(account.getId())
								.accountName(account.getAccountName())
							.build(),
							ServerSentStreamType.ACCOUNT_INFO_CHANGE_ACCEPT
						) {}
					).subscribe();
				});
			})
			.flatMap(e->
				ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(response(Result._0, e.withId(null)), ResponseWrapper.class)
			)
		);
	}
	
}
