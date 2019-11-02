package com.pos.ecommerce.dao.impl;

import com.pos.ecommerce.client.entitites.Order;
import com.pos.ecommerce.dao.OrderDAO;
import com.pos.ecommerce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    private static OrderDAOImpl instance;
    private final Session session;

    public static OrderDAOImpl getInstance() {
        if (instance==null){
            instance = new OrderDAOImpl();
        }
        return instance;
    }

    private OrderDAOImpl(){
        session = HibernateUtil.getSession();
    }

    @Override
    public Order saveOrder(Order order) {
            final Transaction transaction = session.beginTransaction();
            try {
                session.saveOrUpdate(order);
                transaction.commit();
                return order;
            } catch (Exception ex) {
                session.getTransaction().rollback();
                ex.printStackTrace();
                return null;
            }
    }

    @Override
    public List<Order> getOrdes() {
        return (List<Order>)session.createCriteria(Order.class).list();
    }

    @Override
    public List<Order> getOrder(String value) {
        return (List<Order>) session.createCriteria(Order.class)
                .add(Restrictions.like("code", "%"+value+"%"))
                .list();
    }

    @Override
    public Order getOrderForId(Long id) {
        return (Order) session.createCriteria(Order.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

}
