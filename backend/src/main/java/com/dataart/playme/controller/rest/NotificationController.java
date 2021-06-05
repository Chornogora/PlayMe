package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Notification;
import com.dataart.playme.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getNotifications(@CurrentMusician Musician musician) {
        return notificationService.getNotifications(musician);
    }

    @PostMapping(path = "/_read")
    public Notification readNotification(@RequestBody Notification notification) {
        return this.notificationService.markRead(notification);
    }
}
