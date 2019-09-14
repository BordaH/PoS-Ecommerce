package com.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OrderEvent extends GwtEvent<OrderEventHandler> {


    public static final Type<OrderEventHandler> TYPE = new Type<>();
    public final VerticalPanel activity;

    public OrderEvent(VerticalPanel activity) {
        this.activity=activity;
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
