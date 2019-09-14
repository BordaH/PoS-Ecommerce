package com.ecommerce.dominio;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    private  String email;
    private  String dom;
    private  String phone;
    private  String note;
    private  List<Item> items;

    public Order(){}

    public Order(String email, String dom, String phone, String note, List<Item> items) {

        this.email = email;
        this.dom = dom;
        this.phone = phone;
        this.note = note;
        this.items = items;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
