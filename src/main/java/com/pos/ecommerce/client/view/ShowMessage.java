package com.pos.ecommerce.client.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Emphasis;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Created by Henry on 24/04/2018.
 */
public class ShowMessage {

    private static Modal dialog;
    private static Button close;
    private static Paragraph p;

    public static void show(String message) {
            dialog = new Modal();
            dialog.setTitle("PV Mobile");
            dialog.setSize(ModalSize.SMALL);
            dialog.setHideOtherModals(true);
            dialog.setRemoveOnHide(true);
            close = new Button("Cerrar");
            close.setType(ButtonType.DANGER);
            close.addClickHandler(event -> dialog.hide());


            ModalBody modalBody = new ModalBody();
            p = new Paragraph();
            p.setText(message);
            modalBody.add(p);
        Scheduler.get().scheduleDeferred(() -> close.setFocus(true));

            ModalFooter modalFooter = new ModalFooter();
            modalFooter.add(close);

            dialog.setTitle("PV Mobile");
            dialog.add(modalBody);
            dialog.add(modalFooter);
            dialog.show();
            Scheduler.get().scheduleDeferred(() -> close.setFocus(true));
            dialog.addDomHandler(event -> {
                if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER)
                    dialog.hide();
            },KeyUpEvent.getType());

    }

    public static void hideAutomatically(int time) {
        Timer t = new Timer() {
            public void run() {
                dialog.hide();
            }
        };
        t.schedule(time);
    }

    public static void showHideAutmatically(String s, int i) {
        show(s);
        hideAutomatically(i);

    }
    public static Button getClose(){
        return close;
    }

    public static Modal getModal() {
        return dialog;
    }

    public static void showHideAutmaticallyAndFocus(String notFoundProduct, int i, TextBox searchTextBox) {
        show(notFoundProduct);
        Timer t = new Timer() {
            public void run() {
                dialog.hide();
                Scheduler.get().scheduleDeferred(() -> searchTextBox.setFocus(true));

            }
        };
        t.schedule(i);
    }
}

