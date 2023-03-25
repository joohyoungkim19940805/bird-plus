package com.radcns.bird_plus.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.radcns.bird_plus.util.CommonUtil;
import com.radcns.bird_plus.util.CreateRandomCodeUtil;

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
		this.context = applicationContext;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
	    registry.viewResolver(thymeleafReactiveViewResolver());
	}
	
	@Bean
	public ITemplateResolver thymeleafTemplateResolver() {
	    final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
	    resolver.setApplicationContext(this.context);
	    resolver.setPrefix("classpath:/templates/");
	    resolver.setSuffix(".html");
	    resolver.setTemplateMode(TemplateMode.HTML);
	    resolver.setCacheable(false);
	    resolver.setCheckExistence(false);
	    resolver.setCharacterEncoding("UTF-8");
	    
	    return resolver;
	}
	
	@Bean
	public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
	    SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
	    templateEngine.setTemplateResolver(thymeleafTemplateResolver());
	    return templateEngine;
	}
	
	@Bean
	public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
	    ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
	    viewResolver.setTemplateEngine(thymeleafTemplateEngine());

	    return viewResolver;
	}
	
	@Bean
	public CommonUtil commonUtil() {
		return new CommonUtil();
	}
	
	@Bean
	public CreateRandomCodeUtil createRandomCodeUtil() {
		return new CreateRandomCodeUtil();//
	}
	
}


