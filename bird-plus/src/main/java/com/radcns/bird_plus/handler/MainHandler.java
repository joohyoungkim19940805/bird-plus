package com.radcns.bird_plus.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.entity.customer.AccountEntity;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.UnauthorizedException;

import reactor.core.publisher.Mono;

import static com.radcns.bird_plus.util.Response.response;
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
				.body(accountService.createUser(request.bodyToMono(AccountEntity.class))
					.map(e -> response(Result._00, e)), Response.class
				)
				.onErrorResume(e -> Mono.error(new UnauthorizedException(100)));
	}
	
	public Mono<ServerResponse> loginProc(ServerRequest request){
		return accountService.authenticate(request.bodyToMono(AccountEntity.class), request.remoteAddress())
				.flatMap(account -> ok()
						.cookie(ResponseCookie.fromClientResponse(HttpHeaders.AUTHORIZATION, account.getToken())
								.httpOnly(true)
								.secure(true)
								.sameSite("Strict")
								.path("/")
		        				.build())
						.contentType(MediaType.APPLICATION_JSON)
						.body(Mono.just(response(Result._00, account)), Response.class));
		/*
		 ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(accountService.authenticate(request.bodyToMono(AccountEntity.class), request.remoteAddress())
					.map(e -> {
						return response(Result._00, e)
					}), Response.class)
			)
		*/
	}

	public Mono<ServerResponse> test(ServerRequest request){
		return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(request.bodyToMono(String.class).map(e->response(Result._00, e)), Response.class)
				.onErrorResume(e -> Mono.error(new UnauthorizedException(100)));
	}

	
	/*
    public  login(@RequestBody UserLoginDto dto) {
        return accountService.authenticate(dto.getAccountName(), dto.getPassword())
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
