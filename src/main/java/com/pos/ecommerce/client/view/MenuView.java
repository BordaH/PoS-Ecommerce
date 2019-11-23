package com.pos.ecommerce.client.view;


import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pos.ecommerce.client.presenter.MenuPresenter;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class MenuView implements MenuPresenter.Display {


    private final AnchorListItem order;
    private final AnchorListItem stock;
    private AnchorListItem orders;
    private AnchorListItem logOut;
    private Button login;
    private final Navbar navPills;
    private final VerticalPanel activity;
    private final FlowPanel rootPanel;
    private final NavbarBrand brand;
    private final NavbarCollapseButton collapseButton;
    private final NavbarCollapse collapse;
    private final NavbarNav navbarNav;
    private TextBox email;
    private TextBox password;
    private Modal modalLogin;
    private Button invited;
    private HelpBlock helpBlock;

    public MenuView(){

        navPills = new Navbar();
        activity = new VerticalPanel();
        activity.getElement().setAttribute("align","center");
        rootPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        navPills.setPosition(NavbarPosition.STATIC_TOP);
        NavbarHeader header = new NavbarHeader();

        brand = new NavbarBrand();
        brand.setText("");
        brand.setPull(Pull.LEFT);
        Image img = new Image("images/EasyPos-icon.png");
        img.setHeight("2.5em");
        img.getElement().getStyle().setMarginTop(-0.7  , Style.Unit.EM);
        img.setResponsive(true);
        img.setPull(Pull.LEFT);
        brand.add(img);
        brand.setPull(Pull.LEFT);

        navbarNav = new NavbarNav();
        navbarNav.setPull(Pull.RIGHT);

        navbarNav.setMarginRight(5);

        collapseButton = new NavbarCollapseButton();
        collapseButton.setDataTarget("#menu-collapse");
        collapseButton.setPull(Pull.LEFT);
        collapse = new NavbarCollapse();
        collapse.setId("menu-collapse");
        order = new AnchorListItem("Pedido");
        stock = new AnchorListItem("Stock");
        logOut = new AnchorListItem("Salir");
        logOut.setIcon(IconType.SIGN_OUT);
        orders = new AnchorListItem("Pedidos");
        navbarNav.add(order);
        navbarNav.add(orders);
        navbarNav.add(stock);
        navbarNav.add(logOut);
        navbarNav.setVisible(false);
        collapse.add(navbarNav);
        header.add(brand);
        header.add(collapseButton);

        collapseButton.setPull(Pull.RIGHT);
        navPills.add(header);
        navPills.add(collapse);
        navPills.setPosition(NavbarPosition.STATIC_TOP);
        navPills.setType(NavbarType.INVERSE);
        navPills.setWidth("101%");
        activity.setHeight("80%");
        rootPanel.add(navPills);
        rootPanel.add(activity);
        activity.setWidth("99%");
        rootPanel.setWidth("100%");
        rootPanel.setHeight("100%");
        createPanelLogin();
    }

    private void createPanelLogin() {
        modalLogin = new Modal();
        modalLogin.setClosable(false);
        ModalHeader modalHeader = new ModalHeader();
        modalHeader.setTitle("¡Bienvenido a Ecommerce!");
        modalHeader.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        modalLogin.add(modalHeader);

        modalLogin.setDataKeyboard(false);
        modalLogin.setHideOtherModals(false);
        modalLogin.setRemoveOnHide(false);
        modalLogin.setDataBackdrop(ModalBackdrop.STATIC);
        modalLogin.setFade(true);
        ModalBody modalBody = new ModalBody();
        Image containImage = new Image("images/EasyPos-icon.png");

        containImage.setWidth("220px");
        containImage.getElement().getStyle().setMarginBottom(17, Style.Unit.PX);
        modalBody.add(containImage);
        modalBody.getElement().getStyle().setProperty("textAlign","-webkit-center");
        ModalFooter modalFooter = new ModalFooter();
        Form form = new Form();
        form.setType(FormType.DEFAULT);
        FieldSet fieldSet = new FieldSet();
        FormGroup formGroup = new FormGroup();
        email = new TextBox();
        email.setPlaceholder("Email");
        email.setSize(InputSize.SMALL);

        formGroup.add(email);
        FormGroup formGroup1 = new FormGroup();
        password = new TextBox();
        helpBlock = new HelpBlock();

        password.getElement().setAttribute("type","password");
        password.setPlaceholder("Contraseña");
        password.setSize(InputSize.SMALL);
        formGroup1.add(password);
        formGroup1.add(helpBlock);
        login = new Button("Iniciar Sesion");
        login.setType(ButtonType.PRIMARY);
        login.setSize(ButtonSize.SMALL);
        invited = new Button("Ingresar como invitado");
        invited.setType(ButtonType.LINK);
        fieldSet.add(formGroup);
        fieldSet.add(formGroup1);
        modalFooter.add(invited);
        modalFooter.add(login);
        form.add(fieldSet);
        modalBody.add(form);
        modalLogin.add(modalBody);
        modalLogin.setSize(ModalSize.SMALL);
        modalLogin.add(modalFooter);
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }

    @Override
    public Button getButtonLogin() {
        return login;
    }

    @Override
    public TextBox getEmail() {
        return email;
    }

    @Override
    public TextBox getPassword() {
        return password;
    }


    @Override
    public void showMenu() {
        navbarNav.setVisible(true);
    }

    @Override
    public Modal getModalLogin() {
        return modalLogin;
    }

    @Override
    public AnchorListItem getButtonOrder() {
        return order;
    }

    @Override
    public VerticalPanel getActivity() {
        return activity;
    }

    @Override
    public Button getButtonInvited() {
        return invited;
    }

    @Override
    public void invited() {
        order.setVisible(false);
        orders.setVisible(false);
        stock.setVisible(false);
    }

    @Override
    public AnchorListItem getButtonLogOut() {
        return logOut;
    }

    @Override
    public AnchorListItem getButtonOrders() {
        return orders;
    }

    @Override
    public TextBox getTextBoxEmail() {
        return email;
    }

    @Override
    public TextBox getTextBoxPassword() {
        return password;
    }

    @Override
    public AnchorListItem getButtonStock() {
        return stock;
    }

    @Override
    public HelpBlock getHelpblock() {
        return helpBlock;
    }
}
