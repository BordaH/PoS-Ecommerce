package com.ecommerce.server;

import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.ecommerce.dominio.Repository;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ecommerce.client.EcommerceService;

import java.util.Arrays;
import java.util.List;

public class EcommerceServiceImpl extends RemoteServiceServlet implements EcommerceService {

    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }

    @Override
    public boolean userOfsession() {
        return true;
    }

    @Override
    public String login(String email, String password)
    {
        //TODO:IMPLEMENTAR CORRECTAMENTE
        return "";
    }

    @Override
    public List<Item> getProduct(String text) {
        return Repository.getInstance().items();
    }

    @Override
    public Order createOrder(String email, String dom, String phone, String note, List<Item> items)
    {
        return Repository.getInstance().addOrder(new Order(email,dom,phone,note,items));
    }

    @Override
    public List<Order> getOrders() {
        return Repository.getInstance().orders();
    }
}