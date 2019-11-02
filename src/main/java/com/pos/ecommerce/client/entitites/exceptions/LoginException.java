package com.pos.ecommerce.client.entitites.exceptions;

public class LoginException extends RuntimeException{
    public LoginException(String msg) {
        super(msg);
    }

    public LoginException() {
    }
}
