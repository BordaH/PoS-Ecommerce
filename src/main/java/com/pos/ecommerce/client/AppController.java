package com.pos.ecommerce.client;

import com.google.gwt.user.client.Window;
import com.pos.ecommerce.client.constants.TokenConfiguration;
import com.pos.ecommerce.client.events.*;
import com.pos.ecommerce.client.events.MenuEvent;
import com.pos.ecommerce.client.events.OrderEvent;
import com.pos.ecommerce.client.events.OrdersEvent;
import com.pos.ecommerce.client.presenter.*;
import com.pos.ecommerce.client.view.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class AppController implements ValueChangeHandler<String> {


    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;
    private String token;
    private Presenter presenter;
    private HasWidgets rootPanel;

    public AppController(EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        bind();
      
    }

    private void bind() {
        History.addValueChangeHandler(this);
        eventBus.addHandler(MenuEvent.TYPE, menuEvent -> doMenu());
        eventBus.addHandler(LoginEvent.TYPE, loginEvent -> doLogin());
        eventBus.addHandler(OrdersEvent.TYPE,ordersEvent -> doOrders());
        eventBus.addHandler(StockEvent.TYPE, stockEvent -> stock());
        eventBus.addHandler(OrderEvent.TYPE,orderEvent -> doOrder(orderEvent.guest));
        eventBus.addHandler(UpdateCartEvent.TYPE, updateCart -> updateCart(updateCart.msg));
    }

    private void stock() {
        presenter = new StockPresenter(new StockView(),rpcEcommerce,eventBus);
        presenter.go(rootPanel);
    }

    private void updateCart(String msg) {
        ((NewOrderPresenter) presenter).updateCart(msg);
    }

    private void doLogin() {
        token= TokenConfiguration.MENU;
        MenuPresenter presenter = new MenuPresenter(new MenuView(),rpcEcommerce,eventBus, true);
        rootPanel = presenter.getActivity();
        this.presenter = presenter;
        presenter.go(RootPanel.get());
    }

    private void doOrders() {
        token= TokenConfiguration.ORDERS;
        presenter = new OrdersPresenter(new OrdersView(),rpcEcommerce,eventBus);
        presenter.go(rootPanel);
    }

    private void doOrder(boolean guest) {
        token= TokenConfiguration.ORDER;
        if (guest){
            presenter = new NewOrderPresenterGuest(new NewOrderView(),rpcEcommerce,eventBus);
        }else {
            presenter = new NewOrderPresenter(new NewOrderView(),rpcEcommerce,eventBus);
        }
        presenter.go(rootPanel);
    }


    private void doMenu() {
        token= TokenConfiguration.MENU;
        MenuPresenter presenter = new MenuPresenter(new MenuView(),rpcEcommerce,eventBus, true);
        rootPanel = presenter.getActivity();
        this.presenter = presenter;
        presenter.go(RootPanel.get());
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        presenter = PresenterConfiguration.getInstance().getPresenterFor(token);
        if(presenter!=null){
            presenter.go(RootPanel.get());
        }
    }
}
