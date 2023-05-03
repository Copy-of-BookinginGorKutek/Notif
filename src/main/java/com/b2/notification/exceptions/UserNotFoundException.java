package com.b2.notification.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String emailUser){
        super("Pengguna dengan email " + emailUser + " tidak ditemukan");
    }
}
