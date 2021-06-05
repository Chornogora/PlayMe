package com.dataart.playme.repository;

import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByRecipientOrderByRead(Musician recipient);
}
