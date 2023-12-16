package com.radcns.bird_plus.web.router.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.web.handler.AccountHandler;
import com.radcns.bird_plus.web.handler.ChattingHandler;
import com.radcns.bird_plus.web.handler.EmoticonHandler;
import com.radcns.bird_plus.web.handler.EventStreamHandler;
import com.radcns.bird_plus.web.handler.MainHandler;
import com.radcns.bird_plus.web.handler.NoticeBoardHandler;
import com.radcns.bird_plus.web.handler.RoomHandler;
import com.radcns.bird_plus.web.handler.S3Handler;
import com.radcns.bird_plus.web.handler.WorkspaceHandler;

import java.net.URI;
import java.util.List;

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
					.build())*/
				.nest(path("update") , updatePathBuilder -> updatePathBuilder
						.POST("/simple-account-info", accept(MediaType.APPLICATION_JSON), accountHandler::updateSimpleAccountInfo)
					.build())
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiChatting(ChattingHandler chattingHandler){
		return route().nest(path("/api/chatting"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
							.POST("/send-chatting", accept(MediaType.APPLICATION_JSON), chattingHandler::sendStream)
						.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
							.GET("/chatting-list/{workspaceId}/{roomId}", accept(MediaType.APPLICATION_JSON), chattingHandler::searchChattingList)
						.build())
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiEventStream(EventStreamHandler eventStreamHandler){
		return route().nest(path("/api/event-stream"), builder -> builder
					.GET("/workspace/{workspaceId}", eventStreamHandler::emissionStream)
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiWorkspace(WorkspaceHandler workspaceHandler){
		return route().nest(path("/api/workspace"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
						.POST("/", accept(MediaType.APPLICATION_JSON), workspaceHandler::createWorkspace)
						.POST("/joined", accept(MediaType.APPLICATION_JSON), workspaceHandler::createJoinedWorkspace)
						.POST("/permit", accept(MediaType.APPLICATION_JSON), workspaceHandler::createPermitWokrspaceInAccount)
						.POST("/give-admin", accept(MediaType.APPLICATION_JSON), workspaceHandler::giveAdmin)
					.build())
				/*.nest(path("update") , updatePathBuilder -> updatePathBuilder
					.build())*/
				.nest(path("search"), searchPathBuilder -> searchPathBuilder
						.GET("/name-specific-list", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceName)
						.GET("/is-joined", accept(MediaType.APPLICATION_JSON), workspaceHandler::isWorkspaceJoined)
						.GET("/my-joined-list", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceMyJoined)
						.GET("/joined-account-list/{workspaceId}", accept(MediaType.APPLICATION_JSON), workspaceHandler::searchWorkspaceInAccount)
						.GET("/detail/{workspaceId}", accept(MediaType.APPLICATION_JSON), workspaceHandler::getWorkspaceDetail)
						.GET("/permit-request-list/{workspaceId}", accept(MediaType.TEXT_EVENT_STREAM), workspaceHandler::searchPermitRequestList)
						.GET("/is-admin/{workspaceId}", accept(MediaType.APPLICATION_JSON), workspaceHandler::getIsAdmin)
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
						.POST("/", accept(MediaType.APPLICATION_JSON), roomHandler::createRoom)
						.POST("/my-self-room/{workspaceId}", accept(MediaType.APPLICATION_JSON), roomHandler::createMySelfRoom)
						//.POST("/room-in-account", accept(MediaType.TEXT_EVENT_STREAM), roomHandler::createRoomInAccount)
						.POST("/in-account", accept(MediaType.APPLICATION_JSON), roomHandler::createRoomInAccount)
						.POST("/favorites", accept(MediaType.APPLICATION_JSON), roomHandler::createRoomFavorites)
					.build())
				.nest(path("/update"), updatePathBuilder -> updatePathBuilder
						.POST("/order", accept(MediaType.APPLICATION_JSON), roomHandler::updateRoomInAccountOrder)
						.POST("/favorites-order", accept(MediaType.APPLICATION_JSON), roomHandler::updateRoomFavoritesOrder)
					.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
						.GET("/list{workspaceId}", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomList)
						.GET("/my-joined-list/{workspaceId}", accept(MediaType.APPLICATION_JSON), roomHandler::searchMyJoinedRoomList)
						.GET("/favorites-list/{workspaceId}", accept(MediaType.APPLICATION_JSON), roomHandler::searchRoomFavoritesList)	
						.GET("/detail/{roomId}", accept(MediaType.APPLICATION_JSON), roomHandler::getRoomDetail)
						.GET("/in-account-list/{roomId}", accept(MediaType.TEXT_EVENT_STREAM), roomHandler::searchRoomJoinedAccountList)
						.GET("/is-room-favorites/{roomId}", accept(MediaType.APPLICATION_JSON), roomHandler::isRoomFavorites)
					.build())
			).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiNoticeBoard(NoticeBoardHandler noticeBoardHandler){
		return route().nest(path("/api/notice-board"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
						.POST("/", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::createNoticeBoard)
						.POST("/group", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::createNoticeBoardGroup)
						.POST("/detail", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::createNoticeBoardDetail)
						.build())
				.nest(path("/delete"), createPathBuilder -> createPathBuilder
						.POST("/", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::deleteNoticeBoard)
						.POST("/group", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::deleteNoticeBoardGroup)
						.build())
				.nest(path("/update"), updatePathBuilder -> updatePathBuilder
						.POST("/order", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::updateNoticeBoardOrder)
						.POST("/detail-order", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::updateNoticeBoardDetailOrder)
						//.POST("/group", accept(MediaType.APPLICATION_JSON), noticeBoardHandler::updateNoticeBoardGroup)
						.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
						.GET("/notice-board-list/{workspaceId}/{roomId}", accept(MediaType.TEXT_EVENT_STREAM), noticeBoardHandler::searchNoticeBoardList)
						.GET("/notice-board-detail-list/{workspaceId}/{roomId}/{noticeBoardId}", accept(MediaType.TEXT_EVENT_STREAM), noticeBoardHandler::searchNoticeBoardDetailList)
						.build())
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> apiS3(S3Handler s3Handler){
		return route().nest(path("/api/generate-presigned-url"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
						.POST("/", accept(MediaType.APPLICATION_JSON), s3Handler::generatePutObjectPresignedUrl)
						.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
						.POST("/", accept(MediaType.APPLICATION_JSON), s3Handler::generateGetObjectPresignedUrl)
						.build())
				).build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> emoticon(EmoticonHandler emoticonHandler){
		return route().nest(path("/api/emoticon"), builder -> builder
				.nest(path("/create"), createPathBuilder -> createPathBuilder
						.POST("/reaction", accept(MediaType.APPLICATION_JSON), emoticonHandler::createEmoticonReaction)
						.build())
				.nest(path("/delete"), deletePathBuilder -> deletePathBuilder
						.POST("/", accept(MediaType.APPLICATION_JSON), emoticonHandler::deleteEmoticon)
						.build())
				.nest(path("/search"), searchPathBuilder -> searchPathBuilder
						.GET("/is-reaction", accept(MediaType.APPLICATION_JSON), emoticonHandler::getIsReaction)
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