package com.ecommerce.client.view;

import com.ecommerce.client.presenter.OrdersPresenter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

public class OrdersView implements OrdersPresenter.Display {

    private final FlowPanel rootPanel;

    public OrdersView(){
        rootPanel = new FlowPanel();
        Heading heading = new Heading(HeadingSize.H3);
        heading.setText("Pedidos");
        rootPanel.add(heading);
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }

    @Override
    public void showOrdes(FlowPanel flowPanel) {
        rootPanel.add(flowPanel);
    }
}
