package com.radcns.bird_plus.web.router;


import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.web.handler.ChattingHandler;
import com.radcns.bird_plus.web.handler.LoginHandler;
import com.radcns.bird_plus.web.handler.MainHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.net.URI;

import com.radcns.bird_plus.util.ExceptionCodeConstant.Result;
import com.radcns.bird_plus.util.Response;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;



@Configuration
public class MainRouter {
	
    @RouterOperations({
    	@RouterOperation(path = "/default/login-processing", produces = {MediaType.APPLICATION_JSON_VALUE },
    			beanClass = MainHandler.class,  beanMethod = "isAuthenticated",
                operation = @Operation(operationId = "isAuthenticated",
                        responses = {
                        @ApiResponse(responseCode = "200", description = "get current user.", content = @Content(schema = @Schema(implementation = Response.class)))
                })
    	)
    })
	
	@Bean
	public RouterFunction<ServerResponse> index(MainHandler mainHandler){

    	return route( GET("/"), req -> ServerResponse.temporaryRedirect(URI.create("/login-page")).build() )
				.and(route( GET("/login"), req -> ServerResponse.temporaryRedirect(URI.create("/login-page")).build() ))
				.and(route( GET("/login-page").and(accept(MediaType.TEXT_HTML)), mainHandler::loginPage ))
				.and(route( POST("/create").and(accept(MediaType.APPLICATION_JSON)), mainHandler::create ))
				.and(route( POST("/login-processing").and(accept(MediaType.APPLICATION_JSON)), mainHandler::loginProc ))
				.and(route( POST("/forgot-password-send-email").and(accept(MediaType.APPLICATION_JSON)), mainHandler::forgotPassword ))
				;
	}

	@Bean
	public RouterFunction<ServerResponse> apiAccount(LoginHandler loginHandler){
		return route().nest(path("/api/account"), builder -> builder
				.GET("/is-login", accept(MediaType.APPLICATION_JSON), loginHandler::isLogin)
				)
				.build();
		/*return route( GET("/api/login/isLogin").and(accept(MediaType.APPLICATION_JSON)), loginHandler::isLogin)
				.and(route( GET("api/login/forgot-password/email").and(accept(MediaType.APPLICATION_JSON)), loginHandler::isLogin ))
				;
				*/
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiChatting(ChattingHandler chattingHandler){
		return route().nest(path("/api/chatting"), builder -> builder
					.POST("/stream", accept(MediaType.APPLICATION_JSON), chattingHandler::addStream)
					.GET("/stream/{auth}", chattingHandler::getStream)
				).build();
		/*
		return route( POST("/api/chatting/stream").and(accept(MediaType.APPLICATION_JSON)), chattingHandler::addStream )
				.and(route( GET("api/chatting/stream/{auth}"), chattingHandler::getStream ))
				.and(route( GET("api/chatting/stream-test/test"), chattingHandler::test ))
				
		;
		*/
	}
	
	
	
	/*
	public Mono<ServerResponse> admin(ServerRequest request){

		return ok().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")).render("content/mainPage", ofEntries(
				entry("userInfo", userService.getUserInfo())
			));
	}
	@Bean
	public RouterFunction<ServerResponse> admin(MainHandler webFluxHandler){
		return route(GET("/admin")
				.and(accept(MediaType.TEXT_HTML)),
				webFluxHandler::admin);
	}
	
	*/
	
	
}