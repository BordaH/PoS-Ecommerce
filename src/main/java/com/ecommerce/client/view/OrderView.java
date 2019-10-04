package com.ecommerce.client.view;

import com.ecommerce.dominio.HeaderConstanst;
import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.FormType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.List;

public class OrderView extends Composite {
    public OrderView(Order o) {
        Panel panel = new Panel();
        PanelHeader panelHeader = new PanelHeader();
        panelHeader.setText("Pedido: "+o.getCode());
        panel.add(panelHeader);
        PanelBody panelBody = new PanelBody();
        panelBody.add(createFormGroup("Nombre y apellido:",o.getName()));
        panelBody.add(createFormGroup("Email:",o.getEmail()));
        panelBody.add(createFormGroup("Domicilio:",o.getDom()));
        panelBody.add(createFormGroup("Telefono:",o.getPhone()));
        panelBody.add(createFormGroup("Nota:",o.getNote()));
        panelBody.setWidth("100%");
        panelBody.add(createItems(o.getItems()));
        panel.add(panelBody);
        panel.setType(PanelType.INFO);
        initWidget(panel);
    }

    private IsWidget createItems(List<Item> items) {
        CellTable<Item> dataGrid = new CellTable<>();
        TextColumn<Item> colCode = new TextColumn<Item>() {
            @Override
            public String getValue(Item object) {
                return object.getCode();
            }
        };
        TextColumn<Item> colDescription = new TextColumn<Item>() {
            @Override
            public String getValue(Item object) {
                return object.getName();
            }
        };
        TextColumn<Item> colQuantity = new TextColumn<Item>() {
            @Override
            public String getValue(Item object) {
                return String.valueOf(object.getQuantity());
            }
        };
        TextColumn<Item> colPrice = new TextColumn<Item>() {
            @Override
            public String getValue(Item object) {
                return "$"+object.getPrice();
            }
        };

        dataGrid.addColumn(colCode, HeaderConstanst.CODE);
        dataGrid.addColumn(colDescription, HeaderConstanst.DESCRIPTION);
        dataGrid.addColumn(colQuantity, HeaderConstanst.QUANTITY);
        dataGrid.addColumn(colPrice, HeaderConstanst.PRICE);
        dataGrid.setBordered(true);
        dataGrid.setCondensed(true);
        dataGrid.setHover(true);
        dataGrid.setStriped(true);
        dataGrid.setHeight("50%");
        dataGrid.setRowData(items);
        return dataGrid;
    }

    private IsWidget createFormGroup(String field, String value) {
        Form group = new Form();
        group.setType(FormType.INLINE);
        FieldSet fieldSet = new FieldSet();
        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.setFor("form");
        formLabel.setMarginTop(6);
        formLabel.addStyleName("col-sm-6");
        FormControlStatic formControlStatic = new FormControlStatic();
        formControlStatic.setId("form");
        if (value.isEmpty()){
            formControlStatic.setText("SIN DEFINIR");
        }else {
            formControlStatic.setText(value);
        }
        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        flowPanel.add(formControlStatic);
        formControlStatic.addStyleName("col-sm-2");
        fieldSet.add(formLabel);
        fieldSet.add(formControlStatic);
        group.add(fieldSet);
//        group.setWidth("100%");
        return group;
    }
}
