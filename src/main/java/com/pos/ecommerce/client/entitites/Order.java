package com.pos.ecommerce.client.entitites;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.annotations.GenerationTime;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

@Entity
@Table(name = "orders")
public class Order  implements IsSerializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="code", unique=true, nullable=false)
    private Long code;

    @ManyToOne(targetEntity=Client.class, cascade=CascadeType.ALL)
    private Client user ;
    private  String note="";

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="order_id")
    private List<Item> items= new ArrayList<>();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;
    private boolean confirm = false;
    private double amountDiscount=0.00;
    private Integer discount=0;

    public Order(Client user, List<Item> items,String note) {
        this.user = user;
        this.items = items;
        this.note = note;
        this.date = new Date();
    }

    public Order() {
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

    public Double getTotal() {
        return items.stream().filter(i->i!=null).flatMapToDouble(item -> DoubleStream.of(item.getPrice()*item.getQuantity())).sum()-amountDiscount;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean getConfirm() {
        return confirm;
    }

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }

    public void setAmountDiscount(double amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public double getAmountDiscount() {
        return amountDiscount;
    }

    public void setDisount(Integer discount) {
        this.discount = discount;
    }

    public Integer getDisount() {
        return discount;
    }
}
