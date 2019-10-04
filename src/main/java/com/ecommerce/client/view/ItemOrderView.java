package com.ecommerce.client.view;

import com.ecommerce.dominio.Item;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.html.Paragraph;

public class ItemOrderView extends Composite {
    private final Button button;
    private final IntegerBox cant;

    public ItemOrderView(Item r) {
        ThumbnailPanel thumbnailPanel = new ThumbnailPanel();
        Caption caption = new Caption();
        Heading heading = new Heading(HeadingSize.H3);
        heading.setText(r.getName());
        IsWidget price = createFormGroup("Precio:","$"+r.getPrice());
        IsWidget code = createFormGroup("Codigo:",r.getCode());
        button = new Button();
        button.setMarginTop(5);
        cant = new IntegerBox();
        cant.setValue(r.getQuantity());
        cant.setWidth("50%");
        button.setSize(ButtonSize.EXTRA_SMALL);
        caption.add(heading);
        caption.add(code);
        caption.add(price);
        caption.add(cant);
        caption.add(button);
        Icon icon = new Icon();
        icon.setType(IconType.ADDRESS_CARD  );
        icon.setSize(IconSize.TIMES4);
        icon.setPull(Pull.LEFT);
        thumbnailPanel.add(icon);
        thumbnailPanel.add(caption);

        initWidget(thumbnailPanel);
    }
    private IsWidget createFormGroup(String field, String value) {
        Form group = new Form();
        group.setType(FormType.INLINE);
        FieldSet fieldSet = new FieldSet();
        FormLabel formLabel = new FormLabel();
        formLabel.setText(field);
        formLabel.setFor("form");
        FormControlStatic formControlStatic = new FormControlStatic();
        formControlStatic.setId("form");
        if (value.isEmpty()){
            formControlStatic.setText("SIN DEFINIR");
        }else {
            formControlStatic.setText(value);
        }
        formControlStatic.setMarginLeft(4);
        org.gwtbootstrap3.client.ui.gwt.FlowPanel flowPanel = new org.gwtbootstrap3.client.ui.gwt.FlowPanel();
        flowPanel.add(formControlStatic);
        fieldSet.add(formLabel);
        fieldSet.add(formControlStatic);
        group.add(fieldSet);
        return group;
    }
    public Button getButton() {
        return button;
    }

    public IntegerBox getTextBoxQuantity() {
        return cant;
    }
}
