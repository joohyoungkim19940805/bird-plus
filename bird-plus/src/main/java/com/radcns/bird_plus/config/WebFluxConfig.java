package com.radcns.bird_plus.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.http.codec.multipart.PartEventHttpMessageReader;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radcns.bird_plus.config.security.JwtVerifyHandler;

import com.radcns.bird_plus.util.CommonUtil;
import com.radcns.bird_plus.util.CreateRandomCodeUtil;
import com.radcns.bird_plus.util.KeyPairUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.netty.resolver.DefaultAddressResolverGroup;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import reactor.netty.http.client.HttpClient;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Spring Webflux bird-plus",
        version = "1.0",
        description = "swagger documentation using open api."
))
public class WebFluxConfig implements ApplicationContextAware, WebFluxConfigurer {
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;
	
    @Value("${key.pair.file.dir}")
	private String keyPairFileDir;    

    @Value("${key.public.name}")
	private String keyPublicName;
    
    @Value("${key.private.name}")
	private String keyPrivateName;

    
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
		
		var partReader = new PartEventHttpMessageReader();
        // Configure the maximum amount of disk space allowed for file parts
        partReader.setEnableLoggingRequestDetails(true);
        partReader.setMaxInMemorySize(-1);
        partReader.setMaxHeadersSize(-1);
        partReader.setHeadersCharset(StandardCharsets.UTF_8);
        configurer.defaultCodecs().multipartReader(partReader);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
	    registry.viewResolver(thymeleafReactiveViewResolver());
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
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
	    templateEngine.addDialect(new LayoutDialect());
	    return templateEngine;
	}
	
	@Bean
	public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
	    ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
	    viewResolver.setTemplateEngine(thymeleafTemplateEngine());

	    return viewResolver;
	}
	
	/*
	@Bean
	public CommonUtil commonUtil() {
		return new CommonUtil();
	}
	*/
	/*
	@Bean
	public CreateRandomCodeUtil createRandomCodeUtil() {
		return new CreateRandomCodeUtil();//
	}
	*/
	
	@Bean
	public KeyPair keyPair() throws NoSuchAlgorithmException {
		if(keyPairFileDir == null || keyPairFileDir.isEmpty()) {
			keyPairFileDir = System.getProperty("user.home");
		}
		var keyPairUtil = new KeyPairUtil(keyPairFileDir, keyPublicName, keyPrivateName);
		return keyPairUtil.getKeyPair();
		//return keyPair;//Keys.keyPairFor(SignatureAlgorithm.RS256);
	}
	
	@Bean
	public JwtVerifyHandler jwtVerifyHandler(KeyPair keyPair, ObjectMapper objectMapper) {
		return new JwtVerifyHandler(keyPair, objectMapper);
	}
	
	@Bean
	public WebClient.Builder webClientBuilder() {
		HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
		
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
		
	}
	@Bean("jasyptEncryptorDES")
	public StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(System.getenv("MY_SERVER_PASSWORD")); // 암호화키
		config.setAlgorithm("PBEWithMD5AndDES"); // 알고리즘
		config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
		config.setPoolSize("1"); // 인스턴스 pool
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
		config.setStringOutputType("base64"); //인코딩 방식
		encryptor.setConfig(config);
		return encryptor;
	}
}


