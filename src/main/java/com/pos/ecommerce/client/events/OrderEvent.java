package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class OrderEvent extends GwtEvent<OrderEventHandler> {


    public static final Type<OrderEventHandler> TYPE = new Type<>();
    public boolean guest;

    public OrderEvent(boolean guest) {
        this.guest = guest;
    }

    @Override
    public Type<OrderEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(OrderEventHandler handler) {
        handler.doOrder(this);
    }
}
