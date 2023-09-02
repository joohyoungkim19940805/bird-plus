package com.radcns.bird_plus.web.router.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.web.handler.ChattingHandler;
import com.radcns.bird_plus.web.handler.LoginHandler;
import com.radcns.bird_plus.web.handler.MainHandler;
import com.radcns.bird_plus.web.handler.RoomHandler;
import com.radcns.bird_plus.web.handler.WorkspaceHandler;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MainRouter implements IndexRouterSwagger{
	
	@Bean
	@Override
	public RouterFunction<ServerResponse> index(MainHandler mainHandler){

    	return route( GET("/"), req -> ServerResponse.temporaryRedirect(URI.create("/login-page")).build() )
    			//.and(route( GET("/test").and(accept(MediaType.APPLICATION_JSON)), mainHandler::test ))
    			.and(route( GET("/login"), req -> ServerResponse.temporaryRedirect(URI.create("/login-page")).build() ))
				.and(route( GET("/login-page").and(accept(MediaType.TEXT_HTML)), mainHandler::loginPage ))
				.and(route( POST("/create").and(accept(MediaType.APPLICATION_JSON)), mainHandler::create ))
				.and(route( GET("/account-verify/{token}").and(accept(MediaType.TEXT_HTML)), mainHandler::accountVerifyPage ))
				.and(route( POST("/account-verify").and(accept(MediaType.APPLICATION_JSON)), mainHandler::accountVerify ))
				.and(route( POST("/login-processing").and(accept(MediaType.APPLICATION_JSON)), mainHandler::loginProcessing ))
				.and(route( POST("/forgot-password-send-email").and(accept(MediaType.APPLICATION_JSON)), mainHandler::forgotPassword ))
				.and(route( GET("/change-password-page/{token}").and(accept(MediaType.TEXT_HTML)), mainHandler::changePasswordPage ))
				.and(route( POST("/change-password").and(accept(MediaType.APPLICATION_JSON)), mainHandler::changePassword ))
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
					.POST("/send-stream", accept(MediaType.APPLICATION_JSON), chattingHandler::sendStream)
					.GET("/emission-stream/{auth}", chattingHandler::emissionStream)
				).build();
	}
	@Bean
	public RouterFunction<ServerResponse> apiWorkspace(WorkspaceHandler workspaceHandler){
		return route().nest(path("/api/workspace"), builder -> builder
					.POST("/create-workspace", accept(MediaType.APPLICATION_JSON), workspaceHandler::createWorkspace)
					.GET("/search-workspace-name", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceName)
					.GET("/is-workspace-joined", accept(MediaType.APPLICATION_JSON), workspaceHandler::isWorkspaceJoined)
					.GET("/search-workspace-my-joined", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceMyJoined)
					.GET("/search-workspace-in-account/{workspaceId}", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceInAccount)
				).build();
		/*
		return route( POST("/api/chatting/stream").and(accept(MediaType.APPLICATION_JSON)), chattingHandler::addStream )
				.and(route( GET("api/chatting/stream/{auth}"), chattingHandler::getStream ))
				.and(route( GET("api/chatting/stream-test/test"), chattingHandler::test ))
				
		;
		*/
	}
	@Bean
	public RouterFunction<ServerResponse> apiRoom(RoomHandler roomHandler){
		return route().nest(path("/api/room"), builder -> builder
				.POST("/create-room", accept(MediaType.APPLICATION_JSON), roomHandler::createRoom)
				.POST("/create-room-in-account", accept(MediaType.TEXT_EVENT_STREAM), roomHandler::createRoomInAccount)
				.POST("/create-room-favorites", accept(MediaType.APPLICATION_JSON), roomHandler::createRoomFavorites)
				.POST("/update-room-in-account", accept(MediaType.APPLICATION_JSON), roomHandler::updateRoomInAccount)
				.POST("/update-room-favorites", accept(MediaType.APPLICATION_JSON), roomHandler::updateRoomFavorites)
				.GET("/search-room", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoom)
				.GET("/search-room-my-joined", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomMyJoinedAndRoomType)
				.GET("/search-room-my-joined-name", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomMyJoinedNameAndRoomType)
				.GET("/search-room-favorites-my-joined", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomFavoritesMyJoined)
				.GET("/search-room-favorites-my-joined-name", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomFavoritesMyJoinedNema)	
			).build();
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