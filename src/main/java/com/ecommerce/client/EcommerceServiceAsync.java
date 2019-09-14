package com.ecommerce.client;

import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface EcommerceServiceAsync {

    void userOfsession(AsyncCallback<Boolean> booleanAsyncCallback);
    void login(String email, String password, AsyncCallback<String> stringAsyncCallback);
    void getProduct(String text, AsyncCallback<List<Item>> listAsyncCallback);

    void createOrder(String email, String dom, String phone, String note, List<Item> items, AsyncCallback<Order> booleanAsyncCallback);

    void getOrders(AsyncCallback<List<Order>> listAsyncCallback);
}
