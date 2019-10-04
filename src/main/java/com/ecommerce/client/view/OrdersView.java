package com.ecommerce.client.view;

import com.ecommerce.client.presenter.OrdersPresenter;
import com.ecommerce.dominio.HeaderConstanst;
import com.ecommerce.dominio.Order;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

public class OrdersView implements OrdersPresenter.Display {

    private FlowPanel rootPanel;
    private FlowPanel panelDetail;
    private Button buttonLoad;
    private CellTable<Order> dataGrid;
    private ListDataProvider<Order> listDataProvider;
    private SingleSelectionModel<Order> selectionModel;
    private TextBox textBoxSearch;

    public OrdersView(){
        rootPanel = new FlowPanel();
        buttonLoad = new Button("Cargar pedido");
        buttonLoad.setType(ButtonType.PRIMARY);
        buttonLoad.setSize(ButtonSize.SMALL);
        Legend heading = new Legend();
        heading.setText("Pedidos");
        heading.setMarginLeft(4);
        rootPanel.add(heading);
        rootPanel.add(createOrdersTable());
        panelDetail = new FlowPanel();
        rootPanel.add(panelDetail);
    }

    private IsWidget createOrdersTable() {
        Panel panel = new Panel();
        PanelBody panelBody = new PanelBody();
        dataGrid = new CellTable<>();
        TextColumn<Order> colCode = new TextColumn<Order>() {
            @Override
            public String getValue(Order object) {
                return object.getCode();
            }
        };

        TextColumn<Order> colUser = new TextColumn<Order>() {
            @Override
            public String getValue(Order object) {
                return object.getName();
            }
        };
        TextColumn<Order> colTotal = new TextColumn<Order>() {
            @Override
            public String getValue(Order object) {
                return "$"+object.getTotal();
            }
        };

        dataGrid.addColumn(colCode, HeaderConstanst.CODE);
        dataGrid.addColumn(colUser, HeaderConstanst.CLIENTE);
        dataGrid.addColumn(colTotal, HeaderConstanst.TOTAL);
        dataGrid.setBordered(true);
        dataGrid.setCondensed(true);
        dataGrid.setHover(true);
        dataGrid.setStriped(true);
        dataGrid.setHeight("50%");
        listDataProvider = new ListDataProvider<>();
        listDataProvider.addDataDisplay(dataGrid);
        dataGrid.setLoadingIndicator(new ProgressBar());
        selectionModel = new SingleSelectionModel<Order>();
        dataGrid.setSelectionModel(selectionModel);
        panelBody.add(createaPanelSearch());
        panelBody.add(dataGrid);
        PanelFooter panelFooter = new PanelFooter();
        panelFooter.add(buttonLoad);
        panel.add(panelBody);
        panel.add(panelFooter);
        return panel;
    }

    private IsWidget createaPanelSearch() {
        NavbarNav container = new NavbarNav();
        textBoxSearch = new TextBox();
        textBoxSearch.setPlaceholder("Ingrese codigo del pedido");
        NavbarForm navbarForm = new NavbarForm();
        navbarForm.add(textBoxSearch);
        container.add(navbarForm);
        return container;
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }

    @Override
    public void showOrdes(FlowPanel flowPanel) {
        rootPanel.add(flowPanel);
    }

    @Override
    public ListDataProvider<Order> getListDataProvider() {
        return listDataProvider;
    }

    @Override
    public CellTable<Order> getCellTableOrders() {
        return dataGrid;
    }

    @Override
    public SingleSelectionModel<Order> getSelectionModelOrder() {
        return selectionModel;
    }

    @Override
    public void showOrderDetail(OrderView orderView) {
        panelDetail.clear();
        panelDetail.add(orderView);
    }

    @Override
    public TextBox getTextBoxSearch() {
        return textBoxSearch;
    }

    @Override
    public Button getButtonLoad() {
        return buttonLoad;
    }
}
