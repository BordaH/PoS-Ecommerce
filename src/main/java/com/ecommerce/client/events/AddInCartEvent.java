package com.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class AddInCartEvent extends GwtEvent<AddInCartEventHandler> {
    public static final Type<AddInCartEventHandler> TYPE = new Type<>();

    @Override
    public Type<AddInCartEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddInCartEventHandler handler) {
        handler.addInCart(this);
    }
}
