package com.dataart.playme.service.impl;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Notification;
import com.dataart.playme.repository.NotificationRepository;
import com.dataart.playme.service.MailService;
import com.dataart.playme.service.NotificationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private static final String DEFAULT_MAIL_TEMPLATE = "./src/main/resources/mail_templates/default.html";

    private final NotificationRepository notificationRepository;

    private final MailService mailService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, MailService mailService) {
        this.notificationRepository = notificationRepository;
        this.mailService = mailService;
    }

    @Override
    public List<Notification> getNotifications(Musician musician) {
        return notificationRepository.findByRecipientOrderByRead(musician);
    }

    @Override
    public Notification createNotification(Musician recipient, String text, String title) {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setTitle(title);
        notification.setText(text);
        notification.setRecipient(recipient);
        Notification savedNotification = notificationRepository.save(notification);

        new Thread(new EmailNotifier(recipient, title, text)).start();

        return savedNotification;
    }

    @Override
    public Notification deleteNotification(Notification notification, Musician musician) {
        Notification dbNotification = notificationRepository.findById(notification.getId())
                .orElseThrow(() -> new NoSuchRecordException("No notifications with id " + notification.getId() + " found"));
        notificationRepository.delete(dbNotification);
        return dbNotification;
    }

    @Override
    public Notification markRead(Notification notification) {
        Notification dbNotification = notificationRepository
                .findById(notification.getId())
                .orElseThrow(() -> new NoSuchRecordException("Can't find notification"));
        dbNotification.setRead(true);
        return notificationRepository.save(dbNotification);
    }

    @AllArgsConstructor
    private class EmailNotifier implements Runnable {

        private final Musician recipient;

        private final String title;

        private final String text;

        @Override
        public void run() {
            try {
                if (recipient.isEmailNotifications()) {
                    mailService.sendThroughRemote(recipient.getUser().getEmail(), title,
                            DEFAULT_MAIL_TEMPLATE, Map.of("text", text));
                }
            } catch (IOException e) {
                LOGGER.error("Failed to send message throw email");
            }
        }
    }
}
