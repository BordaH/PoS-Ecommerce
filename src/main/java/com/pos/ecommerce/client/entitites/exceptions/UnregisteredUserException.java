package com.pos.ecommerce.client.entitites.exceptions;

import com.pos.ecommerce.client.constants.MessageConstants;

public class UnregisteredUserException extends LoginException {

    public UnregisteredUserException(){
        super(MessageConstants.UNREGISTERED_USER);
    }
}
