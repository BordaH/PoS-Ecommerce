package com.pos.ecommerce.client.view;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pos.ecommerce.client.constants.HeaderConstanst;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.presenter.OrdersPresenter;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.Date;

public class OrdersView implements OrdersPresenter.Display {

    private FlowPanel rootPanel;
    private FlowPanel panelDetail;
    private Button buttonLoad;
    private CellTable<OrderDTO> dataGrid;
    private ListDataProvider<OrderDTO> listDataProvider;
    private SingleSelectionModel<OrderDTO> selectionModel;
    private TextBox textBoxSearch;

    public OrdersView(){
        rootPanel = new FlowPanel();
        buttonLoad = new Button("Confirmar pedido");
        buttonLoad.setType(ButtonType.PRIMARY);
        buttonLoad.setSize(ButtonSize.SMALL);
        buttonLoad.setEnabled(false);
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
        TextColumn<OrderDTO> colDate = new TextColumn<OrderDTO>() {
            @Override
            public String getValue(OrderDTO object) {
                Date d = object.getDate();
                DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
                return format.format(d);
            }
        };
        TextColumn<OrderDTO> colCode = new TextColumn<OrderDTO>() {
            @Override
            public String getValue(OrderDTO object) {
                return String.valueOf(object.getCode());
            }
        };

        TextColumn<OrderDTO> colUser = new TextColumn<OrderDTO>() {
            @Override
            public String getValue(OrderDTO object) {
                return object.getUser().getFirstName();
            }
        };
        TextColumn<OrderDTO> colTotal = new TextColumn<OrderDTO>() {
            @Override
            public String getValue(OrderDTO object) {
                return "$"+object.getTotal();
            }
        };
        TextColumn<OrderDTO> colConfirm = new TextColumn<OrderDTO>() {
            @Override
            public String getValue(OrderDTO object) {
                return object.getConfirm()?"CONFIRMADA":"A CONFIRMAR";
            }
        };

        dataGrid.addColumn(colCode, HeaderConstanst.CODE);
        dataGrid.addColumn(colDate,HeaderConstanst.DATE);
        dataGrid.addColumn(colConfirm, HeaderConstanst.STATE);
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
        selectionModel = new SingleSelectionModel<OrderDTO>();
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
    public ListDataProvider<OrderDTO> getListDataProvider() {
        return listDataProvider;
    }

    @Override
    public CellTable<OrderDTO> getCellTableOrders() {
        return dataGrid;
    }

    @Override
    public SingleSelectionModel<OrderDTO> getSelectionModelOrder() {
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
