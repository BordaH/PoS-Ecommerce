package com.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OrdersEvent extends GwtEvent<OrdersEventHandler> {
    public static final Type<OrdersEventHandler> TYPE = new Type<>();
    private VerticalPanel activity;

    public OrdersEvent(VerticalPanel activity) {

        this.activity = activity;
    }

    @Override
    public Type<OrdersEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(OrdersEventHandler handler) {
        handler.doOrders(this);
    }

    public VerticalPanel getActivity() {
        return activity;
    }
}
