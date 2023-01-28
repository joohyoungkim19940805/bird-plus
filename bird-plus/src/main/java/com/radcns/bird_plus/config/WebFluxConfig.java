package com.radcns.bird_plus.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.util.CommonUtil;

@Configuration
public class WebFluxConfig implements ApplicationContextAware, WebFluxConfigurer {
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		configurer.defaultCodecs().jackson2JsonEncoder(
			new Jackson2JsonEncoder(objectMapper)
		);
		configurer.defaultCodecs().jackson2JsonDecoder(
			new Jackson2JsonDecoder(objectMapper)
		);
		// * @param byteCount the max number of bytes to buffer, or -1 for unlimited
		configurer.defaultCodecs().maxInMemorySize(-1);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = context;
	}
	
	@Bean
	public CommonUtil commonUtil() {
		return new CommonUtil();
	}
	
}


