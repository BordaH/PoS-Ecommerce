package com.pos.ecommerce.client.presenter;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.server.Message;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pos.ecommerce.client.EcommerceServiceAsync;
import com.pos.ecommerce.client.constants.MessageConstants;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.entitites.exceptions.ProductException;
import com.pos.ecommerce.client.view.ShowMessage;
import com.pos.ecommerce.client.view.StockView;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.List;

public class StockPresenter implements Presenter{
    private final StockView display;
    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;

    public interface Display {

        Widget asWidget();

        TextBox getTextBoxSearch();

        Button getButtonAdd();

        CellTable<ItemDTO> getCellTable();

        ListDataProvider<ItemDTO> getListDataProvider();

        SingleSelectionModel<ItemDTO> getSelectionModel();

        void showDetail(ItemDTO itemDTO, boolean b);

        Button getButtonSave();

        Form getForm();

        TextBox getCodeTextBox();

        TextBox getStockTextBox();

        TextBox getDescriptionTextBox();

        TextBox getPriceTextBox();

        Button getButtoCreate();
    }

    public StockPresenter(StockView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {

        display.getTextBoxSearch().addKeyUpHandler(e->{
            String value = display.getTextBoxSearch().getValue();
            if (e.getNativeKeyCode()== KeyCodes.KEY_ENTER && !value.isEmpty()){
                search(value.trim());
            }
        });
        display.getButtonAdd().addClickHandler(e->{
            display.getButtonSave().setVisible(false);
            display.getButtoCreate().setVisible(true);
            display.showDetail(null,false);
        });
        display.getCellTable().addDomHandler(event ->
                showDetail(display.getSelectionModel().getSelectedObject()),DoubleClickEvent.getType());
        display.getButtoCreate().addClickHandler(e-> {
            if (display.getForm().validate()){
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setCode(display.getCodeTextBox().getValue());
                itemDTO.setQuantity(Integer.valueOf(display.getStockTextBox().getValue()));
                itemDTO.setName(display.getDescriptionTextBox().getValue());
                itemDTO.setPrice(Double.valueOf(display.getPriceTextBox().getValue()));
                createProduct(itemDTO);
            }
        });
        display.getButtonSave().addClickHandler(e->{
            if (display.getForm().validate()){
                ItemDTO selectedObject = display.getSelectionModel().getSelectedObject();
                selectedObject.setQuantity(Integer.valueOf(display.getStockTextBox().getValue()));
                selectedObject.setName(display.getDescriptionTextBox().getValue());
                selectedObject.setPrice(Double.valueOf(display.getPriceTextBox().getValue()));
                updateProduct(selectedObject);
            }
        });
    }

    private void createProduct(ItemDTO itemDTO) {
        try {
            rpcEcommerce.createNewProduct(itemDTO, new AsyncCallback<ItemDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                    ShowMessage.show(caught.getMessage());
                }

                @Override
                public void onSuccess(ItemDTO result) {
                    if (result!=null){
                        ShowMessage.show(MessageConstants.CREATE_PRODUCTO);
                    }
                }
            });
        } catch (ProductException e) {
            ShowMessage.show(e.getMessage());
        }
    }

    private void showDetail(ItemDTO itemDTO) {
        display.getButtonSave().setVisible(true);
        display.getButtoCreate().setVisible(false);
        display.showDetail(itemDTO,true);

    }

    private void updateProduct(ItemDTO itemDTO) {
        try {
            rpcEcommerce.updateProduct(itemDTO, new AsyncCallback<ItemDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                    ShowMessage.show(caught.getMessage());
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(ItemDTO result) {
                    if (result!=null){
                        ShowMessage.show(MessageConstants.UPDATE_PRODUCTO);
                        search(display.getTextBoxSearch().getValue());
                    }else {
                        ShowMessage.show(MessageConstants.FAILED_UPDATE_PRODUCTO);
                    }
                }
            });
        } catch (ProductException e) {
            ShowMessage.show(e.getMessage());
        }
    }


    private void search(String code) {
        rpcEcommerce.getStockFor(code, new AsyncCallback<List<ItemDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                ShowMessage.show(caught.getMessage());
            }

            @Override
            public void onSuccess(List<ItemDTO> result) {
                if (result!=null && !result.isEmpty()){
                    display.getListDataProvider().getList().clear();
                    display.getListDataProvider().setList(result);
                }else {
                    ShowMessage.show(MessageConstants.NOT_FOUND_PRODUCTS+code);
                }
            }
        });
    }

    @Override
    public void go(HasWidgets widgets) {
        widgets.clear();
        widgets.add(display.asWidget());
    }
}
