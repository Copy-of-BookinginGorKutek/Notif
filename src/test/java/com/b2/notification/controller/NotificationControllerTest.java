package com.b2.notification.controller;

import com.b2.notification.Util;
import com.b2.notification.config.SecurityConfiguration;
import com.b2.notification.dto.NotificationRequest;
import com.b2.notification.model.Notification;
import com.b2.notification.service.NotificationServiceImpl;
import com.b2.notification.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfiguration.class)
@WebMvcTest(controllers = NotificationController.class)
class NotificationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private NotificationServiceImpl service;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    JwtUtils utils;


    Notification notification;
    Object bodyContent;

    String email;

    @BeforeEach
    void setUp() {
        email = "abc@gmail.com";
        LocalDateTime time = LocalDateTime.of(2023,5,3,12,12,12);
        notification = Notification.builder()
                .id(1)
                .emailUser(email)
                .message("Status terbaru Anda adalah menunggu konfirmasi")
                .timestamp(time)
                .build();
        bodyContent = new Object() {

            public final String emailUser = email;
            public final Integer statusId = 1;
        };

    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllReservation() throws Exception {
        List<Notification> allNotification = List.of(notification);

        when(service.findByEmailUser(email)).thenReturn(allNotification);

        mvc.perform(get(String.format("/api/v1/notification/get/%s", email))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllNotificationsByEmail"))
                .andExpect(jsonPath("$[0].id").value(notification.getId()));

        verify(service, atLeastOnce()).findByEmailUser(email);
    }


    @Test
    @WithMockUser(roles = "USER")
    void testDeleteNotification() throws Exception {
        mvc.perform(delete("/api/v1/notification/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("markAsReadNotificationById"));

        verify(service, atLeastOnce()).delete(any(Integer.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSendNotification() throws Exception {
        when(service.createNotification(any(NotificationRequest.class))).thenReturn(notification);

        mvc.perform(post("/api/v1/notification/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("sendNotificationBasedOnStatusId"))
                .andExpect(jsonPath("$.id").value(notification.getId()));
        verify(service, atLeastOnce()).createNotification(any(NotificationRequest.class));
    }
}
