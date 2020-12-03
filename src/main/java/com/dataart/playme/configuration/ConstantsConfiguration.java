package com.dataart.playme.configuration;

import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

@Configuration
public class ConstantsConfiguration {

    private static final String CONSTANTS_FILE = "./src/main/resources/constants.properties";

    @Autowired
    public void loadConstants(){
        try (InputStream inputStream = new FileInputStream(CONSTANTS_FILE)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Constants.setProperties(properties);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
