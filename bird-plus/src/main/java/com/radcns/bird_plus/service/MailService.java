package com.radcns.bird_plus.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.mail.MailException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;

import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;

import com.radcns.bird_plus.config.security.JwtIssuerType;
import com.radcns.bird_plus.entity.customer.AccountEntity;
import com.radcns.bird_plus.entity.dto.ForgotEmailDto;

import com.radcns.bird_plus.util.properties.EmailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
	
    //from: no-reply@test.com
    //base-url: http://localhost:8080

	
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private static final String DEFAULT_LANGUAGE = "ko";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ISpringWebFluxTemplateEngine thymeleafTemplateEngine;
    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private AccountService accountService;
    
    //from: no-reply@test.com
    //base-url: http://localhost:8080

    private void sendEmail(ForgotEmailDto sender) {

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    	//SimpleMailMessage message = new SimpleMailMessage();
    	try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, sender.isMultipart(), StandardCharsets.UTF_8.name());
            message.setTo(sender.getTo());
            message.setFrom(emailProperties.getFrom());
            message.setSubject(sender.getSubject());
            message.setText(sender.getContent(), sender.isHtml());
            javaMailSender.send(mimeMessage);
            
            logger.debug("Sent email to User '{}'", sender.getTo());
        } catch (MailException | MessagingException e) {
        	logger.warn("Email could not be sent to user '{}'", sender.getTo(), e);
        }
    }

    private void sendEmailFromTemplate(AccountEntity account, String templateName) {
        if (account.getEmail() == null) {
        	logger.debug("Email doesn't exist for user '{}'", account.getEmail());
            return;
        }
        Locale locale = Locale.forLanguageTag(DEFAULT_LANGUAGE);
        Context context = new Context(locale);
        account.setEmail(URLEncoder.encode(account.getEmail(), StandardCharsets.UTF_8));
        context.setVariable("account", account);
        context.setVariable("baseUrl", emailProperties.getBaseUrl());

        context.setVariable("token", accountService.generateAccessToken(account, JwtIssuerType.FORGOT_PASSWORD).getToken());

        String content = thymeleafTemplateEngine.process(templateName, context);
        String subject = "Account Recovery - RADCNS";//messageSource.getMessage(titleKey, null, locale);

        sendEmail(ForgotEmailDto.builder()
                .content(content).subject(subject).to(account.getEmail())
                .isHtml(true).isMultipart(false)
                .build());
    }

    public void sendForgotPasswordEmail(AccountEntity account, String templateName) {
        System.out.println(account);
    	logger.debug("Sending login OTP email to '{}'", account.getEmail());
        sendEmailFromTemplate(account, templateName);
        /*return ServerResponse.ok()
    			.contentType(MediaType.APPLICATION_JSON)
    			.body(Mono.just(response(Result._00, null)), Response.class);*/
    }


    /*
    public Mono<ForgotEmailResponseDto> sendForgotPasswordEmail(AccountEntity account, String templateName, String subject) {
        logger.debug("Sending login OTP email to '{}'", account.getEmail());
        if (account.getEmail() == null) {
        	logger.debug("Email doesn't exist for user '{}'", account.getEmail());
        }
        Locale locale = Locale.forLanguageTag(DEFAULT_LANGUAGE);
        Context context = new Context(locale);
        context.setVariable(USER, account.getName());
        context.setVariable(BASE_URL, emailProperties.getBaseUrl());
        String content = thymeleafTemplateEngine.process(templateName, context);
        
        ForgotEmailDto request = ForgotEmailDto.builder()
        	.apiKey(emailProperties.getApiKey())
        	.from(emailProperties.getFrom())
        	.subject(subject)
        	.bodyHtml(content)
        	.charset(StandardCharsets.UTF_8.displayName())
        	.charsetBodyHtml(StandardCharsets.UTF_8.displayName())
        	.to(List.of(account.getEmail()))
        	.build();
        
        HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
        
        WebClient webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).baseUrl("https://api.elasticemail.com/")
    			.exchangeStrategies(
    				ExchangeStrategies.builder()
    				.codecs(configurer -> configurer.defaultCodecs()
    					.maxInMemorySize(-1)
    				).build()
    			)
    			.build();
        return webClient.post()
		.uri(uriBuilder -> uriBuilder
			.path("v2/email/send")
			.build()
		)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(request), ForgotEmailDto.class)
		.retrieve()
	    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new WebClientNeedRetryException("Server error", response.statusCode().value())))
		.bodyToMono(ForgotEmailResponseDto.class)
	    .retryWhen(Retry.backoff(Integer.MAX_VALUE, Duration.ofSeconds(30))
	    	.filter(throwable -> throwable instanceof WebClientNeedRetryException)).doOnNext(e->{
	    		System.out.println("kjh test <<<<");
	    		System.out.println(e);
	    	});
    }
	*/
}

