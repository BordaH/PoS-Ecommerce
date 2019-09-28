package com.ecommerce.dominio;

import java.io.Serializable;

public class Item implements Serializable {

    private  String name;
    private  String code;
    private  Double price;
    private Integer quantity;

    public Item(){

    }
    public Item(String name,String code,Double price){
        this.quantity=1;
        this.name = name;
        this.code = code;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
