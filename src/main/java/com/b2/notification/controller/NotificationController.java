package com.b2.notification.controller;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.model.Notification;
import com.b2.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/get/{email}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Notification>> getAllNotificationsByEmail(@PathVariable String email){
        List<Notification> response = notificationService.findByEmailUser(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Notification> sendNotificationBasedOnStatusId(@RequestBody NotificationRequest notificationRequest){
        Notification notification = notificationService.createNotification(notificationRequest);
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> markAsReadNotificationById(@PathVariable Integer id){
        notificationService.delete(id);
        return ResponseEntity.ok("Notification has been marked as read");
    }
}
