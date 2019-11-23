package com.pos.ecommerce.client.presenter;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.pos.ecommerce.client.EcommerceServiceAsync;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.exceptions.CreateException;
import com.pos.ecommerce.client.entitites.exceptions.SaveOrderException;
import com.pos.ecommerce.client.events.UpdateCartEvent;
import com.pos.ecommerce.client.view.ItemOrderView;
import com.pos.ecommerce.client.view.NewOrderView;
import com.pos.ecommerce.client.view.ShowMessage;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.base.TextBoxBase;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
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
        bind();
    }

    public void updateCart(String message) {
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
                eventBus.fireEvent(new UpdateCartEvent("Producto " + object.getName()+" eliminado"));
            }
        });
        display.getColumnQuantity().setFieldUpdater(new FieldUpdater<ItemDTO, String>() {
            @Override
            public void update(int index, ItemDTO object, String value) {
                items.get(index).setQuantity(Integer.valueOf(value));
                eventBus.fireEvent(new UpdateCartEvent("Se actualizo la cantidad de "+object.getName()+"a "+value));
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
        name.addValueChangeHandler(e->{
            form.validate();
        });
        name.addValidator(new ValidatorBlank());
        createFormGroup("Nombre:",name,fieldSet);
        TextBox lastname = new TextBox();
        lastname.setSize(InputSize.SMALL);
        lastname.addValueChangeHandler(e->{
            form.validate();
        });
        lastname.addValidator(new ValidatorBlank());
        createFormGroup("Apellido:",lastname,fieldSet);
        TextBox email = new TextBox();
        email.setSize(InputSize.SMALL);
        email.addValueChangeHandler(e->{
            form.validate();
        });
        email.addValidator(new Validator<String>() {
            @Override
            public int getPriority() {
                return Priority.MEDIUM;
            }

            @Override
            public List<EditorError> validate(Editor<String> editor, String s) {
                List<EditorError> result = new ArrayList<EditorError>();
                String valueStr = s == null ? "" : s.toString();
                if (valueStr.isEmpty()) {
                    result.add(new BasicEditorError(editor, s, "Complete este campo"));
                    return result;
                }
                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                boolean matches = s.matches(regex);
                if (!matches){
                    result.add(new BasicEditorError(editor, s, "El email no es valido."));
                }
                return result;
            }
        });
        createFormGroup("Email:",email,fieldSet);
        TextBox dom = new TextBox();
        dom.setSize(InputSize.SMALL);
        dom.addValueChangeHandler(e->{
            form.validate();
        });
        dom.addValidator(new ValidatorBlank());
        createFormGroup("Domicilio:",dom,fieldSet);
        TextBox phone = new TextBox();
        phone.addValueChangeHandler(e->{
            form.validate();
        });
        phone.setSize(InputSize.SMALL);
        createFormGroup("Teléfono:",phone,fieldSet);
        phone.addValidator(new ValidatorBlank());
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
        button.addClickHandler(e->{
            if (form.validate()){
                sendOrder(name.getValue(),lastname.getValue(),email.getValue(),dom.getValue(),phone.getValue(),textArea.getValue(),items);
            }
        });
        form.add(fieldSet);
        modalBody.add(form);
        ModalFooter modalFooter = new ModalFooter();
        modalFooter.add(button);
        modal.add(modalBody);
        modal.add(modalFooter);
        modal.setTitle("Complete sus datos");
        modal.show();
    }

    private String createFormGroup(String field, TextBoxBase name, FieldSet fieldSet) {

        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        flowPanel.add(name);

        FormGroup formGroup = new FormGroup();
        HelpBlock helpblock= new HelpBlock();
        formGroup.add(name);
        formGroup.add(helpblock);
        fieldSet.add(formLabel);
        fieldSet.add(formGroup);
        return field;
    }



    private void sendOrder(String name, String lastname, String email, String dom, String phone, String note, List<ItemDTO> items) {
        try {
            if ( isInputValid(name) &&  isInputValid(lastname) && emailIsValid(email)&& !items.isEmpty()){
            OrderDTO order = new OrderDTO(new UserDTO(name,lastname,email,dom,phone),items,note);
            rpcEcommerce.sendOrderGuest(order, new AsyncCallback<OrderDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(OrderDTO result) {
                    if (result!=null){
                        ShowMessage.show("Pedido enviado.");
                    }else {
                        ShowMessage.show("Error al enviar el pedido.");
                    }
                    ShowMessage.getModal().addHideHandler(e->{
                        Window.Location.reload();
                    });
                }
            });
        }else {
                ShowMessage.show("Complete los datos");
        }
        } catch (Exception e) {
            ShowMessage.show(e.getMessage());
            e.getCause().printStackTrace();
        } catch (SaveOrderException e) {
            ShowMessage.show(e.getMessage());
            e.getCause().printStackTrace();
        } catch (CreateException e) {
            ShowMessage.show(e.getMessage());
            e.getCause().printStackTrace();
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
        eventBus.fireEvent(new UpdateCartEvent("Se elimino el producto"));
    }

    protected void addInCart(ItemDTO item) {
        Optional<ItemDTO> first = items.stream().filter(i -> i.getCode().equals(item.getCode())).findFirst();
        String s = "El producto "+ item.getName()+ " se agregó al carrito";
        if (first.isPresent()){
            s = "Se actualizó el producto "+item.getName();
            items.remove(first.get());
        }
        items.add(item);
        eventBus.fireEvent(new UpdateCartEvent(s));
    }

    @Override
    public void go(HasWidgets widgets) {
        widgets.clear();
        widgets.add(display.asWidget());
        display.guest();
    }
}
