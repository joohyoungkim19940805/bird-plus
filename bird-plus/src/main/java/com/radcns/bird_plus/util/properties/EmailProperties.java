package com.radcns.bird_plus.util.properties;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * email configuration properties
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    private String from;

    private String baseUrl;
}