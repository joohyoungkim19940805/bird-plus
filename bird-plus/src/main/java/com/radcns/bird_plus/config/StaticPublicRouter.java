package com.radcns.bird_plus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.resources;


public class StaticPublicRouter {
	@Bean
	public RouterFunction<ServerResponse> image(){
		return resources("bird-plus-desktop/bird-plus/view/image", new ClassPathResource("image/"));
	}
	
	@Bean
	public RouterFunction<ServerResponse> css(){
		return resources("bird-plus-desktop/bird-plus/view/css", new ClassPathResource("css/"));
	}
	
	@Bean
	public RouterFunction<ServerResponse> javascript(){
		return resources("bird-plus-desktop/bird-plus/view/js", new ClassPathResource("js/"));
	}
	
}
