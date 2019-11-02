package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateCart extends GwtEvent<AddInCartEventHandler> {
    public static final Type<AddInCartEventHandler> TYPE = new Type<>();
    private String message;

    public UpdateCart(String s) {
        message = s;
    }

    @Override
    public Type<AddInCartEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddInCartEventHandler handler) {
        handler.addInCart(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
