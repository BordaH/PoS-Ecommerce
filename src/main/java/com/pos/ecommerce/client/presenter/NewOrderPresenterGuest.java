package com.pos.ecommerce.client.presenter;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.pos.ecommerce.client.EcommerceServiceAsync;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.events.UpdateCart;
import com.pos.ecommerce.client.view.ItemOrderView;
import com.pos.ecommerce.client.view.NewOrderView;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.base.TextBoxBase;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewOrderPresenterGuest extends NewOrderPresenter implements Presenter {


    private List<ItemDTO> items = new ArrayList<>();


    public NewOrderPresenterGuest(NewOrderView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        if (!eventBus.isEventHandled(UpdateCart.TYPE)){
            eventBus.addHandler(UpdateCart.TYPE, updateCart -> updateCart(updateCart.getMessage()));
        }
        bind();
    }

    protected void updateCart(String message) {
        display.setItems(items);
        display.getHeadinCart().setSubText("Total: $"+items.stream().mapToDouble(i->i.getTotal()).sum());
        if (!message.isEmpty()){
            Notify.notify(message);
        }
    }

    protected void bind() {
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
        display.getButtonCancelOrder().addClickHandler(e->{
            if (!items.isEmpty()){
                cancelOrder();
            }
        });
        display.getButtonMakeOrder().addClickHandler(e->{
            if (!items.isEmpty()){
                showFormInvited();
            }
        });
        display.getButtonCart().addClickHandler(e->display.getModalCart().show());
        display.getColumnDelete().setFieldUpdater(new FieldUpdater<ItemDTO, String>() {
            @Override
            public void update(int index, ItemDTO object, String value) {
                items.remove(index);
                eventBus.fireEvent(new UpdateCart("Producto " + object.getName()+" eliminado"));
            }
        });
        display.getColumnQuantity().setFieldUpdater(new FieldUpdater<ItemDTO, String>() {
            @Override
            public void update(int index, ItemDTO object, String value) {
                items.get(index).setQuantity(Integer.valueOf(value));
                eventBus.fireEvent(new UpdateCart("Se actualizo la cantidad de "+object.getName()+"a "+value));
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
            itemOrder.addStyleName("col-sm-4 col-md-3");
            flowPanel.add(itemOrder);
            row.add(flowPanel);
            if(b){
                itemOrder.getButton().addClickHandler(e->{
                    r.setQuantity(Integer.valueOf(itemOrder.getTextBoxQuantity().getValue()));
                    itemOrder.getTextBoxQuantity().setValue("1");
                    addInCart(r);
                });
            }else {
                itemOrder.getButton().addClickHandler(e->{
                    removeOfCart(r);
                  flowPanel.remove(itemOrder);
                });
            }


        });

        display.showItems(row);
    }
    private void showFormInvited() {
        Modal modal = new Modal();
        ModalBody modalBody = new ModalBody();
        Form form = new Form();
        FieldSet fieldSet = new FieldSet();

        TextBox name = new TextBox();
        name.setSize(InputSize.SMALL);
        createFormGroup("Nombre:",name,fieldSet);
        TextBox lastname = new TextBox();
        lastname.setSize(InputSize.SMALL);
        createFormGroup("Apellido:",lastname,fieldSet);
        TextBox email = new TextBox();
        email.setSize(InputSize.SMALL);
        createFormGroup("Email:",email,fieldSet);
        TextBox dom = new TextBox();
        dom.setSize(InputSize.SMALL);
        createFormGroup("Domicilio:",dom,fieldSet);
        TextBox phone = new TextBox();
        phone.setSize(InputSize.SMALL);
        createFormGroup("Teléfono:",phone,fieldSet);

        phone.addKeyPressHandler(event -> {
            if(isNotSpecialKey(event)){
                String c = event.getCharCode()+"";
                if(RegExp.compile("[^0-9]").test(c) || event.getCharCode()== KeyCodes.KEY_BACKSPACE)
                    phone.cancelKey();
            }
        });
        TextArea textArea = new TextArea();
        textArea.setSize(InputSize.SMALL);
        createFormGroup("Ingrese una nota para el vendedor",textArea,fieldSet);

        Button button = new Button("Enviar pedido");
        button.setType(ButtonType.PRIMARY);
        button.setSize(ButtonSize.SMALL);
        button.addClickHandler(e->sendOrder(name.getValue(),lastname.getValue(),email.getValue(),dom.getValue(),phone.getValue(),textArea.getValue(),items));
        form.add(fieldSet);
        modalBody.add(form);
        ModalFooter modalFooter = new ModalFooter();
        modalFooter.add(button);
        modal.add(modalBody);
        modal.add(modalFooter);
        modal.setTitle("Complete sus datos");
        modal.show();
    }

    private void createFormGroup(String field, TextBoxBase name, FieldSet fieldSet) {

        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        formLabel.setFor("form");
        formLabel.setMarginTop(5);
        formLabel.addStyleName("col-sm-5");
        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        flowPanel.add(name);
        name.addStyleName("col-sm-3 col-md-7");
        fieldSet.add(formLabel);
        fieldSet.add(name);
    }



    private void sendOrder(String name, String lastname, String email, String dom, String phone, String note, List<ItemDTO> items) {
        try {
            if ( isInputValid(name) &&  isInputValid(lastname) && emailIsValid(email) && !dom.isEmpty() && !phone.isEmpty() && !items.isEmpty()){
            OrderDTO order = new OrderDTO(new UserDTO(name,lastname,email,dom,phone),items,note);
            rpcEcommerce.sendOrderGuest(order, new AsyncCallback<OrderDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(OrderDTO result) {
                    if (result!=null){
                        Window.alert("Pedido enviado.");
                    }else {
                        Window.alert("Error al enviar el pedido.");
                    }
                    Window.Location.reload();
                }
            });
        }else {
            Window.alert("Complete los datos");
        }
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
    }



    protected void searchProduct(String text) {
        rpcEcommerce.getProduct(text, new AsyncCallback<List<ItemDTO>>() {
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


    protected void removeOfCart(ItemDTO r) {
        items.remove(r);
        eventBus.fireEvent(new UpdateCart("Se elimino el producto"));
    }

    protected void addInCart(ItemDTO item) {
        Optional<ItemDTO> first = items.stream().filter(i -> i.getCode().equals(item.getCode())).findFirst();
        String s = "El producto "+ item.getName()+ " se agregó al carrito";
        if (first.isPresent()){
            s = "Se actualizó el producto "+item.getName();
            items.remove(first.get());
        }
        items.add(item);
        eventBus.fireEvent(new UpdateCart(s));
    }

    @Override
    public void go(HasWidgets widgets) {
        widgets.clear();
        widgets.add(display.asWidget());
        display.guest();
    }
}
