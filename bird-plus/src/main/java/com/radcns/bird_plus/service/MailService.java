package com.radcns.bird_plus.service;

import java.util.Locale;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
@Service
public class MailService {
    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private static final String DEFAULT_LANGUAGE = "en";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SpringTemplateEngine templateEngine;

    //from: no-reply@test.com
    //base-url: http://localhost:8080

    private void sendEmail(EmailSenderDTO sender) {

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, sender.isMultipart(), StandardCharsets.UTF_8.name());
            message.setTo(sender.getTo());
            message.setFrom("no-reply@test.com");
            message.setSubject(sender.getSubject());
            message.setText(sender.getContent(), sender.isHtml());
            javaMailSender.send(mimeMessage);
            //LOGGER.debug("Sent email to User '{}'", sender.getTo());
        } catch (MailException | MessagingException e) {
            //LOGGER.warn("Email could not be sent to user '{}'", sender.getTo(), e);
        }
    }

    private void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            //LOGGER.debug("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
        Locale locale = Locale.forLanguageTag(DEFAULT_LANGUAGE);
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, "http://localhost:8080");
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);

        sendEmail(EmailSenderDTO.builder()
                .content(content).subject(subject).to(user.getEmail())
                .isHtml(true).isMultipart(false)
                .build());
    }

    @Async
    public void sendLoginOTPEmail(User user) {
        //LOGGER.debug("Sending login OTP email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/OTPCodeEmail", "email.otp.verification");
    }
}

