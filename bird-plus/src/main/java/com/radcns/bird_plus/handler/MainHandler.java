package com.radcns.bird_plus.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.config.security.Token;
import com.radcns.bird_plus.entity.AccountEntity;
import com.radcns.bird_plus.service.AccountService;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MainHandler {
	
	@Autowired
	private AccountService accountService;
	
	public Mono<ServerResponse> index(ServerRequest request){
		return ok().contentType(MediaType.TEXT_HTML).render("/index.html");
	}
	
	public Mono<ServerResponse> loginPage(ServerRequest request){
		return ok().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")).render("content/loginPage.html"/*, Map.ofEntries(
				Map.entry("data", new TestRecord("a", (long)9999, "asasas", (int)10, 100, 100))
			)*/);
	}
	
	public Mono<ServerResponse> create(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(accountService.createUser(request.bodyToMono(AccountEntity.class)), AccountEntity.class);
	}
	
	public Mono<ServerResponse> loginProc(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(accountService.authenticate(request.bodyToMono(AccountEntity.class)), Token.class);
	}
	
	public Mono<ServerResponse> homeTest(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just("test"), Object.class);
	}
	
	/*
    public  login(@RequestBody UserLoginDto dto) {
        return accountService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenInfo -> Mono.just(ResponseEntity.ok(AuthResultDto.builder()
                        .userId(tokenInfo.getUserId())
                        .token(tokenInfo.getToken())
                        .issuedAt(tokenInfo.getIssuedAt())
                        .expiresAt(tokenInfo.getExpiresAt())
                        .build())));
    }
    */
	/*
	public Mono<ServerResponse> admin(ServerRequest request){

		return ok().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")).render("content/mainPage", ofEntries(
				entry("userInfo", userService.getUserInfo())
			));
	}
	*/
	/*
	public Mono<ServerResponse> searchCorpName(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body( stockInfoService.searchCorpName(request.bodyToMono(String.class)), Object.class);
	}
	*/
	
}
