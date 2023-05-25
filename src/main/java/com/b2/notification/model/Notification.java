package com.b2.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="_notification")
@Generated
public class Notification {
    @Id
    @GeneratedValue
    private Integer id;
    private String emailUser;
    private String message;
    private LocalDateTime timestamp;
}
