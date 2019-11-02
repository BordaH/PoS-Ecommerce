package com.pos.ecommerce.dao.impl;

import com.pos.ecommerce.client.entitites.Product;
import com.pos.ecommerce.dao.ProductDAO;
import com.pos.ecommerce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private static ProductDAO instance;
    private final Session session;

    public static ProductDAO getInstance() {
        if (instance==null){
            instance = new ProductDAOImpl();
        }
        return instance;
    }

    private ProductDAOImpl(){
        session = HibernateUtil.getSession();
    }
    @Override
    public List<Product> getProduct(String text) {
        return (List<Product>) session.createCriteria(Product.class)
                .add(Restrictions.like("name", "%"+text+"%"))
                .list();
    }
}
