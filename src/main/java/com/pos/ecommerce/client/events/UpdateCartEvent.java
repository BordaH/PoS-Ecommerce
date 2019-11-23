package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateCartEvent extends GwtEvent<UpdateCartEventHandler> {
    public static final Type<UpdateCartEventHandler> TYPE = new Type<>();
    public final String msg;

    public UpdateCartEvent(String s) {
        msg= s;
    }

    @Override
    public Type<UpdateCartEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateCartEventHandler handler) {
        handler.doUpdate(this);
    }
}
