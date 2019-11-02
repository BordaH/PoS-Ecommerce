package com.pos.ecommerce.dao;

import com.pos.ecommerce.client.entitites.Order;

import java.util.List;

public interface OrderDAO {

    Order saveOrder(Order order);

    List<Order> getOrdes();


    List<Order> getOrder(String value);

    Order getOrderForId(Long items);
}
