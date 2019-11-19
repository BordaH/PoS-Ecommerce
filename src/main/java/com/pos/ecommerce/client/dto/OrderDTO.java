package com.pos.ecommerce.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

public class OrderDTO implements Serializable {
    private UserDTO user ;
    private  String note="";
    private  List<ItemDTO> items= new ArrayList<>();
    private Long code;
    private boolean confirm = false;
    private double amountDiscount=0.00;
    private Integer discount=0;
    private Date date;

    public OrderDTO(UserDTO user, List<ItemDTO> items, String note) {
        this.user = user;
        this.items = items;
        this.note = note;
    }

    public OrderDTO() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void removeItem(ItemDTO itemDTO) {
        items.stream().forEach(i->{
            if(itemDTO.getCode().equals(i.getCode())){
                items.remove(i);
                checkDiscount();
            }
        });
    }

    private void checkDiscount() {
        if (items.isEmpty()){
            amountDiscount=0.00;
        }
    }
}
