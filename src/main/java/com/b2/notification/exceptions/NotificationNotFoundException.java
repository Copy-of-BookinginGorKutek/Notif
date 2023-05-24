package com.b2.notification.exceptions;

import lombok.Generated;

@Generated
public class NotificationNotFoundException extends RuntimeException{
    public NotificationNotFoundException(Integer id){
        super("Notifikasi dengan ID " + id + " tidak ditemukan");
    }
}
