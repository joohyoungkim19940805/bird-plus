package com.radcns.bird_plus.web.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.repository.account.AccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.ResponseWrapper;

import reactor.core.publisher.Mono;

import static com.radcns.bird_plus.util.ResponseWrapper.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AccountHandler {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	
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
	
}
