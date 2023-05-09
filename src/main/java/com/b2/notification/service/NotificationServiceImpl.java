package com.b2.notification.service;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.exceptions.NotificationNotFoundException;
import com.b2.notification.exceptions.UserNotFoundException;
import com.b2.notification.model.Notification;
import com.b2.notification.model.StatusPembayaranNotif;
import com.b2.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(NotificationRequest request) {
        if (Boolean.FALSE.equals(isUserExist(request.getEmailUser()))){
            throw new UserNotFoundException(request.getEmailUser());
        }
        String message = getStatusPembayaranNotifById(request.getStatusId()).getMessage();
        Notification notification = Notification.builder()
                .emailUser(request.getEmailUser())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public List<Notification> findByEmailUser(String emailUser) {
        if (Boolean.FALSE.equals(isUserExist(emailUser))){
            throw new UserNotFoundException(emailUser);
        }
        return notificationRepository.findAllByEmailUser(emailUser);
    }

    @Override
    public void delete(Integer id) {
        if (notificationRepository.findById(id).isEmpty()){
            throw new NotificationNotFoundException(id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public Boolean isUserExist(String emailUser) {
        //TODO: Lakukan pemanggilan microservice untuk menentukan apakah user ada atau tidak
        return true;
    }

    public StatusPembayaranNotif getStatusPembayaranNotifById(Integer id){
        for (StatusPembayaranNotif status : StatusPembayaranNotif.values()){
            if (status.getId().equals(id)){
                return status;
            }
        }
        return null;
    }}