package com.pos.ecommerce.client.view;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pos.ecommerce.client.constants.HeaderConstanst;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.presenter.StockPresenter;
import com.pos.ecommerce.client.presenter.ValidatorBlank;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.Date;
import java.util.List;

public class StockView implements StockPresenter.Display {
    private FlowPanel rootPanel;
    private TextBox textBoxSearch;
    private CellTable<ItemDTO> dataGrid;
    private ListDataProvider<ItemDTO> listDataProvider;
    private SingleSelectionModel<ItemDTO> selectionModel;
    private Button button;
    private TextBox description;
    private TextBox codigo;
    private TextBox price;
    private TextBox stock;
    private Modal modal;
    private Button buttonSave;
    private Form form;
    private Button createProduct;


    public StockView(){
        initModalDetail();
        rootPanel = new FlowPanel();
        button = new Button("Crear producto");
        button.setSize(ButtonSize.SMALL);
        button.setType(ButtonType.PRIMARY);
        button.setPull(Pull.RIGHT);
        button.setMarginBottom(7);
        textBoxSearch = new TextBox();
        Legend heading = new Legend();
        heading.setText("Stock");
        heading.setMarginLeft(4);
        rootPanel.add(heading);
        Panel panel = new Panel();

        PanelBody panelBody = new PanelBody();
        panelBody.add(createPanelSearch());
        panelBody.add(button);
        panelBody.add(crateTable());
        panel.add(panelBody);
        rootPanel.add(panel);
    }

    private void initModalDetail() {
        modal = new Modal();
        modal.setTitle("Editar producto");
        ModalBody modalBody = new ModalBody();
        form = new Form();
        form.setType(FormType.INLINE);
        FieldSet fieldSetUser = new FieldSet();
        description = new TextBox();
        codigo = new TextBox();
        codigo.setEnabled(false);
        price = new TextBox();
        stock = new TextBox();
        stock.addKeyPressHandler(event -> {
            if(event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_DELETE &&
                    event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_BACKSPACE &&
                    event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_LEFT &&
                    event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_RIGHT){
                String c = event.getCharCode()+"";
                if(RegExp.compile("[^0-9]").test(c) || event.getCharCode()== KeyCodes.KEY_BACKSPACE) {
                    stock.cancelKey();
                }
            }
        });

        FormGroup formGroupStock = new FormGroup();
        HelpBlock helpBlock = new HelpBlock();
        formGroupStock.add(stock);
        formGroupStock.add(helpBlock);

        stock.addKeyUpHandler(e->{
            if (stock.getValue().isEmpty()){
            }
        });
        stock.addValidator(new ValidatorBlank());
        description.addValidator(new ValidatorBlank());
        price.addValidator(new ValidatorBlank());
        FormGroup formGroupDescription = new FormGroup();
        HelpBlock helpBlock2= new HelpBlock();
        formGroupDescription.add(description);
        formGroupDescription.add(helpBlock2);
        FormGroup formGroupPrice = new FormGroup();
        HelpBlock helpBlock1= new HelpBlock();
        formGroupPrice.add(price);
        formGroupPrice.add(helpBlock1);

        FormGroup formGroupCode = new FormGroup();
        HelpBlock helpBlock3= new HelpBlock();
        formGroupCode.add(codigo);
        formGroupCode.add(helpBlock3);
        codigo.addValidator(new ValidatorBlank());
        createFormGroup("Codigo:",formGroupCode, fieldSetUser);
        createFormGroup("Descripcion:",formGroupDescription,fieldSetUser);
        createFormGroup("Precio:",formGroupPrice, fieldSetUser);
        createFormGroup("Cantidad en stock:",formGroupStock, fieldSetUser);

        form.add(fieldSetUser);
        modalBody.add(form);
        buttonSave = new Button("Guardar producto");
        buttonSave.setType(ButtonType.INFO);
        buttonSave.setSize(ButtonSize.SMALL);
        createProduct = new Button("Crear producto");
        createProduct.setType(ButtonType.INFO);
        createProduct.setSize(ButtonSize.SMALL);
        createProduct.setVisible(false);
        fieldSetUser.add(buttonSave);
        fieldSetUser.add(createProduct);
        modal.add(modalBody);

    }
    private IsWidget createFormGroup(String field, Widget formControlStatic, FieldSet fieldSet) {
        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.addStyleName("col-sm-6");
        formControlStatic.addStyleName("col-sm-5");
        fieldSet.add(formLabel);
        fieldSet.add(formControlStatic);
        return fieldSet;
    }
    private Widget crateTable() {
        dataGrid = new CellTable<ItemDTO>();
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

        Column<ItemDTO, String> quantity = new Column<ItemDTO, String>(new TextCell()) {
            @Override
            public String getValue(ItemDTO object) {
                return String.valueOf(object.getQuantity());
            }
        };;

        dataGrid.addColumn(code, HeaderConstanst.CODE);
        dataGrid.addColumn(name,HeaderConstanst.DESCRIPTION);
        dataGrid.addColumn(quantity,HeaderConstanst.QUANTITY);
        dataGrid.addColumn(price,HeaderConstanst.PRICE);
        dataGrid.setWidth("100%");
        dataGrid.setBordered(true);
        dataGrid.setCondensed(true);
        dataGrid.setHover(true);
        dataGrid.setStriped(true);
        dataGrid.setHeight("50%");
        listDataProvider = new ListDataProvider<>();
        listDataProvider.addDataDisplay(dataGrid);
        dataGrid.setLoadingIndicator(new ProgressBar());
        selectionModel = new SingleSelectionModel<ItemDTO>();
        dataGrid.setSelectionModel(selectionModel);
        return dataGrid;
    }

    private Widget createPanelSearch() {
        FormControlStatic formLabel = new FormControlStatic();
        formLabel.setText("Ingrese el codigo del producto: ");
        formLabel.addStyleName("col-sm-3");
        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        flowPanel.add(textBoxSearch);
        textBoxSearch.addStyleName("col-sm-5");
        FieldSet fieldSet = new FieldSet();
        Form form = new Form();
        form.setType(FormType.INLINE);
        fieldSet.add(formLabel);
        fieldSet.add(flowPanel);
        form.add(fieldSet);
        return form;
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }

    @Override
    public TextBox getTextBoxSearch() {
        return textBoxSearch;
    }

    @Override
    public Button getButtonAdd() {
        return button;
    }

    @Override
    public CellTable<ItemDTO> getCellTable() {
        return dataGrid;
    }

    @Override
    public ListDataProvider<ItemDTO> getListDataProvider() {
        return listDataProvider;
    }

    @Override
    public SingleSelectionModel<ItemDTO> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void showDetail(ItemDTO itemDTO, boolean b) {
        if(b){
            codigo.setEnabled(false);
            price.setText(String.valueOf(itemDTO.getPrice()));
            description.setText(itemDTO.getName());
            codigo.setText(itemDTO.getCode());
            stock.setText(String.valueOf(itemDTO.getQuantity()));
            modal.setTitle("Editar producto");
        }else {
            codigo.setEnabled(true);
            price.setText("");
            description.setText("");
            codigo.setText("");
            stock.setText("");
            modal.setTitle("Crear producto");
        }
        modal.show();
    }

    @Override
    public Button getButtonSave() {
        return buttonSave;
    }

    @Override
    public Form getForm() {
        return form;
    }

    @Override
    public TextBox getCodeTextBox() {
        return codigo;
    }

    @Override
    public TextBox getStockTextBox() {
        return stock;
    }

    @Override
    public TextBox getDescriptionTextBox() {
        return description;
    }

    @Override
    public TextBox getPriceTextBox() {
        return price;
    }

    @Override
    public Button getButtoCreate() {
        return createProduct;
    }
}
