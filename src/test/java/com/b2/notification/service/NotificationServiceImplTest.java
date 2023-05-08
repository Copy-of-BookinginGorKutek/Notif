package com.b2.notification.service;

import com.b2.notification.model.Notification;
import com.b2.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {
    @InjectMocks
    private NotificationServiceImpl service;
    @Mock
    private NotificationRepository repository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        LocalDateTime time = LocalDateTime.of(2023,5,3,12,12,12);
        repository.save(new Notification(1, "abc@gmail.com", "Status terbaru Anda adalah menunggu konfirmasi", time));
        repository.save(new Notification(2, "abc@gmail.com", "Status terbaru Anda adalah terkonfirmasi", time));
    }

    @Test
    void testGetAllNotificationsByEmail(){
        List<Notification> notificationList = new ArrayList<>();
        LocalDateTime time = LocalDateTime.of(2023,5,3,12,12,12);
        notificationList.add(new Notification(1, "abc@gmail.com", "Status terbaru Anda adalah menunggu konfirmasi", time));
        notificationList.add(new Notification(2, "abc@gmail.com", "Status terbaru Anda adalah terkonfirmasi", time));

        when(repository.findAllByEmailUser("abc@gmail.com")).thenReturn(notificationList);
        List<Notification> actualNotifications = service.findByEmailUser("abc@gmail.com");

        verify(repository, times(1)).findAllByEmailUser("abc@gmail.com");
        assertEquals(2, actualNotifications.size());
    }
}
