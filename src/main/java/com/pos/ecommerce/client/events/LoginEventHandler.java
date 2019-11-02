package com.pos.ecommerce.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface LoginEventHandler extends EventHandler {
    void doLogin(LoginEvent loginEvent);
}
