package com.ecommerce.dominio.exceptions;

import com.ecommerce.dominio.MessageConstants;

public class InvalidPasswordException extends LoginException{
    public InvalidPasswordException() {
        super(MessageConstants.PASSWORD_INVALID);
    }
}
