package com.pos.ecommerce.client.presenter;

import com.pos.ecommerce.client.*;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.events.OrdersEvent;
import com.pos.ecommerce.client.view.OrderView;
import com.pos.ecommerce.client.view.OrdersView;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.Collections;
import java.util.List;

public class OrdersPresenter implements Presenter {

    private final OrdersView display;
    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;

    public interface Display {
        Widget asWidget();

        void showOrdes(FlowPanel flowPanel);

        ListDataProvider<OrderDTO> getListDataProvider();

        CellTable<OrderDTO> getCellTableOrders();

        SingleSelectionModel<OrderDTO> getSelectionModelOrder();

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
        display.getCellTableOrders().addDomHandler(event -> {
            if (display.getSelectionModelOrder().getSelectedObject().getConfirm()){
                display.getButtonLoad().setEnabled(false);
            }else {
                display.getButtonLoad().setEnabled(true);
            }
            display.showOrderDetail(new OrderView(display.getSelectionModelOrder().getSelectedObject()));
        },DoubleClickEvent.getType());
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
                loadOrder(display.getSelectionModelOrder().getSelectedObject());
            }
        });
    }

    private void loadOrder(OrderDTO order) {
        rpcEcommerce.confirmOrder(order, new AsyncCallback<OrderDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                    caught.printStackTrace();
            }

            @Override
            public void onSuccess(OrderDTO result) {
                Modal modal = new Modal();
                ModalBody modalBody = new ModalBody();
                modal.add(modalBody);
                modal.setSize(ModalSize.SMALL);
                if (result.getConfirm()){
                    Paragraph paragraph = new Paragraph("El pedido "+result.getCode()+ " se ha confirmado");
                    modalBody.add(paragraph);
                }else {
                    Paragraph paragraph = new Paragraph("No se pudo confirmar el pedido");
                    modalBody.add(paragraph);
                }
                modal.show();
                modal.addHideHandler(e->eventBus.fireEvent(new OrdersEvent()));
            }
        });
    }

    private void search(String value) {
        rpcEcommerce.getOrder(value, new AsyncCallback<List<OrderDTO>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<OrderDTO> result) {
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
        rpcEcommerce.getOrders(new AsyncCallback<List<OrderDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<OrderDTO> result) {
                display.getListDataProvider().setList(sortOrders(result));
            }
        });
    }

    private List<OrderDTO> sortOrders(List<OrderDTO> result) {
        Collections.sort(result, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return result;
    }

}
