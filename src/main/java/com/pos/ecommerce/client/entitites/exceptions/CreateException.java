package com.pos.ecommerce.client.entitites.exceptions;

public class CreateException extends Throwable {
    public CreateException(Exception e,String msg) {
        super(msg,e);
    }

    public CreateException() {
    }
}
