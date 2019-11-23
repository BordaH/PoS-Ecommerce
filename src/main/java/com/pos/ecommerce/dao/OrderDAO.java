package com.pos.ecommerce.dao;

import com.pos.ecommerce.client.entitites.Order;
import com.pos.ecommerce.client.entitites.exceptions.SaveOrderException;
import org.hibernate.HibernateException;

import java.util.List;

public interface OrderDAO {

    Order saveOrder(Order order) throws SaveOrderException;

    List<Order> getOrdes();


    List<Order> getOrder(String value);

    Order getOrderForId(Long items) throws HibernateException;

    Order updateOrder(Order order) throws SaveOrderException;
}
