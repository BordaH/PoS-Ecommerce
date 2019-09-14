package com.ecommerce.client;

import com.ecommerce.client.events.MenuEvent;
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
        /*final Button button = new Button("Click me");
        final Label label = new Label();
        button.addClickHandler(event -> {
            if (label.getText().equals("")) {
                EcommerceService.App.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
            } else {
                label.setText("");
            }
        });

        RootPanel.get("slot1").add(button);
        RootPanel.get("slot2").add(label);*/
        final EcommerceServiceAsync rpcEcommerce = GWT.create(EcommerceService.class);
        final HandlerManager eventBus = new HandlerManager(null);
        AppController appViewer = new AppController(rpcEcommerce, eventBus);
        rpcEcommerce.userOfsession(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
               caught.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean result) {
                eventBus.fireEvent(new MenuEvent());
            }
        });
    }
}
