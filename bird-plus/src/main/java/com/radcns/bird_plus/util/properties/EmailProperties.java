package com.radcns.bird_plus.util.properties;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author kim.joohyoung
 *
 * email configuration properties
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    private String from;

    private String baseUrl;
    
    private ForgotPasswordProperties forgotPassword;
    
    private AccountVerifyProperties accountVerify;
    
    public static interface EmailPropertiesTemplate{
    	String getTemplateName();
    	String getSubject();
    	void setTemplateName(String templateName);
    	void setSubject(String subject);
    }
    
    @Data
    @ToString
    @Configuration
    @ConfigurationProperties(prefix="mail.forgot-password")
    public static class ForgotPasswordProperties implements EmailPropertiesTemplate{
    	private String templateName;
    	private String subject;
    }
    
    @Data
    @ToString
    @Configuration
    @ConfigurationProperties(prefix="mail.account-verify")
    public static class AccountVerifyProperties implements EmailPropertiesTemplate{
    	private String templateName;
    	private String subject;
    }
    
}