package com.ecommerce.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class MenuEvent extends GwtEvent<MenuEventHandler> {

    public static final Type<MenuEventHandler> TYPE = new Type<>();

    @Override
    public Type<MenuEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MenuEventHandler handler) {
        handler.doLogin(this);
    }
}
