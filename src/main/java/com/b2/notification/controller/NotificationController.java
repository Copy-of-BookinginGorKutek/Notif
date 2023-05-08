package com.b2.notification.controller;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.model.Notification;
import com.b2.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }
    @GetMapping("/get/{email}")
    public ResponseEntity<List<Notification>> getAllNotificationsByEmail(@PathVariable String email){
        List<Notification> response = notificationService.findByEmailUser(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotificationBasedOnStatusId(@RequestBody NotificationRequest notificationRequest){
        Notification notification = notificationService.createNotification(notificationRequest);
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> markAsReadNotificationById(@PathVariable Integer id){
        notificationService.delete(id);
        return ResponseEntity.ok("Notification has been marked as read");
    }
}
