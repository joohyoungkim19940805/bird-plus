package com.radcns.bird_plus.router;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.radcns.bird_plus.handler.MainHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
public class MainRouter {
	
	
	@Bean
	public RouterFunction<ServerResponse> index1(MainHandler webFluxHandler){
		return route(GET("/")
					.and(accept(MediaType.TEXT_HTML)),
					webFluxHandler::index);
		/*
				.and(route(POST("/searchCorpName")
						.and(accept(MediaType.APPLICATION_JSON)),
						webFluxHandler::searchCorpName));
		*/
	}
	
	
}