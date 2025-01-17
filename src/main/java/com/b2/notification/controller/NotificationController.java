package com.b2.notification.controller;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.model.Notification;
import com.b2.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }
    @Operation(summary = "Get all notifications by email")
    @GetMapping("/get/{email}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Notification>> getAllNotificationsByEmail(@PathVariable String email){
        List<Notification> response = notificationService.findByEmailUser(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create and send new notification")
    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequest notificationRequest){
        Notification notification = notificationService.createNotification(notificationRequest);
        return ResponseEntity.ok(notification);
    }

    @Operation(summary = "Delete existing notification by ID")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> markAsReadNotificationById(@PathVariable Integer id){
        notificationService.delete(id);
        return ResponseEntity.ok("Notification has been marked as read");
    }
}
