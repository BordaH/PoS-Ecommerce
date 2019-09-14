package com.ecommerce.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface OrdersEventHandler extends EventHandler {
    void doOrders(OrdersEvent ordersEvent);
}
