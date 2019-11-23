package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface StockEventHandler extends EventHandler {
    void doStock(StockEvent stockEvent);
}
