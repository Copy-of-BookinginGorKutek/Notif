package com.b2.notification.service;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationRequest request);
    List<Notification> findByEmailUser(String emailUser);
    void delete(Integer id);
    Boolean isUserExist(String emailUser);
}
