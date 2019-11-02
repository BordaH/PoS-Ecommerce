package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class OrdersEvent extends GwtEvent<OrdersEventHandler> {
    public static final Type<OrdersEventHandler> TYPE = new Type<>();

    public OrdersEvent() {
    }

    @Override
    public Type<OrdersEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(OrdersEventHandler handler) {
        handler.doOrders(this);
    }

}
