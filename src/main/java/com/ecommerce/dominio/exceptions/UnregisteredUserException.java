package com.ecommerce.dominio.exceptions;

import com.ecommerce.dominio.MessageConstants;

public class UnregisteredUserException extends LoginException {

    public UnregisteredUserException(){
        super(MessageConstants.UNREGISTERED_USER);
    }
}
