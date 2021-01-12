package com.dataart.playme.service.impl;

import com.dataart.playme.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailServiceImpl implements MailService {

    private static final String MAIL_PLACEHOLDER_REGEXP = "\\{\\{.+?}}";

    private final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private final Properties mailProperties;

    @Autowired
    public MailServiceImpl(Properties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public void sendThroughRemote(String recipient, String subject, String mailTemplateFilepath,
                                  Map<String, String> placeholders) throws IOException {
        try {
            Session session = Session.getDefaultInstance(mailProperties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    String user = mailProperties.getProperty("mail.smtp.user");
                    String password = mailProperties.getProperty("mail.smtp.password");
                    return new PasswordAuthentication(user, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailProperties.getProperty("mail.smtp.user")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            String content = buildContent(mailTemplateFilepath, placeholders);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.error(String.format("Mail was not sent to %s", recipient));
            e.printStackTrace();
        }
    }

    private String buildContent(String mailTemplateFilepath, Map<String, String> placeholders) throws IOException {
        String mailTemplate = getMailTemplate(mailTemplateFilepath);
        StringBuilder mailBuilder = new StringBuilder(mailTemplate);
        Pattern pattern = Pattern.compile(MAIL_PLACEHOLDER_REGEXP);
        Matcher matcher = pattern.matcher(mailBuilder);

        while (matcher.find()) {
            String found = matcher.group();
            String placeholderName = found.substring(2, found.length() - 2);

            int startIndex = matcher.start();
            mailBuilder.replace(startIndex, startIndex + found.length(), placeholders.get(placeholderName));
        }

        return mailBuilder.toString();
    }

    public String getMailTemplate(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader reader = new BufferedReader(fileReader);
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }
        return builder.toString();
    }
}
