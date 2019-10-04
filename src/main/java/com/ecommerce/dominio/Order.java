package com.ecommerce.dominio;

import java.io.Serializable;
import java.util.List;
import java.util.stream.DoubleStream;

public class Order implements Serializable {

    private String name;
    private  String email;
    private  String dom;
    private  String phone;
    private  String note;
    private  List<Item> items;
    private String id;
    private String code;


    public Order(String id, String code,String name, String email, String dom, String phone, String note, List<Item> items) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.dom = dom;
        this.phone = phone;
        this.note = note;
        this.items = items;
    }

    public Order() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTotal() {
        return items.stream().filter(i->i!=null).flatMapToDouble(item -> DoubleStream.of(item.getPrice()*item.getQuantity())).sum();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
