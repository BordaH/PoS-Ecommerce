package com.pos.ecommerce.client.entitites;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "items" ,uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")})
public class Item  implements IsSerializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private  String name;
    private  String code;
    private  Double price;
    private Integer quantity;
    @ManyToOne
    private Order order;

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

    @Transient
    public Double getTotal() {
        return price*quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }
}
