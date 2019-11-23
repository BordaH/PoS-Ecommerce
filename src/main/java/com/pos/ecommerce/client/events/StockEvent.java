package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class StockEvent extends GwtEvent<StockEventHandler> {
    public static final Type<StockEventHandler> TYPE = new Type<>();

    @Override
    public Type<StockEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StockEventHandler handler) {
        handler.doStock(this);
    }
}
