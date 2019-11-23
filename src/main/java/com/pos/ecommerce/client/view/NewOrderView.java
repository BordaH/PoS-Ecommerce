package com.pos.ecommerce.client.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.pos.ecommerce.client.constants.HeaderConstanst;
import com.pos.ecommerce.client.dto.*;
import com.pos.ecommerce.client.presenter.NewOrderPresenterGuest;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pos.ecommerce.client.presenter.ValidatorBlank;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.base.form.AbstractForm;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.ArrayList;
import java.util.List;

public class NewOrderView implements NewOrderPresenterGuest.Display {
    private Row panelActivity;
    private FlowPanel rootPanel;
    private TextBox textBoxSearch;
    private Button buttonSearch;
    private AnchorListItem cart;
    private Button users;
    private Modal modalUsers;
    private FormControlStatic nameUser;
    private FormControlStatic emailUser;
    private FormControlStatic domUser;
    private FormControlStatic phoneUser;
    private TextBox textBoxSearchUser;
    private DataGrid<UserDTO> dataGridUsers;
    private ListDataProvider<UserDTO> dataProviderUser;
    private SingleSelectionModel<UserDTO> selectionModelUser;
    private FormControlStatic dniUSer;
    private Modal modalCart;
    private TextBox nameUserCart;
    private TextBox emailUserCart;
    private TextBox domUserCart;
    private TextBox phoneUserCart;
    private TextBox dniUSerCart;
    private CellTable<ItemDTO> dataGridItems;
    private ListDataProvider<ItemDTO> dataProviderItems;
    private SingleSelectionModel selectionModelItems;
    private Button buttonMakeOrder;
    private Button buttonCancelOrder;
    private Column<ItemDTO, String> quantity;
    private Column<ItemDTO, String> delete;
    private TextBox textBoxDiscount;
    private FormControlStatic discountApply;
    private FormControlStatic amountDiscount;
    private FormControlStatic total;
    private FlowPanel panelClient;
    private FlowPanel panelAmountAndDiscount;
    private Button clearClient;
    private Heading heading;
    private Form formUser;
    private FieldSet fieldSetUser;
    private Form formAmount;
    private FieldSet fieldSetAmount;
    private HelpBlock helpBlockEmail;

    public NewOrderView(){
        initModalUsers();

        rootPanel = new FlowPanel();
        panelActivity = new Row();
        FlowPanel container = new FlowPanel();
        container.add(this::initCart);
        container.add(panelActivity);
        container.setWidth("100%");
        rootPanel.add(panelSearch());
        rootPanel.add(container);
    }

    private Panel initCart() {
        modalCart = new Modal();
        buttonMakeOrder = new Button("Confirmar orden");
        buttonCancelOrder = new Button("Cancelar orden");
        buttonCancelOrder.setType(ButtonType.DANGER);
        buttonCancelOrder.addStyleName("letra-Carrito");
        buttonMakeOrder.addStyleName("letra-Carrito");
        buttonMakeOrder.setType(ButtonType.PRIMARY);
        textBoxDiscount = new TextBox();
        textBoxDiscount.setSize(InputSize.SMALL);
        total = new FormControlStatic();
        total.setText("$0");
        total.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        discountApply = new FormControlStatic();
        discountApply.setText("0");
        discountApply.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        amountDiscount = new FormControlStatic();
        amountDiscount.setText("$0");
        amountDiscount.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        modalCart.setTitle("Carrito");
        modalCart.setSize(ModalSize.LARGE);
        Panel panel = new Panel();
        PanelBody modalBody = new PanelBody();
        modalBody.addStyleName("letra-Carrito");
        heading = new Heading(HeadingSize.H3);
        Icon icon = new Icon(IconType.SHOPPING_CART);
        icon.setPull(Pull.LEFT);
        heading.setText("Carrito");
        heading.add(icon);
        heading.setSubText("Total: $ 0");
        modalBody.add(heading);
        modalBody.add(createDataUserCart());
        modalBody.add(createTableItemsCart());
        modalBody.add(createPanelAmountAndDiscount());
        PanelHeader modalFooter = new PanelHeader();
        buttonMakeOrder.setMarginRight(3);
        buttonMakeOrder.setSize(ButtonSize.SMALL);
        buttonCancelOrder.setSize(ButtonSize.SMALL);
        modalFooter.add(buttonMakeOrder);
        modalFooter.add(buttonCancelOrder);
        modalFooter.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        panel.add(modalFooter);
        panel.add(modalBody);
        panel.setType(PanelType.DEFAULT);
        panel.setPull(Pull.LEFT);
        panel.setMarginLeft(5);
        panel.setMarginRight(5);
        panel.setWidth("400px");

        return panel;
    }

    private IsWidget createPanelAmountAndDiscount() {
        textBoxDiscount.addKeyPressHandler(event -> {
            if(isNotSpecialKey(event)){
                String c = event.getCharCode()+"";
                if(RegExp.compile("[^0-9]").test(c) || event.getCharCode()== KeyCodes.KEY_BACKSPACE)
                    textBoxDiscount.cancelKey();
            }
        });
        panelAmountAndDiscount = new FlowPanel();
        formAmount = new Form();
        fieldSetAmount = new FieldSet();
        fieldSetAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        formAmount.setType(FormType.INLINE);
        createFormGroup("Descuento a aplicar(%):",textBoxDiscount, fieldSetAmount);
        createFormGroup("Descuento aplicado(%):",discountApply, fieldSetAmount);
        createFormGroup("Monto descuento:",amountDiscount, fieldSetAmount);
        createFormGroup("Total:",total, fieldSetAmount);
        formAmount.add(fieldSetAmount);
        panelAmountAndDiscount.add(formAmount);
        return panelAmountAndDiscount;
    }
    private boolean isNotSpecialKey(KeyPressEvent event) {
        return event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_DELETE &&
                event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_BACKSPACE &&
                event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_LEFT &&
                event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_RIGHT;
    }
    private IsWidget createTableItemsCart() {
        dataGridItems = new CellTable<ItemDTO>();
        dataProviderItems = new ListDataProvider<ItemDTO>();
        selectionModelItems = new SingleSelectionModel();
        dataProviderItems.addDataDisplay(dataGridItems);
        dataGridItems.setSelectionModel(selectionModelItems);
        dataGridItems.setBordered(true);
        dataGridItems.setCondensed(true);
        dataGridItems.setHover(true);
        dataGridItems.setStriped(true);
        TextColumn<ItemDTO> name = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return object.getName();
            }
        };

        TextColumn<ItemDTO> code = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return object.getCode();
            }
        };

        TextColumn<ItemDTO> price = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                return "$"+object.getPrice();
            }
        };

        quantity = new Column<ItemDTO, String>(new EditTextCell()) {
            @Override
            public String getValue(ItemDTO object) {
                return String.valueOf(object.getQuantity());
            }
        } ;
        org.gwtbootstrap3.client.ui.gwt.ButtonCell buttonCellDelete = new org.gwtbootstrap3.client.ui.gwt.ButtonCell(IconType.REMOVE,ButtonType.DANGER,ButtonSize.EXTRA_SMALL);

        delete = new Column<ItemDTO, String>(buttonCellDelete) {
            @Override
            public String getValue(ItemDTO object) {
                return "";
            }
        };
        TextColumn<ItemDTO> total = new TextColumn<ItemDTO>() {
            @Override
            public String getValue(ItemDTO object) {
                    return "$"+object.getTotal();
            }
        };
        dataGridItems.addColumn(code, HeaderConstanst.CODE);
        dataGridItems.addColumn(name,HeaderConstanst.DESCRIPTION);
        dataGridItems.addColumn(quantity,HeaderConstanst.QUANTITY);
        dataGridItems.addColumn(price,HeaderConstanst.PRICE);
        dataGridItems.addColumn(total,HeaderConstanst.TOTAL);
        dataGridItems.addColumn(delete,"");
        dataGridItems.setWidth("100%");
        ScrollPanel scrollPanel = new ScrollPanel(dataGridItems);
        scrollPanel.setWidth("100%");
        return scrollPanel;
    }

    private IsWidget createDataUserCart() {
        nameUserCart = new TextBox();
        emailUserCart = new TextBox();
        helpBlockEmail = new HelpBlock();
        domUserCart = new TextBox();
        phoneUserCart = new TextBox();
        phoneUserCart.addKeyPressHandler(event -> {
            if(isNotSpecialKey(event)){
                String c = event.getCharCode()+"";
                if(RegExp.compile("[^0-9]").test(c) || event.getCharCode()== KeyCodes.KEY_BACKSPACE)
                    phoneUserCart.cancelKey();
            }
        });
        dniUSerCart = new TextBox();
        nameUserCart.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        emailUserCart.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        domUserCart.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        phoneUserCart.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        dniUSerCart.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        users = new Button();
        users.setIcon(IconType.USERS);
        users.setSize(ButtonSize.SMALL);
        users.setVisible(false);
        users.setMarginBottom(10);
        return createDataCart();
    }

    private void initModalUsers() {
        nameUser = new FormControlStatic();
        emailUser = new FormControlStatic();
        domUser = new FormControlStatic();
        phoneUser = new FormControlStatic();
        dniUSer = new FormControlStatic();
        modalUsers = new Modal();
        ModalBody modalBody = new ModalBody();
        modalUsers.setTitle("Usuarios");

        modalBody.add(createTableUsers());
        modalUsers.add(modalBody);
    }
    private IsWidget createDataCart() {
        panelClient = new FlowPanel();
        fieldSetUser = new FieldSet();
        formUser = new Form();
        formUser.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        nameUserCart.addKeyUpHandler(e->{
           if (e.getNativeKeyCode()==KeyCodes.KEY_TAB){
               formUser.validate();
           }
        });
        FormGroup formGroup = new FormGroup();
        HelpBlock h1 = new HelpBlock();
        formGroup.add(nameUserCart);
        formGroup.add(h1);
        formGroup.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        nameUserCart.addValidator(new ValidatorBlank());


        FormGroup formGroup1 = new FormGroup();
        HelpBlock h2 = new HelpBlock();
        formGroup1.add(domUserCart);
        formGroup1.add(h2);
        formGroup1.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        domUserCart.addKeyUpHandler(e->{
            if (e.getNativeKeyCode()==KeyCodes.KEY_TAB){
                formUser.validate();
            }
        });
        FormGroup formGroup3 = new FormGroup();
        HelpBlock h3 = new HelpBlock();
        formGroup3.add(dniUSerCart);
        formGroup3.add(h3);
        formGroup3.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        dniUSerCart.addKeyUpHandler(e->{
            if (e.getNativeKeyCode()==KeyCodes.KEY_TAB){
                formUser.validate();
            }
        });
        FormGroup formGroup4 = new FormGroup();
        HelpBlock h4 = new HelpBlock();
        formGroup4.add(emailUserCart);
        formGroup4.add(h4);
        formGroup4.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        emailUserCart.addValueChangeHandler(e->{
            formUser.validate();
        });
        emailUserCart.addValidator(new Validator<String>() {
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
        createFormGroup("Nombre y apellido:",formGroup,fieldSetUser);
        createFormGroup("Email:",formGroup4, fieldSetUser);
        createFormGroup("Documento:",formGroup3, fieldSetUser);
        createFormGroup("Domicilio:",formGroup1, fieldSetUser);

        formUser.add(fieldSetUser);

        panelClient.add(formUser);
        ButtonGroup buttonGroup = new ButtonGroup();
        clearClient = new Button();
        clearClient.setIcon(IconType.REMOVE);
        clearClient.setType(ButtonType.DANGER);
        clearClient.setSize(ButtonSize.SMALL);
        clearClient.setVisible(false);
        buttonGroup.add(users);
        buttonGroup.add(clearClient);
        Button v  =new Button();
        panelClient.add(buttonGroup);
        return panelClient;
    }

    private PanelBody createTableUsers() {
        PanelBody panelBody = new PanelBody();
        textBoxSearchUser = new TextBox();
        textBoxSearchUser.setPlaceholder("Ingrese nombre u otro campo para buscar");
        panelBody.add(textBoxSearchUser);
        dataGridUsers = new DataGrid<>();
        dataProviderUser = new ListDataProvider<UserDTO>();
        selectionModelUser = new SingleSelectionModel();
        dataProviderUser.addDataDisplay(dataGridUsers);
        dataGridUsers.setSelectionModel(selectionModelUser);
        dataGridUsers.setBordered(true);
        dataGridUsers.setCondensed(true);
        dataGridUsers.setHover(true);
        dataGridUsers.setStriped(true);
        TextColumn<UserDTO> userTextColumn = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO object) {
                return object.getFirstName()+" "+object.getLastName();
            }
        };
        TextColumn<UserDTO> email = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO object) {
                return object.getEmail();
            }
        };
        TextColumn<UserDTO> doc = new TextColumn<UserDTO>() {
            @Override
            public String getValue(UserDTO object) {
                return String.valueOf(object.getDni());
            };
        };
        dataGridUsers.addColumn(userTextColumn, HeaderConstanst.NOMBRE);
        dataGridUsers.addColumn(email, HeaderConstanst.EMAIL);
        dataGridUsers.addColumn(doc, HeaderConstanst.DOCUMENT);
        dataGridUsers.setWidth("100%");
        dataGridUsers.setHeight("300px");
        panelBody.add(dataGridUsers);
        return panelBody;
    }

    private IsWidget createFormGroup(String field, Widget formControlStatic,FieldSet fieldSet) {
        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        formLabel.setMarginTop(5);
        formLabel.addStyleName("col-sm-6");
        formControlStatic.addStyleName("col-sm-6");
        fieldSet.add(formLabel);
        fieldSet.add(formControlStatic);
        return fieldSet;
    }
    private Widget panelSearch() {
        Navbar navbar = new Navbar();
        NavbarHeader navbarHeader = new NavbarHeader();
        NavbarCollapseButton navBarCollapseButton = new NavbarCollapseButton();
        navBarCollapseButton.setDataTarget("#navbar-collapse");
        navbarHeader.add(navBarCollapseButton);

        NavbarCollapse navbarCollapse = new NavbarCollapse();
        navbarCollapse.setId("navbar-collapse");
        NavbarForm navbarForm = new NavbarForm();
        textBoxSearch = new TextBox();
        buttonSearch = new Button();
        cart = new AnchorListItem();
        cart.setIcon(IconType.SHOPPING_CART);
        cart.setBadgeText("0");
        cart.setPull(Pull.RIGHT);

        buttonSearch.setIcon(IconType.SEARCH);
        buttonSearch.setType(ButtonType.PRIMARY);
        textBoxSearch.setSize(InputSize.SMALL);
        textBoxSearch.setPlaceholder("Ingrese el nombre del producto");
        InputGroup inputGroup = new InputGroup();
        InputGroupButton inputGroupButton = new InputGroupButton();
        inputGroupButton.add(buttonSearch);
        inputGroup.add(textBoxSearch);
        inputGroup.add(inputGroupButton);
        inputGroup.setWidth("50%");
        navbarForm.add(inputGroup);
        navbarForm.setPull(Pull.RIGHT);
        navbarForm.setWidth("100%");
        navbarCollapse.add(navbarForm);
        navbar.add(navbarHeader);
        navbar.add(navbarCollapse);
        return navbar;
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }

    @Override
    public Button getButtonSearch() {
        return buttonSearch;
    }

    @Override
    public TextBox getTextBoxSearch() {
        return textBoxSearch;
    }

    @Override
    public void showItems(com.google.gwt.user.client.ui.FlowPanel flowPanel) {
        panelActivity.clear();
        panelActivity.add(flowPanel);
    }

    @Override
    public AnchorListItem getButtonCart() {
        return cart;
    }

    @Override
    public Button getButtonUsers() {
        return users;
    }

    @Override
    public Modal getModalUsers() {
        return modalUsers;
    }

    @Override
    public ListDataProvider<UserDTO> getListDataProvider() {
        return dataProviderUser;
    }

    @Override
    public DataGrid<UserDTO> getDataGridUsers() {
        return dataGridUsers;
    }

    @Override
    public SingleSelectionModel getSelectionModelUser() {
        return selectionModelUser;
    }

    @Override
    public void setDataClient(UserDTO user) {
        nameUser.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
        emailUser.setText(user.getEmail());
        domUser.setText(user.getDom());
        dniUSer.setText(String.valueOf(user.getDni()));
        phoneUser.setText(user.getPhone());
        nameUserCart.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
        emailUserCart.setText(user.getEmail());
        domUserCart.setText(user.getDom());
        dniUSerCart.setText(String.valueOf(user.getDni()));
        phoneUserCart.setText(user.getPhone());

    }

    @Override
    public TextBox getTextBoxSearchUser() {
        return textBoxSearchUser;
    }

    @Override
    public Button getButtonMakeOrder() {
        return buttonMakeOrder;
    }

    @Override
    public Button getButtonCancelOrder() {
        return buttonCancelOrder;
    }

    @Override
    public Modal getModalCart() {
        return modalCart;
    }

    @Override
    public ListDataProvider<ItemDTO> getLisDataProviderItems() {
        return dataProviderItems;
    }

    @Override
    public Column<ItemDTO, String> getColumnDelete() {
        return delete;
    }

    @Override
    public Column<ItemDTO, String> getColumnQuantity() {
        return quantity;
    }

    @Override
    public CellTable<ItemDTO> getDataGridItems() {
        return dataGridItems;
    }

    @Override
    public void setItems(List<ItemDTO> items) {
        dataProviderItems.getList().clear();
        dataProviderItems.getList().addAll(items);
        dataProviderItems.flush();
        dataGridItems.setVisibleRange(0,items.size());
        cart.setBadgeText(String.valueOf(items.size()));
    }

    @Override
    public TextBox getTextBoxDiscount() {
        return textBoxDiscount;
    }

    @Override
    public FormControlStatic getFormControlAmountDiscount() {
        return amountDiscount;
    }

    @Override
    public FormControlStatic getFormControlDiscount() {
        return discountApply;
    }

    @Override
    public FormControlStatic getFormControlTotal() {
        return total;
    }

    @Override
    public void guest() {
        panelClient.setVisible(false);
        panelAmountAndDiscount.setVisible(false);
    }

    @Override
    public void setTextsBoxs(boolean b) {
        nameUserCart.setEnabled(b);
        emailUserCart.setEnabled(b);
        dniUSerCart.setEnabled(b);
        domUserCart.setEnabled(b);
        phoneUserCart.setEnabled(b);
        clearClient.setVisible(!b);
    }

    @Override
    public Button getButtonClearClient() {
        return clearClient;
    }

    @Override
    public void clearDataUser() {
        nameUserCart.setText("");
        emailUserCart.setText("");
        domUserCart.setText("");
        dniUSerCart.setText("");
        phoneUserCart.setText("");
        setTextsBoxs(true);
    }

    @Override
    public TextBox getTextBoxName() {
        return nameUserCart;
    }

    @Override
    public TextBox getTextBoxEmail() {
        return emailUserCart;
    }

    @Override
    public TextBox getTextBoxPhone() {
        return phoneUserCart;
    }

    @Override
    public TextBox getTextBoxDni() {
        return dniUSerCart;
    }

    @Override
    public Heading getHeadinCart() {
        return heading;
    }

    @Override
    public Form getFormClient() {
        return formUser;
    }

    @Override
    public TextBox getTextBoxDom() {
        return domUserCart;
    }

    @Override
    public void setVisibleUsers(boolean visibleUsers) {
        users.setVisible(visibleUsers);
    }
}
