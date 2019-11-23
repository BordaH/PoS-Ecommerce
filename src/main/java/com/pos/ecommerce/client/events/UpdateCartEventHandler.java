package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface UpdateCartEventHandler extends EventHandler {
    void doUpdate(UpdateCartEvent updateCartEvent);
}
