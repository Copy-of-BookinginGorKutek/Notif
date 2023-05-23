package com.b2.notification.service;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.exceptions.NotificationNotFoundException;
import com.b2.notification.model.Notification;
import com.b2.notification.model.StatusPembayaranNotif;
import com.b2.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
@Service
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
    }
    @Override
    public Notification createNotification(NotificationRequest request) {
        String message = getStatusPembayaranNotifById(request.getStatusId()).getMessage();
        LocalDateTime localDateTimeUTC = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime localDateTimeConverted = localDateTimeUTC.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
                .toLocalDateTime();
        Notification notification = Notification.builder()
                .emailUser(request.getEmailUser())
                .message(message)
                .timestamp(localDateTimeConverted)
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public List<Notification> findByEmailUser(String emailUser) {
        return notificationRepository.findAllByEmailUser(emailUser);
    }

    @Override
    public void delete(Integer id) {
        if (notificationRepository.findById(id).isEmpty()){
            throw new NotificationNotFoundException(id);
        }
        notificationRepository.deleteById(id);
    }

    public StatusPembayaranNotif getStatusPembayaranNotifById(Integer id){
        for (StatusPembayaranNotif status : StatusPembayaranNotif.values()){
            if (status.getId().equals(id)){
                return status;
            }
        }
        return null;
    }}
