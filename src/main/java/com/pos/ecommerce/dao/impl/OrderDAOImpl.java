package com.pos.ecommerce.dao.impl;

import com.pos.ecommerce.client.entitites.Order;
import com.pos.ecommerce.client.entitites.exceptions.SaveOrderException;
import com.pos.ecommerce.dao.OrderDAO;
import com.pos.ecommerce.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    private static OrderDAOImpl instance;
    private final Session session;
    private Long id = 1L;
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

    public Order saveOrder(Order order) throws SaveOrderException {
            final Transaction transaction = session.beginTransaction();
            try {
                session.clear();
                session.save(order);
                transaction.commit();
                return order;
            } catch (Exception ex) {
                transaction.rollback();
                throw new SaveOrderException(ex,"Error al guardar el pedido.");
            }
    }

    @Override
    public List<Order> getOrdes() {
        return (List<Order>)session.createCriteria(Order.class).list();
    }

    @Override
    public List<Order> getOrder(String value) {
        return (List<Order>) session.createCriteria(Order.class)
                .add(Restrictions.eq("code", Long.valueOf(value)))
                .list();
    }

    @Override
    public Order getOrderForId(Long id) throws HibernateException {
        return (Order) session.createCriteria(Order.class)
                .add(Restrictions.eq("code", id))
                .uniqueResult();
    }

    @Override
    public Order updateOrder(Order order) throws SaveOrderException {
        final Transaction transaction = session.beginTransaction();
        try {
            session.merge(order);
            transaction.commit();
            session.flush();
            return order;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new SaveOrderException(ex,"Error al actulizar el pedido.");
        }
    }

}
