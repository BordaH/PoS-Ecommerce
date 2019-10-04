package com.ecommerce.client.presenter;

import com.ecommerce.client.EcommerceServiceAsync;
import com.ecommerce.client.view.OrderView;
import com.ecommerce.client.view.OrdersView;
import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.List;

public class OrdersPresenter implements Presenter {

    private final OrdersView display;
    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;

    public interface Display {
        Widget asWidget();

        void showOrdes(FlowPanel flowPanel);

        ListDataProvider<Order> getListDataProvider();

        CellTable<Order> getCellTableOrders();

        SingleSelectionModel<Order> getSelectionModelOrder();

        void showOrderDetail(OrderView orderView);

        TextBox getTextBoxSearch();

        Button getButtonLoad();
    }


    public OrdersPresenter(OrdersView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {
        display.getCellTableOrders().addDomHandler(event -> display.showOrderDetail(new OrderView(display.getSelectionModelOrder().getSelectedObject())),DoubleClickEvent.getType());
        display.getTextBoxSearch().addKeyUpHandler(e->{
            String value = display.getTextBoxSearch().getValue();
            if (value.isEmpty()){
                getOrders();
            }else {
                search(value);
            }
        });
        display.getButtonLoad().addClickHandler(e->{
            if (display.getSelectionModelOrder().getSelectedObject()!=null){
                Window.alert("LOAD");
            }
        });
    }

    private void search(String value) {
        rpcEcommerce.getOrder(value, new AsyncCallback<List<Order>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<Order> result) {
                display.getListDataProvider().getList().clear();
                display.getListDataProvider().setList(result);
            }
        });
    }

    @Override
    public void go(HasWidgets widgets) {
        fetchData();
        widgets.clear();
        widgets.add(display.asWidget());
    }

    private void fetchData() {
        getOrders();
    }

    private void getOrders() {
        rpcEcommerce.getOrders(new AsyncCallback<List<Order>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Order> result) {
                display.getListDataProvider().setList(result);
            }
        });
    }

}
