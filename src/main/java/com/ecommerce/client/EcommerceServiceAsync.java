package com.ecommerce.client;

import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.ecommerce.dominio.User;
import com.ecommerce.dominio.exceptions.LoginException;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface EcommerceServiceAsync {

    void userOfsession(AsyncCallback<User> booleanAsyncCallbackb);
    void login(String email, String password, AsyncCallback<Boolean> stringAsyncCallback) throws LoginException;
    void getProduct(String text, AsyncCallback<List<Item>> listAsyncCallback);
    void createOrder(String name, String email, String dom, String phone, String note, List<Item> items, AsyncCallback<Order> booleanAsyncCallback);
    void getOrders(AsyncCallback<List<Order>> listAsyncCallback);

    void logOut(AsyncCallback<User> userAsyncCallback);

    void loginGuest(AsyncCallback<String> stringAsyncCallback);

    void getOrder(String value, AsyncCallback<List<Order>> listAsyncCallback);
}
