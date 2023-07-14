package com.radcns.bird_plus.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;

import com.radcns.bird_plus.entity.customer.AccountEntity;
import com.radcns.bird_plus.entity.dto.EmailDto;
import com.radcns.bird_plus.util.CommonUtil;
import com.radcns.bird_plus.util.properties.EmailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
	
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private static final String DEFAULT_LANGUAGE = "ko";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ISpringWebFluxTemplateEngine thymeleafTemplateEngine;
    @Autowired
    private EmailProperties emailProperties;
    
    //from: no-reply@test.com
    //base-url: http://localhost:8080

    private void sendEmail(EmailDto sender) {

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
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

    private void sendEmailFromTemplate(AccountEntity account, String templateName, String title) {
        if (account.getEmail() == null) {
        	logger.debug("Email doesn't exist for user '{}'", account.getEmail());
            return;
        }
        Locale locale = Locale.forLanguageTag(DEFAULT_LANGUAGE);
        Context context = new Context(locale);
        context.setVariable(USER, account.getName());
        context.setVariable(BASE_URL, emailProperties.getBaseUrl());
        String content = thymeleafTemplateEngine.process(templateName, context);
        String subject = title;//messageSource.getMessage(titleKey, null, locale);

        sendEmail(EmailDto.builder()
                .content(content).subject(subject).to(account.getEmail())
                .isHtml(true).isMultipart(false)
                .build());
    }

    @Async
    public void sendForgotPasswordEmail(AccountEntity account, String templateName, String title) {
        logger.debug("Sending login OTP email to '{}'", account.getEmail());
        sendEmailFromTemplate(account, templateName, title);
    }
}

