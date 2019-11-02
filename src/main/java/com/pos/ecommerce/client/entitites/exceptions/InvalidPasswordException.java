package com.pos.ecommerce.client.entitites.exceptions;

import com.pos.ecommerce.client.constants.MessageConstants;

public class InvalidPasswordException extends LoginException{
    public InvalidPasswordException() {
        super(MessageConstants.PASSWORD_INVALID);
    }
}
