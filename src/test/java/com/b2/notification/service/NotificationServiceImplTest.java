package com.b2.notification.service;

import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.exceptions.NotificationNotFoundException;
import com.b2.notification.model.Notification;
import com.b2.notification.model.StatusPembayaranNotif;
import com.b2.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void testDeleteFail(){
        when(repository.findById(3)).thenReturn(Optional.empty());
        assertThrows(NotificationNotFoundException.class, ()->service.delete(3));
        verify(repository, times(1)).findById(3);
    }

    @Test
    void testDeleteSuccess(){
        LocalDateTime time = LocalDateTime.of(2023,5,3,12,12,12);
        Notification notification = new Notification(1, "abc@gmail.com", "Status terbaru Anda adalah menunggu konfirmasi", time);
        when(repository.findById(1)).thenReturn(Optional.of(notification));
        service.delete(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void whenIdStatusPembayaranNotifIsValidThenReturnStatusPembayaranNotif(){
        StatusPembayaranNotif statusPembayaranNotif1 = service.getStatusPembayaranNotifById(1);
        assertNotNull(statusPembayaranNotif1);
        assertEquals(StatusPembayaranNotif.MENUNGGU_PEMBAYARAN, statusPembayaranNotif1);
        StatusPembayaranNotif statusPembayaranNotif2 = service.getStatusPembayaranNotifById(2);
        assertNotNull(statusPembayaranNotif2);
        assertEquals(StatusPembayaranNotif.MENUNGGU_KONFIRMASI, statusPembayaranNotif2);
        StatusPembayaranNotif statusPembayaranNotif3 = service.getStatusPembayaranNotifById(3);
        assertNotNull(statusPembayaranNotif3);
        assertEquals(StatusPembayaranNotif.TERKONFIRMASI, statusPembayaranNotif3);
        StatusPembayaranNotif statusPembayaranNotif4 = service.getStatusPembayaranNotifById(4);
        assertNotNull(statusPembayaranNotif4);
        assertEquals(StatusPembayaranNotif.BATAL, statusPembayaranNotif4);
    }

    @Test
    void whenIdStatusPembayaranNotifIsNotValidThenReturnNull(){
        StatusPembayaranNotif statusPembayaranNotif = service.getStatusPembayaranNotifById(5);
        assertNull(statusPembayaranNotif);
    }

    @Test
    void testCreateNotificationStatusId1(){
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .emailUser("user@test.com")
                .statusId(1)
                .build();
        Notification notification = service.createNotification(notificationRequest);
        assertEquals(StatusPembayaranNotif.MENUNGGU_PEMBAYARAN.getMessage(), notification.getMessage());

    }

    @Test
    void testCreateNotificationStatusId2(){
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .emailUser("user@test.com")
                .statusId(2)
                .build();
        Notification notification = service.createNotification(notificationRequest);
        assertEquals(StatusPembayaranNotif.MENUNGGU_KONFIRMASI.getMessage(), notification.getMessage());
        assertEquals("user@test.com", notification.getEmailUser());

    }
    @Test
    void testCreateNotificationStatusId3(){
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .emailUser("user@test.com")
                .statusId(3)
                .build();
        Notification notification = service.createNotification(notificationRequest);
        assertEquals(StatusPembayaranNotif.TERKONFIRMASI.getMessage(), notification.getMessage());
        assertEquals("user@test.com", notification.getEmailUser());

    }
    @Test
    void testCreateNotificationStatusId4(){
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .emailUser("user@test.com")
                .statusId(4)
                .build();
        Notification notification = service.createNotification(notificationRequest);
        assertEquals(StatusPembayaranNotif.BATAL.getMessage(), notification.getMessage());
        assertEquals("user@test.com", notification.getEmailUser());

    }

}
