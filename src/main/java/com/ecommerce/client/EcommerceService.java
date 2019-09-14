package com.ecommerce.client;

import com.ecommerce.dominio.Item;
import com.ecommerce.dominio.Order;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("EcommerceService")
public interface EcommerceService extends RemoteService {

    boolean userOfsession();

    String login(String email, String password);

    List<Item> getProduct(String text);

    Order createOrder(String email, String dom, String phone, String note, List<Item> items);

    List<Order> getOrders();
}
