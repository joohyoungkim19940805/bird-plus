package com.radcns.bird_plus.service;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.entity.email.vo.EmailRequest;
import com.radcns.bird_plus.util.properties.EmailProperties;
import com.radcns.bird_plus.util.properties.EmailProperties.AccountVerifyProperties;
import com.radcns.bird_plus.util.properties.EmailProperties.EmailPropertiesTemplate;
import com.radcns.bird_plus.util.properties.EmailProperties.ForgotPasswordProperties;

import groovyjarjarantlr4.v4.parse.GrammarTreeVisitor.locals_return;
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
    private ISpringWebFluxTemplateEngine thymeleafTemplateEngine;
    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private AccountService accountService;
    
    //from: no-reply@test.com
    //base-url: http://localhost:8080

    private void sendEmail(EmailRequest sender) {

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
        	logger.error("Email could not be sent to user '{}'", sender.getTo(), e);
        }
    }

    private String createEmailTemplate(String templateName, Map<String, Object> data) {
    	Context context = new Context(Locale.forLanguageTag(DEFAULT_LANGUAGE));
    	
    	data.entrySet().forEach(entry->{
    		context.setVariable(entry.getKey(), entry.getValue());
    	});
    	return thymeleafTemplateEngine.process(templateName, context);
    }
    
    private void generatorEmailProperties(AccountEntity account, EmailPropertiesTemplate emailPropertiesTemplate, Map<String, Object> data) {
    	String templateName = emailPropertiesTemplate.getTemplateName();
    	String subject = emailPropertiesTemplate.getSubject();
    	
    	String content = createEmailTemplate(templateName, data);
    	
        sendEmail(EmailRequest.builder()
                .content(content).subject(subject).to(account.getEmail())
                .isHtml(true).isMultipart(false)
                .build());
    }
    
    public void sendForgotPasswordEmail(AccountEntity account) {
    	logger.debug("Sending forgot password email to '{}'", account.getEmail());
    	if (account.getEmail() == null) {
        	logger.error("Email doesn't exist for user '{}'", account.getEmail());
            return;
        }
    	
    	generatorEmailProperties(account, emailProperties.getForgotPassword(), Map.of(
        		"email", account.getEmail(),
        		"baseUrl", emailProperties.getBaseUrl(),
        		"token", accountService.generateAccessToken(account, JwtIssuerType.FORGOT_PASSWORD).getToken()
        		));
    }
    
    public void sendAccountVerifyTemplate(AccountEntity account) {
    	logger.debug("Sending account verify email to '{}'", account.getEmail());
    	if (account.getEmail() == null) {
        	logger.error("Email doesn't exist for user '{}'", account.getEmail());
            return;
        }
    	
    	generatorEmailProperties(account, emailProperties.getAccountVerify(), Map.of(
        		"email", account.getEmail(),
        		"baseUrl", emailProperties.getBaseUrl(),
        		"token", accountService.generateAccessToken(account, JwtIssuerType.ACCOUNT_VERIFY).getToken()
        		));
    }

}

