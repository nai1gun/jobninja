package com.nailgun.jhtest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    private static final String PROP_SMTP_AUTH = "mail.smtp.auth";
    private static final String PROP_STARTTLS = "mail.smtp.starttls.enable";
    private static final String PROP_TRANSPORT_PROTO = "mail.transport.protocol";

    private final Logger log = LoggerFactory.getLogger(MailConfiguration.class);

    @Value("${mail.host:localhost}")
    private String host;
    @Value("${mail.port:0}")
    private Integer port;
    @Value("${mail.username:#{null}}")
    private String user;
    @Value("${mail.password:#{null}}")
    private String password;
    @Value("${mail.protocol:smtp}")
    private String protocol;
    @Value("${mail.tls:false}")
    private Boolean tls;
    @Value("${mail.auth:false}")
    private Boolean auth;

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        log.debug("Configuring mail server");

        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        if ("localhost".equals(host)) {
            log.warn("Warning! Your SMTP server is not configured. We will try to use one on localhost.");
            log.debug("Did you configure your SMTP settings in your application.yml?");
        }
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(password);

        Properties sendProperties = new Properties();
        sendProperties.setProperty(PROP_SMTP_AUTH, auth.toString());
        sendProperties.setProperty(PROP_STARTTLS, tls.toString());
        sendProperties.setProperty(PROP_TRANSPORT_PROTO, protocol);
        sender.setJavaMailProperties(sendProperties);
        return sender;
    }
}
