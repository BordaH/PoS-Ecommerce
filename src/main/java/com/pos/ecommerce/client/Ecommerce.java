package com.pos.ecommerce.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pos.ecommerce.client.events.MenuEvent;
import com.pos.ecommerce.dao.impl.UserDAOImpl;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Ecommerce implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        final EcommerceServiceAsync rpcEcommerce = GWT.create(EcommerceService.class);
        final HandlerManager eventBus = new HandlerManager(null);
        AppController appViewer = new AppController(rpcEcommerce, eventBus);
        eventBus.fireEvent(new MenuEvent());
    }
}