package com.radcns.bird_plus.router;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.handler.MainHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
public class MainRouter {
	
	
	@Bean
	public RouterFunction<ServerResponse> index1(MainHandler webFluxHandler){
		return route( GET("/"), req -> ServerResponse.temporaryRedirect(URI.create("/loginPage")).build() )
				.and(route( GET("/login"), req -> ServerResponse.temporaryRedirect(URI.create("/loginPage")).build() ))
				.and(route( GET("/loginPage").and(accept(MediaType.TEXT_HTML)), webFluxHandler::loginPage ))
				.and(route( POST("/create").and(accept(MediaType.APPLICATION_JSON)), webFluxHandler::create ))
				.and(route( POST("/loginProc").and(accept(MediaType.APPLICATION_JSON)), webFluxHandler::loginProc ))
				.and(route( GET("/home/test").and(accept(MediaType.APPLICATION_JSON)), webFluxHandler::homeTest ));
		/*
				.and(route(POST("/searchCorpName")
						.and(accept(MediaType.APPLICATION_JSON)),
						webFluxHandler::searchCorpName));
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