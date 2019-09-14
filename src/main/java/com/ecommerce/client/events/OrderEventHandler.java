package com.ecommerce.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface OrderEventHandler extends EventHandler {
    void doOrder(OrderEvent orderEvent);
}
