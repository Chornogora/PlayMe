package com.dataart.playme.listener;

import com.dataart.playme.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

@WebListener
public class ConstantsConfigContextListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantsConfigContextListener.class);

    private static final String CONSTANTS_FILE = "./src/main/resources/constants.properties";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties properties = initializeProperties();
        Constants.setProperties(properties);
    }

    public static Properties initializeProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(CONSTANTS_FILE)) {
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            LOGGER.error("Can't initialize properties");
            throw new UncheckedIOException(e);
        }
    }
}
