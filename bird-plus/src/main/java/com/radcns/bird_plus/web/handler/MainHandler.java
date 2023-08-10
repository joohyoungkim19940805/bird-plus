package com.radcns.bird_plus.web.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.config.security.JwtIssuerType;
import com.radcns.bird_plus.config.security.JwtVerifyHandler;
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.account.AccountEntity.AccountVerifyRequest;
import com.radcns.bird_plus.entity.account.AccountEntity.ChangePasswordRequest;
import com.radcns.bird_plus.repository.customer.AccountRepository;
import com.radcns.bird_plus.service.AccountService;
import com.radcns.bird_plus.service.MailService;
import com.radcns.bird_plus.util.Response;
import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.exception.ApiException;
import com.radcns.bird_plus.util.exception.AccountException;
import com.radcns.bird_plus.util.exception.ForgotPasswordException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.radcns.bird_plus.util.Response.response;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MainHandler {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;

	public Mono<ServerResponse> index(ServerRequest request){
		return ok().contentType(MediaType.TEXT_HTML).render("/index.html");
	}
	
	public Mono<ServerResponse> loginPage(ServerRequest request){
		return ok()
				.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
				.render("content/loginPage.html", Map.of("resourcesNameList", List.of("loginPage")));
	}
	
	public Mono<ServerResponse> create(ServerRequest request){
		/*return ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(accountService.createUser(request.bodyToMono(AccountEntity.class))
					.map(e -> response(Result._0, e)), Response.class
				)
				.onErrorResume(e -> Mono.error(new UnauthorizedException(Result._110)));
				*/
		return accountService.createUser(request.bodyToMono(AccountEntity.class))
		.doOnNext(account -> {
			Mono.just(account)
			.publishOn(Schedulers.boundedElastic())
			.subscribe(e -> {
				mailService.sendAccountVerifyTemplate(e);
			});
		}).flatMap(account -> 
			ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(response(Result._0, null)), Response.class)
		);

	}
	
	public Mono<ServerResponse> accountVerifyPage(ServerRequest request){
		return Mono.just(request.queryParam("email"))
				.flatMap(emial -> {
					String token = request.pathVariable("token").replace("bearer-", "");
					
					Jws<Claims> jws = jwtVerifyHandler.getJwt(token);
					Claims claims = jws.getBody();
					JwsHeader<?> header = jws.getHeader();
					Date expiration = claims.getExpiration();
					
					if( ! JwtIssuerType.ACCOUNT_VERIFY.name().equals( String.valueOf(header.get("jwtIssuerType")) )) {
						return ok()
								.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
								.render("content/account/accountVerify.html");
					}else if(expiration.before(new Date())) {
						return ok()
								.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
								.render("content/account/accountVerify.html");
					}

					return ok()
							.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
							.render("content/account/accountVerify.html", Map.of(
									"email", claims.getSubject(),
									"token", token
							));
				})
				//.onErrorResume(e->Mono.error(new UnauthorizedException(Result._104)))
				;
	}
	
	public Mono<ServerResponse> accountVerify(ServerRequest request){
		return request.bodyToMono(AccountVerifyRequest.class)
				.flatMap(accountVerifyRequest ->{
					return accountRepository.findByEmail(accountVerifyRequest.getEmail())
					.switchIfEmpty(Mono.error(new ApiException(Result._1)))
					.map(accountEntity->{
						accountEntity.setIsEnabled(true);
						return accountEntity;
					});
				})
				.flatMap(accountEntity->
					accountRepository.save(accountEntity)
				)
				.switchIfEmpty(Mono.error(new ForgotPasswordException(Result._108)))
				.flatMap(e->
					ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(response(Result._0, null)), Response.class)
				)
				;
		/*
		return Mono.just(request.queryParam("email"))
				.flatMap(emial -> {
					String token = request.pathVariable("token").replace("bearer-", "");
					
					Jws<Claims> jws = jwtVerifyHandler.getJwt(token);
					Claims claims = jws.getBody();
					JwsHeader<?> header = jws.getHeader();
					Date expiration = claims.getExpiration();
					
					if( ! JwtIssuerType.ACCOUNT_VERIFY.name().equals( String.valueOf(header.get("jwtIssuerType")) )) {
						return ok()
								.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
								.render("content/account/accountVerifyTemplate.html");
					}else if(expiration.before(new Date())) {
						return ok()
								.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
								.render("content/account/accountVerifyTemplate.html");
					}

					return ok()
							.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
							.render("content/account/accountVerifyTemplate.html", Map.of(
									"email", claims.getSubject(),
									"token", token
							));
				})
				.onErrorResume(e->{
					return Mono.error(new UnauthorizedException(Result._104));
				});
		*/
	}
	
	public Mono<ServerResponse> loginProcessing(ServerRequest request){
		return accountService.authenticate(request.bodyToMono(AccountEntity.class), request.remoteAddress())
				.flatMap(token -> ok()
						.cookie(ResponseCookie.fromClientResponse(HttpHeaders.AUTHORIZATION, token.getToken())
								.httpOnly(true)
								.sameSite("Strict")
								.path("/")
		        				.build())
						.contentType(MediaType.APPLICATION_JSON)
						.body(Mono.just(response(Result._0, token)), Response.class));
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
				.body(request.bodyToMono(String.class).map(e->response(Result._0, e)), Response.class)
				//.onErrorResume(e -> Mono.error(new UnauthorizedException(Result._999)))
				;
	}
	
	public Mono<ServerResponse> forgotPassword(ServerRequest request){
		return request.bodyToMono(AccountEntity.class)
				.flatMap(account -> accountRepository.findByEmail(account.getEmail()))
				.switchIfEmpty(Mono.error(new AccountException(Result._108)))
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(e->{
					// 이메일 전송이 오래걸리므로 응답에 3~6초씩 걸림
					// 병목이 발생하지 않도록 별도 스레드를 통해 처리한다.
					Mono.just(e)
					.publishOn(Schedulers.boundedElastic())
					.subscribe(account->{
						mailService.sendForgotPasswordEmail(account);
					});
				})
				//.subscribeOn(Schedulers.boundedElastic())
				.flatMap(account -> 
					ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(response(Result._0, account)), Response.class)
				)
				;
	}
	
	public Mono<ServerResponse> changePasswordPage(ServerRequest request){
		return Mono.just(request.queryParam("email"))
			.flatMap(emial -> {
				String token = request.pathVariable("token").replace("bearer-", "");
				
				Jws<Claims> jws = jwtVerifyHandler.getJwt(token);
				Claims claims = jws.getBody();
				JwsHeader<?> header = jws.getHeader();
				Date expiration = claims.getExpiration();

				if( ! JwtIssuerType.FORGOT_PASSWORD.name().equals( String.valueOf(header.get("jwtIssuerType")) )) {
					return ok()
							.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
							.render("content/account/changePasswordExpired.html");
				}else if(expiration.before(new Date())) {
					return ok()
							.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
							.render("content/account/changePasswordExpired.html");
				}

				return ok()
						.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.render("content/account/changePassword.html", Map.of(
								"email", claims.getSubject(),
								"token", token
						));
			})
			.onErrorResume(e->{
				return Mono.error(new AccountException(Result._104));
			});
	}
	
	public Mono<ServerResponse> changePassword(ServerRequest request){
		return request.bodyToMono(ChangePasswordRequest.class)
			.flatMap(changePasswordRequest ->
				accountRepository.findByEmail(changePasswordRequest.getEmail())
				.switchIfEmpty(Mono.error(new AccountException(Result._1)))
				.map(accountEntity->{
					accountEntity.setPassword(accountService.passwordEncoder.encode(changePasswordRequest.getPassword()));
					return accountEntity;
				})
			)
			.flatMap(accountEntity->
				accountRepository.save(accountEntity)
			)
			.switchIfEmpty(Mono.error(new ForgotPasswordException(Result._108)))
			.flatMap(e->
				ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(response(Result._0, null)), Response.class)
			)
			;
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
