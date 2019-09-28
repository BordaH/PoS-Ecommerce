package com.ecommerce.client;

import com.ecommerce.client.events.LoginEvent;
import com.ecommerce.client.events.MenuEvent;
import com.ecommerce.dominio.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
        /*rpcEcommerce.userOfsession(new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
               caught.printStackTrace();
            }

            @Override
            public void onSuccess(User result) {
           //     if (result==null){

              *//*  }else {
                    eventBus.fireEvent(new LoginEvent());
                }*//*
            }
        });*/
    }
}
