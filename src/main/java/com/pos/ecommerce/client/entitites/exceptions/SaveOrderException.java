package com.pos.ecommerce.client.entitites.exceptions;

public class SaveOrderException extends Throwable {
    public SaveOrderException(Exception ex, String s) {
        super(s, ex);
    }

    public SaveOrderException() {
    }
}
