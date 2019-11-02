package com.pos.ecommerce.client.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.pos.ecommerce.client.dto.ItemDTO;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

public class ItemOrderView extends Composite {
    private final Button button;
    private final TextBox cant;
    private final Button subs;
    private final Button add;

    public ItemOrderView(ItemDTO r) {
        ThumbnailPanel thumbnailPanel = new ThumbnailPanel();
        Caption caption = new Caption();
        Heading heading = new Heading(HeadingSize.H3);
        heading.setText(r.getName());
        Description description = new Description();
        DescriptionTitle descriptionData = new DescriptionTitle();
        descriptionData.setText("Precio$: "+r.getPrice());
        DescriptionData descriptionData2 = new DescriptionData();
        descriptionData2.setText("Codigo:"+r.getCode());
        description.add(descriptionData2);
        description.add(descriptionData);
        button = new Button();
        button.setMarginTop(5);
        cant = new TextBox();
        cant.setValue(String.valueOf(r.getQuantity()));
        cant.setSize(InputSize.SMALL);
        cant.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        InputGroup group = new InputGroup();
        group.setSize(InputGroupSize.SMALL);
        InputGroupButton inputGroupButton = new InputGroupButton();
        add = new Button();
        add.setType(ButtonType.PRIMARY);
        add.setIcon(IconType.PLUS);
        add.setSize(ButtonSize.SMALL);
        InputGroupButton inputGroupButton1 = new InputGroupButton();
        subs = new Button();
        subs.setSize(ButtonSize.SMALL);
        subs.setType(ButtonType.DANGER);
        subs.setIcon(IconType.MINUS);
        inputGroupButton.add(subs);
        inputGroupButton1.add(add);
        group.add(inputGroupButton);
        group.add(cant);
        group.add(inputGroupButton1);
        button.setSize(ButtonSize.SMALL);
        caption.add(heading);
        caption.add(description);
        caption.add(group);
        caption.add(button);
        Icon icon = new Icon();
        icon.setType(IconType.ADDRESS_CARD );
        icon.setSize(IconSize.TIMES4);
        thumbnailPanel.add(icon);
        thumbnailPanel.add(caption);
        thumbnailPanel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        setNumericTextBox();
        initWidget(thumbnailPanel);
    }

    private void setNumericTextBox() {
        cant.addKeyPressHandler(event -> {
            if(isNotSpecialKey(event)){
                String c = event.getCharCode()+"";
                if(RegExp.compile("[^0-9]").test(c) || event.getCharCode()== KeyCodes.KEY_BACKSPACE)
                    cant.cancelKey();
            }
        });
        add.addClickHandler(event -> {
            Integer stg = Integer.parseInt(cant.getValue());
            stg++;
            cant.setText(String.valueOf(stg));
        });
        subs.addClickHandler(event -> {
            Integer stg = Integer.valueOf(cant.getValue());;
            if(stg>1) {
                stg--;
                cant.setText(String.valueOf(stg));
            }
        });
    }

    private boolean isNotSpecialKey(KeyPressEvent event) {
        return event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_DELETE &&
                event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_BACKSPACE &&
                event.getNativeEvent().getKeyCode()!= KeyCodes.KEY_LEFT &&
                event.getNativeEvent().getKeyCode()!=KeyCodes.KEY_RIGHT;
    }
    public Button getButton() {
        return button;
    }

    public TextBox getTextBoxQuantity() {
        return cant;
    }
}
