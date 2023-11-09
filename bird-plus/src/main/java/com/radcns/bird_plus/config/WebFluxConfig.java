package com.radcns.bird_plus.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

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

import com.radcns.bird_plus.util.KeyPairUtil;
import com.radcns.bird_plus.util.properties.S3Properties;

import io.netty.resolver.DefaultAddressResolverGroup;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import reactor.netty.http.client.HttpClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
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
		config.setPassword(System.getenv("MY_SERVER_PASSWORD"));
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}
	
	@Bean
	public S3AsyncClientBuilder s3AsyncClientBuilder(S3Properties s3Properties) {
		EnvironmentVariableCredentialsProvider.create();
		var builder = S3AsyncClient.builder();
		String profiles = System.getenv("MY_SERVER_PROFILES");
		if(profiles != null && ! profiles.equals("local")) {
			builder.credentialsProvider(InstanceProfileCredentialsProvider.create());
		}else {
			builder.credentialsProvider(() -> {
				return AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecurityKey());
			});
		}
		builder.httpClientBuilder(NettyNioAsyncHttpClient.builder()
	        .connectionTimeout(Duration.ofMillis(5_000))
	        .maxConcurrency(100)
	        .tlsNegotiationTimeout(Duration.ofMillis(3_500))
		)
		.region(Region.of(s3Properties.getRegion()))
		.serviceConfiguration(t -> t
			/**
			 * 체크섬 유효성 검사를 비활성화하고 청크 인코딩을 활성화합니다. 
			 * 데이터가 스트리밍 방식으로 서비스에 도착하자마자 버킷에 데이터 업로드를 시작하려고 하기 때문에 이렇게 하는 것입니다.
			 */
			.checksumValidationEnabled(false)
			.chunkedEncodingEnabled(true)
		);
		
		return builder;
	}
	
	@Bean
	public S3Presigner.Builder s3PresignerBuilder(S3Properties s3Properties) {
		String profiles = System.getenv("MY_SERVER_PROFILES");
		var builder = S3Presigner.builder();
		if(profiles != null && ! profiles.equals("local")) {
			builder.credentialsProvider(InstanceProfileCredentialsProvider.create());
		}else {
			builder.credentialsProvider(() -> {
				return AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecurityKey());
			});
		}
		builder.region(Region.of(s3Properties.getRegion()));
		/*.serviceConfiguration(
			S3Configuration
			.builder()
			.checksumValidationEnabled(true)
			.chunkedEncodingEnabled(false)
			.build()
		);*/
		return builder;
	}
}


