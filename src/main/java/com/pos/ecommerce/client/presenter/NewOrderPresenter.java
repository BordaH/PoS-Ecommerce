package com.pos.ecommerce.client.presenter;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pos.ecommerce.client.EcommerceServiceAsync;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.exceptions.CreateException;
import com.pos.ecommerce.client.entitites.exceptions.SaveOrderException;
import com.pos.ecommerce.client.events.OrderEvent;
import com.pos.ecommerce.client.events.OrdersEvent;
import com.pos.ecommerce.client.events.UpdateCartEvent;
import com.pos.ecommerce.client.view.ItemOrderView;
import com.pos.ecommerce.client.view.NewOrderView;
import com.pos.ecommerce.client.view.ShowMessage;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewOrderPresenter implements Presenter {

    protected Display display;
    protected EcommerceServiceAsync rpcEcommerce;
    protected HandlerManager eventBus;
    private OrderDTO order;

    public interface Display {
        Widget asWidget();
        Button getButtonSearch();
        TextBox getTextBoxSearch();
        void showItems(FlowPanel result);
        AnchorListItem getButtonCart();

        void setVisibleUsers(boolean visibleUsers);

        Button getButtonUsers();

        Modal getModalUsers();

        ListDataProvider<UserDTO> getListDataProvider();

        DataGrid<UserDTO> getDataGridUsers();

        SingleSelectionModel<UserDTO> getSelectionModelUser();

        void setDataClient(UserDTO user);

        TextBox getTextBoxSearchUser();

        Button getButtonMakeOrder();

        Button getButtonCancelOrder();

        Modal getModalCart();

        ListDataProvider<ItemDTO> getLisDataProviderItems();


        Column<ItemDTO, String> getColumnDelete();

        Column<ItemDTO,String> getColumnQuantity();

        CellTable<ItemDTO> getDataGridItems();

        void setItems(List<ItemDTO> items);

        TextBox getTextBoxDiscount();

        FormControlStatic getFormControlAmountDiscount();

        FormControlStatic getFormControlDiscount();

        FormControlStatic getFormControlTotal();

        void guest();

        void setTextsBoxs(boolean b);

        Button getButtonClearClient();

        void clearDataUser();

        TextBox getTextBoxName();

        TextBox getTextBoxEmail();

        TextBox getTextBoxPhone();

        TextBox getTextBoxDni();

        Heading getHeadinCart();

        Form getFormClient();

        TextBox getTextBoxDom();
    }
    public NewOrderPresenter() {

    }
    public NewOrderPresenter(NewOrderView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        this.order = new OrderDTO();
        bind();
    }

    public void updateCart(String message) {
        applyDiscount(String.valueOf(order.getDisount()));
        display.setItems(order.getItems());
        Double total = order.getTotal();
        display.getFormControlTotal().setText("$"+ total);
        display.getHeadinCart().setSubText("Total:$ "+ total);
        display.getFormControlAmountDiscount().setText("$"+order.getAmountDiscount());
        display.getFormControlDiscount().setText(String.valueOf(order.getDisount()));
        if (!message.isEmpty()){
            NotifySettings ns = NotifySettings.newSettings();
            ns.setDelay(2);
            Notify.notify(message,ns);
        }
    }
    protected boolean isNotSpecialKey(KeyPressEvent event) {
        return event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_DELETE &&
                event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_BACKSPACE &&
                event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_LEFT &&
                event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_RIGHT;
    }
    protected boolean emailIsValid(String email) throws Exception {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean matches = email.matches(regex);
        if (!matches){
            throw new Exception("Revise el mail ingresado");
        }
        return matches;
    }
    protected boolean isInputValid(String name) throws Exception {
        boolean b = name.matches(".*\\d.*");
        boolean b1 = name.isEmpty();
        if (b || b1){
            display.getFormClient().validate();
            throw new Exception("Revise los campos nombre y/o apellido");
        }
        return !b1 && !b;
    }
    protected void bind() {
        display.getButtonClearClient().addClickHandler(e->removeDataClient());
        display.getTextBoxDiscount().addKeyUpHandler(e->{
            if(!display.getTextBoxDiscount().getValue().isEmpty() && e.getNativeKeyCode()==KeyCodes.KEY_ENTER){
                applyDiscount(display.getTextBoxDiscount().getValue());
                eventBus.fireEvent(new UpdateCartEvent(""));
                display.getTextBoxDiscount().setText("");
            }
        });
        display.getColumnDelete().setFieldUpdater(new FieldUpdater<ItemDTO, String>() {
            @Override
            public void update(int index, ItemDTO object, String value) {
                order.removeItem(object);
                eventBus.fireEvent(new UpdateCartEvent("Producto eliminado"));
            }
        });
        display.getColumnQuantity().setFieldUpdater(new FieldUpdater<ItemDTO, String>() {
            @Override
            public void update(int index, ItemDTO object, String value) {
                order.getItems().get(index).setQuantity(Integer.valueOf(value));
                eventBus.fireEvent(new UpdateCartEvent(""));
            }
        });
        display.getButtonMakeOrder().addClickHandler(e->{
            showPaymentMethod();
        });
        display.getButtonCancelOrder().addClickHandler(e->{
            display.getModalCart().hide();
            cancelOrder();
        });
        display.getTextBoxSearchUser().addKeyUpHandler(e->{
                searchUser(display.getTextBoxSearchUser().getValue());
        });
        display.getDataGridUsers().addDomHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                if (display.getSelectionModelUser().getSelectedObject()!=null){
                    setDataClient(display.getSelectionModelUser().getSelectedObject());
                    display.setTextsBoxs(false);
                    display.getModalUsers().hide();
                }
            }
        },DoubleClickEvent.getType());
        display.getTextBoxSearch().addKeyDownHandler(e->{
            String value = display.getTextBoxSearch().getValue();
            if (e.getNativeKeyCode()== KeyCodes.KEY_ENTER && !value.isEmpty()){
                display.getButtonSearch().click();
            }
        });
        display.getButtonSearch().addClickHandler(e->{
            String value = display.getTextBoxSearch().getValue();
            if (!value.isEmpty()){
                searchProduct(display.getTextBoxSearch().getValue());
            }
        });
        display.getButtonCart().addClickHandler(e->display.getModalCart().show());
        display.getButtonUsers().addClickHandler(e->{
            showClients();
        });
    }

    private void showPaymentMethod() {
        Modal modal = new Modal();
        ModalBody modalBody = new ModalBody();
        ModalFooter modalFooter = new ModalFooter();
        Form form = new Form();
        FieldSet fieldSet = new FieldSet();
        FormControlStatic formControlStatic = new FormControlStatic();
        formControlStatic.setText("Total:");
        FormLabel  formLabel = new FormLabel();
        formLabel.setText("$"+order.getTotal());
        FormGroup formGroup = new FormGroup();
        formGroup.add(formControlStatic);
        formGroup.add(formLabel);
        Button onlyConfirm =new Button("Efectivo");
        onlyConfirm.addClickHandler(e->{modal.hide();sendOrder(order);});
        Button mp = new Button("MercadoPago");
        mp.addClickHandler(e->{modal.hide();sendOrderMP(order);});
        fieldSet.add(formGroup);
        form.add(fieldSet);
        modalBody.add(form);
        modalFooter.add(onlyConfirm);
        modalFooter.add(mp);

        modal.add(modalBody);
        modal.add(modalFooter);
        modal.show();
    }

    private void sendOrderMP(OrderDTO order) {
        if (checkData(order)) return;
        if(order.getUser()!=null && order.getItems()!=null && !order.getItems().isEmpty()){
            rpcEcommerce.sendOrderMP(order, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    ShowMessage.show(caught.getMessage());
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(String result) {
                    Window.alert(result);
                    Window.Location.replace(result);

                }
            });
        }
    }

    private void showClients() {
        display.getModalUsers().show();
    }

    private void removeDataClient() {
        order.setUser(null);
        display.clearDataUser();
    }

    private void applyDiscount(String value) {
        Integer integer = Integer.valueOf(value);
        order.setDisount(0);
        order.setAmountDiscount(0.00);
        double amountDiscount = integer*order.getTotal() / 100;
        order.setAmountDiscount(amountDiscount);
        order.setDisount(integer);

    }


    private UserDTO createUser(String nameValue, String emailValue, String docValue, String phoneValue, String value) {
        UserDTO user = new UserDTO();
        user.setFirstName(nameValue);
        user.setEmail(emailValue);
        user.setDni(docValue);
        user.setPhone(phoneValue);
        user.setGuest(true);
        user.setDom(value);
        return user;
    }

    protected void cancelOrder() {
        Modal modal = new Modal();
        ModalBody modalBody = new ModalBody();
        modalBody.add(new Paragraph("Seguro que quiere cancelar el pedido?"));
        ModalFooter modalFooter = new ModalFooter();
        Button ok = new Button("SI");
        ok.setType(ButtonType.PRIMARY);
        ok.setSize(ButtonSize.SMALL);
        Button not=new Button("NO");
        not.setSize(ButtonSize.SMALL);
        not.setType(ButtonType.DANGER);
        ok.addClickHandler(e->{
            modal.hide();
            eventBus.fireEvent(new OrderEvent(false));
        });
        not.addClickHandler(e->modal.hide());
        modalFooter.add(ok);
        modalFooter.add(not);
        modal.add(modalBody);
        modal.add(modalFooter);
        modal.show();
    }

    private void sendOrder(OrderDTO order) {
        if (checkData(order)) return;
        if(order.getUser()!=null && order.getItems()!=null && !order.getItems().isEmpty()){
            try {
                rpcEcommerce.sendOrder(order, new AsyncCallback<OrderDTO>() {
                    @Override
                    public void onFailure(Throwable caught) {
                            ShowMessage.show(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(OrderDTO result) {
                        if (result!=null){
                            Modal modal = new Modal();
                            modal.setTitle("¡Ecommerce!");
                            ModalBody modalBody = new ModalBody();
                            modalBody.add(new Paragraph("El pedido se envió y confirmó correctamente"));
                            modal.add(modalBody);
                            modal.addHideHandler(e->{
                                Window.Location.reload();
                                eventBus.fireEvent(new OrdersEvent());
                            });
                            modal.show();
                        }
                    }
                });
            } catch (CreateException e) {
                ShowMessage.show(e.getMessage());
            } catch (SaveOrderException e) {
                ShowMessage.show(e.getMessage());
            }
        }else {
            display.getModalCart().hide();
            showMessage("Revise que haya agregado un usuario o un producto al pedido.");
        }
    }

    private boolean checkData(OrderDTO order) {
        if (order.getUser()==null){
            try {
                String name = display.getTextBoxName().getValue();
                String email = display.getTextBoxEmail().getValue();
                String phone = display.getTextBoxPhone().getValue();
                if (isInputValid(name)){
                        order.setUser(createUser(name,email,display.getTextBoxDni().getValue(),phone,display.getTextBoxDom().getValue()));
                }
            } catch (Exception e) {
                ShowMessage.show(e.getMessage());
                return true;
            }
        }
        return false;
    }

    protected void showMessage(String s) {
        Modal modal = new Modal();
        modal.setTitle("E-Commerce");
        ModalBody modalBody = new ModalBody();
        modalBody.add(new Paragraph(s));
        modal.add(modalBody);
        modal.setClosable(true);
        modal.setDataKeyboard(true);
        modal.show();
    }

    private void searchUser(String value) {
        if (!value.isEmpty()){
            rpcEcommerce.getUser(value.trim(), new AsyncCallback<List<UserDTO>>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(List<UserDTO> result) {
                    if (result!=null){
                        display.getListDataProvider().getList().clear();
                        display.getListDataProvider().setList(result);
                    }
                }
            });
        }else {
            getUsers();
        }
    }

    private void setDataClient(UserDTO user) {
        order.setUser(user);
        display.setDataClient(user);
        Notify.notify("Usuario agregado");
    }

    protected void searchProduct(String value) {
        rpcEcommerce.getProduct(value.trim(), new AsyncCallback<List<ItemDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<ItemDTO> result) {
                if (result!=null && !result.isEmpty()){
                    createItems(result,true);
                }else if (result!=null){
                    showMessage("No se encontraron resultados para su busqueda.");
                }
            }
        });
    }

    protected void createItems(List<ItemDTO> result, boolean b) {
        FlowPanel row= new FlowPanel();
        result.stream().forEach(r->{
            FlowPanel flowPanel = new FlowPanel();
            ItemOrderView itemOrder = new ItemOrderView(r);
            itemOrder.getButton().setType(ButtonType.PRIMARY);
            itemOrder.getButton().setText("Agregar a carrito");
            itemOrder.getButton().addClickHandler(e->{
                r.setQuantity(Integer.valueOf(itemOrder.getTextBoxQuantity().getValue()));
                itemOrder.getTextBoxQuantity().setValue("1");
                addInCart(r);
            });
            itemOrder.addStyleName("col-sm-4 col-md-3");
            flowPanel.add(itemOrder);
            row.add(itemOrder);

        });
        display.showItems(row);
    }

    protected void addInCart(ItemDTO item) {
        Optional<ItemDTO> first = order.getItems().stream().filter(i -> i.getCode().equals(item.getCode())).findFirst();
        String s = "El producto "+ item.getName()+ " se agregó al carrito";
        if (first.isPresent()){
            s = "Se actualizó el producto "+item.getName();
            order.getItems().remove(first.get());
        }
        order.getItems().add(item);
       eventBus.fireEvent(new UpdateCartEvent(s));
    }

    @Override
    public void go(HasWidgets widgets) {
        widgets.clear();
        widgets.add(display.asWidget());
        display.setVisibleUsers(true);
        fetchData();
    }

    private void fetchData() {
        getUsers();
        order= new OrderDTO();
    }

    private void getUsers() {
        rpcEcommerce.getUsers(new AsyncCallback<List<UserDTO>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<UserDTO> result) {
                if (result!=null){
                    display.getListDataProvider().getList().clear();
                    display.getListDataProvider().setList(result);
                }
            }
        });
    }
}
