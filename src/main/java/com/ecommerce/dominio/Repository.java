package com.ecommerce.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Repository implements Serializable {

    private static Repository instance;
    private List<Order> orders;
    private List<Item> items;

    private Repository(){
        orders = new ArrayList<>();
        orders.addAll(Arrays.asList(new Order("email@gmail.com","dom piso1","111","nota",
                        Arrays.asList(new Item("Producto1","COD1",100.10))),
                new Order("email@gmail.com","dom piso1","111","nota2",
                        Arrays.asList(new Item("Producto2","COD2",200.00),new Item("Producto3","COD3",20.00)))));
        items = new ArrayList<>();
        items.addAll(Arrays.asList(new Item("Producto1","cod1",100.00),
                new Item("Prod2","cod2",20.00),
                new Item("Prod3","cod3",10000.00)));    
    }

    public static Repository getInstance() {
        if (instance==null){
            instance = new Repository();
        }
        return instance;
    }

    public static void setInstance(Repository instance) {
        Repository.instance = instance;
    }

    public Order addOrder(Order order) {
        orders.add(order);
        return order;
    }

    public List<Item> items() {
        return items;
    }

    public List<Order> orders() {
        return orders;
    }
}