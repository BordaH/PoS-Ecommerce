package com.pos.ecommerce.client.entitites;

import javax.persistence.*;

@Entity
@Table(name = "products" )
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private  String name;
    private  String code;
    private  Double price;
    private Integer quantity;

    public Product(){

    }
    public Product(String name,String code,Double price){
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

    @Transient
    public Double getTotal() {
        return price*quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
