package com.ecommerce.client.presenter;

import com.ecommerce.client.EcommerceServiceAsync;
import com.ecommerce.client.view.OrdersView;
import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.FormType;

import java.util.List;

public class OrdersPresenter implements Presenter {

    private final OrdersView display;
    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;

    public interface Display {
        Widget asWidget();

        void showOrdes(FlowPanel flowPanel);
    }


    public OrdersPresenter(OrdersView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {

    }

    @Override
    public void go(HasWidgets widgets) {
        fetchData();
        widgets.clear();
        widgets.add(display.asWidget());
    }

    private void fetchData() {
        rpcEcommerce.getOrders(new AsyncCallback<List<Order>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Order> result) {
                createOrders(result);
            }
        });
    }

    private void createOrders(List<Order> result) {
        FlowPanel flowPanel = new FlowPanel();
        result.stream().forEach(o->{
            ThumbnailPanel thumbnailPanel = new ThumbnailPanel();
            Caption caption = new Caption();
            caption.add(createFormGroup("Email:",o.getEmail()));
            caption.add(createFormGroup("Domicilio:",o.getDom()));
            caption.add(createFormGroup("Telefono:",o.getPhone()));
            caption.add(createFormGroup("Nota:",o.getNote()));
            thumbnailPanel.add(caption);
            thumbnailPanel.add(createItems(o.getItems()));
            flowPanel.add(thumbnailPanel);
        });
        display.showOrdes(flowPanel);
    }

    private IsWidget createItems(List<Item> items) {
        ListGroup listGroup = new ListGroup();
        items.stream().forEach(i->{
            ListGroupItem listGroupItem = new ListGroupItem();
            listGroupItem.setText(i.getName().concat("-").concat(i.getCode()).concat(" ").concat(String.valueOf(i.getPrice())));
            listGroup.add(listGroupItem);
        });
        return listGroup;
    }

    private IsWidget createFormGroup(String field, String value) {
        Form group = new Form();
        group.setType(FormType.INLINE);
        FieldSet fieldSet = new FieldSet();
        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.setFor("form");
        formLabel.setMarginTop(6);
        formLabel.addStyleName("col-sm-1");
        FormControlStatic formControlStatic = new FormControlStatic();
        formControlStatic.setId("form");
        if (value.isEmpty()){
            formControlStatic.setText("SIN DEFINIR");
        }else {
            formControlStatic.setText(value);
        }
        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        flowPanel.add(formControlStatic);
        formControlStatic.addStyleName("col-sm-5");
        fieldSet.add(formLabel);
        fieldSet.add(formControlStatic);
        group.add(fieldSet);
//        group.setWidth("100%");
        return group;
    }
}
