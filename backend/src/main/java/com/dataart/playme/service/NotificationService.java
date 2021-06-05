package com.dataart.playme.service;

import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications(Musician musician);

    Notification createNotification(Musician recipient, String text, String title);

    Notification deleteNotification(Notification notification, Musician musician);

    Notification markRead(Notification notification);
}
