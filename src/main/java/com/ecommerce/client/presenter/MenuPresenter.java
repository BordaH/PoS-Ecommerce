package com.ecommerce.client.presenter;

import com.ecommerce.client.EcommerceServiceAsync;
import com.ecommerce.client.events.OrderEvent;
import com.ecommerce.client.events.OrdersEvent;
import com.ecommerce.client.view.MenuView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;

public class MenuPresenter implements Presenter {

    private final MenuView display;
    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;


    public interface Display {
        Widget asWidget();

        Button getButtonLogin();

        TextBox getEmail();

        TextBox getPassword();

        void showMenu();

        Modal getModalLogin();

        AnchorListItem getButtonOrder();

        VerticalPanel getActivity();

        Button getButtonInvited();

        void invited();

        AnchorListItem getButtonLogOut();

        AnchorListItem getButtonOrders();
    }
    public MenuPresenter(MenuView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {
        display.getButtonInvited().addClickHandler(e->loginInvited());
        display.getButtonLogin().addClickHandler(e->login());
        display.getButtonOrder().addClickHandler(e->newOrder());
        display.getButtonOrders().addClickHandler(e->orders());
        display.getButtonLogOut().addClickHandler(e-> Window.Location.reload());
    }

    private void orders() {
        eventBus.fireEvent(new OrdersEvent(display.getActivity()));
    }

    private void loginInvited() {
        display.showMenu();
        display.invited();
        display.getModalLogin().hide();
        eventBus.fireEvent(new OrderEvent(display.getActivity()));
    }

    private void newOrder() {
        eventBus.fireEvent(new OrderEvent(display.getActivity()));
    }

    private void login() {
        rpcEcommerce.login(display.getEmail().getValue(),display.getPassword().getValue(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(String result) {
                display.getModalLogin().hide();
                display.showMenu();
            }
        });
    }

    @Override
    public void go(HasWidgets widgets) {
        widgets.clear();
        widgets.add(display.asWidget());
        rpcEcommerce.userOfsession(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean result) {
                    if (result){
                        display.getModalLogin().show();
                    }else {
                        display.showMenu();
                    }
            }
        });
    }
}
