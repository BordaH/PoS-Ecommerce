package com.pos.ecommerce.client.dto;

import java.io.Serializable;

public class ItemDTO implements Serializable {

    private String id;
    private  String name="";
    private  String code="";
    private  Double price=0.00;
    private Integer quantity=1;

    public ItemDTO(){

    }
    public ItemDTO(String name,String code,Double price){
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

    public double getTotal() {
        return price*quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
