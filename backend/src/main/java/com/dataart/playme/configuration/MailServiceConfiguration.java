package com.dataart.playme.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MailServiceConfiguration {

    private static final String MAIL_CONSTANTS_FILE = "./src/main/resources/mail.properties";

    @Bean
    public Properties mailProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(MAIL_CONSTANTS_FILE));
        return properties;
    }
}
