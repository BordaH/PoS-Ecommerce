package com.ecommerce.dominio.exceptions;

public class LoginException extends RuntimeException{
    public LoginException(String msg) {
        super(msg);
    }

    public LoginException() {
    }
}
