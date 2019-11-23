package com.pos.ecommerce.client.entitites;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products" )
public class Product  implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private  String name;
    private  String code;
    private  Double price;
    private Integer quantity;

    public Product(String name, String code, Double price, Integer quantity){

        this.name = name;
        this.code = code;
        this.price = price;
        this.quantity = quantity;
    }

    public Product() {
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

    public Long getId() {
        return id;
    }
}
