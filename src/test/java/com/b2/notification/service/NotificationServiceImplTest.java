package com.b2.notification.service;

import com.b2.notification.model.Notification;
import com.b2.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import java.net.URL;
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

    @Mock
    private RestTemplate restTemplate;


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
        Notification first = new Notification(1, "abc@gmail.com", "Status terbaru Anda adalah menunggu konfirmasi", time);
        Notification second = new Notification(2, "abc@gmail.com", "Status terbaru Anda adalah terkonfirmasi", time);
        notificationList.add(first);
        notificationList.add(second);

        when(repository.findAllByEmailUser("abc@gmail.com")).thenReturn(notificationList);
        List<Notification> actualNotifications = service.findByEmailUser("abc@gmail.com");

        verify(repository, times(1)).findAllByEmailUser("abc@gmail.com");
        assertEquals(2, actualNotifications.size());
    }
}
