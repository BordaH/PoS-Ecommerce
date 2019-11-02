package com.pos.ecommerce.client.view;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.pos.ecommerce.client.constants.HeaderConstanst;
import com.pos.ecommerce.client.dto.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.pos.ecommerce.client.dto.OrderDTO;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.FormType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.Date;
import java.util.List;

public class OrderView extends Composite {
    public OrderView(OrderDTO o) {

        Panel panel = new Panel();
        PanelHeader panelHeader = new PanelHeader();
        if (o.getConfirm()){
            panelHeader.setText("Pedido(CONFIRMADO): "+o.getCode());
            panel.setType(PanelType.INFO);
        }else {
            panelHeader.setText("Pedido(A CONFIRMAR): "+o.getCode());
            panel.setType(PanelType.DANGER);
        }
        panel.add(panelHeader);

        PanelBody panelBody = new PanelBody();
        Date d = o.getDate();
        DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
        panelBody.add(createFormGroup("Fecha:",format.format(d)));
        panelBody.add(createFormGroup("Nombre y apellido:",o.getUser().getFirstName()));
        panelBody.add(createFormGroup("Email:",o.getUser().getEmail()));
        panelBody.add(createFormGroup("Domicilio:",o.getUser().getDom()));
        panelBody.add(createFormGroup("Telefono:",o.getUser().getPhone()));
        panelBody.add(createFormGroup("Nota:",o.getNote()));
        panelBody.setWidth("100%");
        panelBody.add(createItems(o.getItems()));
        panelBody.add(createPanelTotalAndDiscount(o));
        panel.add(panelBody);
        initWidget(panel);
    }

    private IsWidget createPanelTotalAndDiscount(OrderDTO o) {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(createFormGroup("DESCUENTO(%):", String.valueOf(o.getDisount())));
        flowPanel.add(createFormGroup("MONTO DESCUENTO:","$"+o.getAmountDiscount()));
        flowPanel.add(createFormGroup("TOTAL:","$"+o.getTotal()));
        return flowPanel;
    }

    private IsWidget createItems(List<ItemDTO> items) {

        CellTable<ItemDTO> dataGrid = new CellTable<>();
        TextColumn<ItemDTO> colCode = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return object.getCode();
            }
        };
        TextColumn<ItemDTO> colDescription = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return object.getName();
            }
        };
        TextColumn<ItemDTO> colQuantity = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return String.valueOf(object.getQuantity());
            }
        };
        TextColumn<ItemDTO> colPrice = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return "$"+object.getPrice();
            }
        };
        TextColumn<ItemDTO> colTotal = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return "$"+object.getTotal();
            }
        };

        dataGrid.addColumn(colCode, HeaderConstanst.CODE);
        dataGrid.addColumn(colDescription, HeaderConstanst.DESCRIPTION);
        dataGrid.addColumn(colQuantity, HeaderConstanst.QUANTITY);
        dataGrid.addColumn(colPrice, HeaderConstanst.PRICE);
        dataGrid.addColumn(colTotal, HeaderConstanst.TOTAL);
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
        formLabel.addStyleName("col-sm-2");
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
