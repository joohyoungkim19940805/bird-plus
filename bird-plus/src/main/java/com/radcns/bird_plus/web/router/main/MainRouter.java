package com.radcns.bird_plus.web.router.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.web.handler.AccountHandler;
import com.radcns.bird_plus.web.handler.ChattingHandler;
import com.radcns.bird_plus.web.handler.EventStreamHandler;
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
	
	/*
				.nest(path("/create"), createPathBuilder -> createPathBuilder
					.build())
				.nest(path("update") , updatePathBuilder -> updatePathBuilder
					.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
					.build())
	 */
	
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
	public RouterFunction<ServerResponse> apiAccount(AccountHandler accountHandler){
		return route().nest(path("/api/account"), builder -> builder
				.nest(path("search"), searchPathBuilder -> searchPathBuilder
						.GET("/is-login", accept(MediaType.APPLICATION_JSON), accountHandler::isLogin)
						.GET("/get-account-info", accept(MediaType.APPLICATION_JSON), accountHandler::getAccountInfo)
					.build())
				/*.nest(path("/create"), createPathBuilder -> createPathBuilder
					.build())
				.nest(path("update") , updatePathBuilder -> updatePathBuilder
					.build())*/
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiChatting(ChattingHandler chattingHandler){
		return route().nest(path("/api/chatting"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
							.POST("/send-chatting", accept(MediaType.APPLICATION_JSON), chattingHandler::sendStream)
						.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
							.GET("/chatting-list", accept(MediaType.APPLICATION_JSON), chattingHandler::searchChattingList)
						.build())
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiEventStream(EventStreamHandler eventStreamHandler){
		return route().nest(path("/api/event-stream"), builder -> builder
					.GET("/workspace/{workspaceId}/{auth}", eventStreamHandler::emissionStream)
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiWorkspace(WorkspaceHandler workspaceHandler){
		return route().nest(path("/api/workspace"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
						.POST("/workspace", accept(MediaType.APPLICATION_JSON), workspaceHandler::createWorkspace)
					.build())
				/*.nest(path("update") , updatePathBuilder -> updatePathBuilder
					.build())*/
				.nest(path("search"), searchPathBuilder -> searchPathBuilder
						.GET("/workspace-name-list", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceName)
						.GET("/is-workspace-joined", accept(MediaType.APPLICATION_JSON), workspaceHandler::isWorkspaceJoined)
						.GET("/workspace-my-joined-list", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceMyJoined)
						.GET("/workspace-in-account-list/{workspaceId}", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceInAccount)
						.GET("/workspace-detail/{workspaceId}", accept(MediaType.APPLICATION_JSON), workspaceHandler::getWorkspaceDetail)
					.build())
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
				.nest(path("/create"), createPathBuilder -> createPathBuilder
						.POST("/room", accept(MediaType.APPLICATION_JSON), roomHandler::createRoom)
						.POST("/my-self-room/{workspaceId}", accept(MediaType.APPLICATION_JSON), roomHandler::createMySelfRoom)
						//.POST("/room-in-account", accept(MediaType.TEXT_EVENT_STREAM), roomHandler::createRoomInAccount)
						.POST("/room-in-account", accept(MediaType.APPLICATION_JSON), roomHandler::createRoomInAccount)
						.POST("/room-favorites", accept(MediaType.APPLICATION_JSON), roomHandler::createRoomFavorites)
					.build())
				.nest(path("/update"), updatePathBuilder -> updatePathBuilder
						.POST("/room-in-account-order", accept(MediaType.APPLICATION_JSON), roomHandler::updateRoomInAccountOrder)
						.POST("/room-favorites-order", accept(MediaType.APPLICATION_JSON), roomHandler::updateRoomFavoritesOrder)
					.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
						.GET("/room-list", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoom)
						.GET("/room-my-joined-list", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomMyJoinedAndRoomType)
						.GET("/room-my-joined-name-list", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomMyJoinedNameAndRoomType)
						.GET("/room-my-joined-favorites-list", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomFavoritesMyJoined)
						.GET("/room-my-joined-favorites-name-list", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomFavoritesMyJoinedNema)	
						.GET("/room-detail/{roomId}", accept(MediaType.APPLICATION_JSON), roomHandler::getRoomDetail)
						.GET("/room-in-account-all-list/{roomId}", accept(MediaType.TEXT_EVENT_STREAM), roomHandler::searchRoomInAccountAllList)
						.GET("/is-room-favorites/{roomId}", accept(MediaType.APPLICATION_JSON), roomHandler::isRoomFavorites)
					.build())
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