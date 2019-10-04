package com.ecommerce.client.view;

import com.ecommerce.client.presenter.NewOrderPresenter;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class NewOrderView implements NewOrderPresenter.Display {
    private FlowPanel rootPanel;
    private TextBox textBoxSearch;
    private Button buttonSearch;
    private AnchorListItem cart;

    public NewOrderView(){
        rootPanel = new FlowPanel();
        rootPanel.add(panelSearch());

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
        navbarForm.add(inputGroup);
        navbarForm.setPull(Pull.RIGHT);
        NavbarForm navbarForm1 = new NavbarForm();
        navbarForm1.add(cart);
        navbarForm1.setPull(Pull.LEFT);
        navbarCollapse.add(navbarForm);
        navbarCollapse.add(navbarForm1);
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
    public void showItems(Row flowPanel) {
        rootPanel.add(flowPanel);
    }

    @Override
    public AnchorListItem getButtonCart() {
        return cart;
    }

}
