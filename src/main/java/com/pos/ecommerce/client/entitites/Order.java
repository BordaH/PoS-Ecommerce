package com.pos.ecommerce.client.entitites;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", unique=true, nullable=false)
    private Long id;

    @ManyToOne(targetEntity=Client.class, cascade=CascadeType.ALL)
    private Client user ;
    private  String note="";

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="order_id")
    private List<Item> items= new ArrayList<>();
    private String code;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return items.stream().filter(i->i!=null).flatMapToDouble(item -> DoubleStream.of(item.getPrice()*item.getQuantity())).sum()-amountDiscount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
