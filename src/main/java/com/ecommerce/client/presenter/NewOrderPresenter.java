package com.ecommerce.client.presenter;

import com.ecommerce.client.EcommerceServiceAsync;
import com.ecommerce.client.events.AddInCartEvent;
import com.ecommerce.client.view.NewOrderView;
import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewOrderPresenter implements Presenter {

    private final Display display;
    private final EcommerceServiceAsync rpcEcommerce;
    private final HandlerManager eventBus;
    private List<Item> items = new ArrayList<>();

    public interface Display {
        Widget asWidget();
        Button getButtonSearch();
        TextBox getTextBoxSearch();
        void showItems(FlowPanel result);
        AnchorListItem getButtonCart();
    }

    public NewOrderPresenter(NewOrderView display, EcommerceServiceAsync rpcEcommerce, HandlerManager eventBus) {
        this.display = display;
        this.rpcEcommerce = rpcEcommerce;
        this.eventBus = eventBus;
        eventBus.addHandler(AddInCartEvent.TYPE, addInCartEvent -> updateCart());
        bind();
    }

    private void updateCart() {
        display.getButtonCart().setBadgeText(String.valueOf(items.size()));
    }

    private void bind() {
        display.getTextBoxSearch().addKeyDownHandler(e->{
            String value = display.getTextBoxSearch().getValue();
            if (e.getNativeKeyCode()== KeyCodes.KEY_ENTER && !value.isEmpty()){
                searchProduct(display.getTextBoxSearch().getValue());
            }
        });
        display.getButtonSearch().addClickHandler(e->searchProduct(display.getTextBoxSearch().getValue()));
        display.getButtonCart().addClickHandler(e->showCart());
    }

    private void showCart() {
        Modal modal = new Modal();
        modal.setTitle("Carrito");
        ModalBody modalBody = new ModalBody();
        modalBody.add(createItems(items,false));
        Button confirm = new Button("Continuar pedido");
        confirm.setType(ButtonType.INFO);
        confirm.setSize(ButtonSize.SMALL);
        confirm.addClickHandler(e->showFormInvited());
        modalBody.add(confirm);
        modal.add(modalBody);
        modal.show();
    }

    private void showFormInvited() {
        Modal modal = new Modal();
        ModalBody modalBody = new ModalBody();
        Form form = new Form();
        TextBox email = new TextBox();
        email.setPlaceholder("Email");
        TextBox dom = new TextBox();
        dom.setPlaceholder("Domicilio");
        TextBox phone = new TextBox();
        phone.setPlaceholder("Teléfono");
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Ingrese una nota para el vendedor");
        Button button = new Button("Enviar pedido");
        button.setType(ButtonType.PRIMARY);
        button.setSize(ButtonSize.SMALL);
        button.addClickHandler(e->sendOrder(email.getValue(),dom.getValue(),phone.getValue(),textArea.getValue(),items));
        FieldSet fieldSet = new FieldSet();
        fieldSet.add(email);
        fieldSet.add(dom);
        fieldSet.add(phone);
        fieldSet.add(textArea);
        fieldSet.add(button);
        form.add(fieldSet);
        modalBody.add(form);
        modal.add(modalBody);
        modal.show();
    }

    private void sendOrder(String email, String dom, String phone, String note, List<Item> items) {
        if (!email.isEmpty()&&!dom.isEmpty()&&!phone.isEmpty() && !items.isEmpty()){
            rpcEcommerce.createOrder(email,dom,phone,note,items, new AsyncCallback<Order>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(Order result) {
                    if (result!=null){
                        Window.alert("Pedido enviado.");
                    }else {
                        Window.alert("Error al enviar el pedido.");
                    }
                    Window.Location.reload();
                }
            });
        }
    }

    private void searchProduct(String text) {
        rpcEcommerce.getProduct(text, new AsyncCallback<List<Item>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Item> result) {
                 createItems(result,true);
            }
        });
    }

    private FlowPanel createItems(List<Item> result,boolean b) {
        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel= new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        result.stream().forEach(r->{
            ThumbnailPanel thumbnailPanel = new ThumbnailPanel();
            Caption caption = new Caption();
            Heading heading = new Heading(HeadingSize.H3);
            heading.setText(r.getCode().concat("-").concat(r.getName()));
            Paragraph paragraph = new Paragraph("Precio:$"+r.getPrice());
            Button button = new Button();
            InputGroup inputGroup = new InputGroup();
            InputGroupButton inputGroupButton = new InputGroupButton();
            Button add = new Button();
            add.setIcon(IconType.PLUS);
            add.setSize(ButtonSize.EXTRA_SMALL);
            add.setType(ButtonType.PRIMARY);
            inputGroupButton.add(add);
            InputGroupButton inputGroupButton1 = new InputGroupButton();
            Button sub = new Button();
            sub.setIcon(IconType.MINUS);
            sub.setSize(ButtonSize.EXTRA_SMALL);
            sub.setType(ButtonType.PRIMARY);
            inputGroupButton1.add(sub);
            IntegerBox cant = new IntegerBox();
            cant.setValue(r.getQuantity());
            inputGroup.add(inputGroupButton1);
            inputGroup.add(cant);
            inputGroup.add(inputGroupButton);
            inputGroup.setSize(InputGroupSize.SMALL);
            button.setSize(ButtonSize.EXTRA_SMALL);
            caption.add(heading);
            caption.add(paragraph);
            if (b){
                button.setType(ButtonType.PRIMARY);
                button.setText("Agregar a carrito");
                button.addClickHandler(e->{r.setQuantity(cant.getValue());addInCart(r);});
            }else {
                button.setType(ButtonType.DANGER);
                button.setText("Eliminar del carrito");
                button.addClickHandler(e->{removeOfCart(r);flowPanel.remove(thumbnailPanel);});
            }
            caption.add(cant);
            caption.add(button);
            thumbnailPanel.add(caption);
            flowPanel.add(thumbnailPanel);
        });
        display.showItems(flowPanel);
        return  flowPanel;
    }

    private void removeOfCart(Item r) {
        items.remove(r);
        eventBus.fireEvent(new AddInCartEvent());
    }

    private void addInCart(Item item) {
        Optional<Item> first = items.stream().filter(i -> i.getCode().equals(item.getCode())).findFirst();
        if (first.isPresent()){
            items.remove(first.get());
        }
        items.add(item);
        eventBus.fireEvent(new AddInCartEvent());
        Notify.notify("El producto se agregó al carrito");
    }

    @Override
    public void go(HasWidgets widgets) {
        widgets.clear();
        widgets.add(display.asWidget());
    }
}
