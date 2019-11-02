package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface AddInCartEventHandler extends EventHandler {
    void addInCart(UpdateCart updateCart);
}
