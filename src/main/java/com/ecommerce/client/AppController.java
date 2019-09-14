package com.ecommerce.client;

import com.ecommerce.client.events.MenuEvent;
import com.ecommerce.client.events.OrderEvent;
import com.ecommerce.client.events.OrdersEvent;
import com.ecommerce.client.presenter.MenuPresenter;
import com.ecommerce.client.presenter.NewOrderPresenter;
import com.ecommerce.client.presenter.OrdersPresenter;
import com.ecommerce.client.presenter.Presenter;
import com.ecommerce.client.view.MenuView;
import com.ecommerce.client.view.NewOrderView;
import com.ecommerce.client.view.OrdersView;
import com.ecommerce.constants.TokenConfiguration;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AppController implements ValueChangeHandler<String> {


    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;
    private String token;
    private Presenter presenter;

    public AppController(EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {
        History.addValueChangeHandler(this);
        eventBus.addHandler(MenuEvent.TYPE, menuEvent -> doMenu());
        eventBus.addHandler(OrdersEvent.TYPE,ordersEvent -> doOrders(ordersEvent.getActivity()));
        eventBus.addHandler(OrderEvent.TYPE,orderEvent -> doOrder(orderEvent.activity));
    }

    private void doOrders(VerticalPanel activity) {
        token= TokenConfiguration.ORDERS;
        presenter = new OrdersPresenter(new OrdersView(),rpcEcommerce,eventBus);
        presenter.go(activity);
    }

    private void doOrder(VerticalPanel activity) {
        token= TokenConfiguration.ORDER;
        presenter = new NewOrderPresenter(new NewOrderView(),rpcEcommerce,eventBus);
        presenter.go(activity);
    }


    private void doMenu() {
        token= TokenConfiguration.MENU;
        presenter = new MenuPresenter(new MenuView(),rpcEcommerce,eventBus);
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
