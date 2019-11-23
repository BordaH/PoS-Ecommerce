package com.pos.ecommerce.dao.impl;

import com.pos.ecommerce.client.entitites.Item;
import com.pos.ecommerce.client.entitites.Product;
import com.pos.ecommerce.client.entitites.exceptions.ProductException;
import com.pos.ecommerce.dao.ProductDAO;
import com.pos.ecommerce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private static ProductDAO instance;
    private final Session session;
    private static Long ids = 1L;
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

    @Override
    public List<Product> getProductForCode(String code) {
        return session.createCriteria(Product.class).add(Restrictions.like("code","%"+code+"%")).list();
    }

    @Override
    public void updateStocks(List<Item> items){
        items.forEach(i->{
            List<Product> productForCode = getProductForCode(i.getCode());
            if (!productForCode.isEmpty()){
                Product product = productForCode.get(0);
                product.setQuantity(product.getQuantity()-i.getQuantity());
                try {
                    updateStock(product);
                } catch (ProductException e) {
                    e.printStackTrace();
                }
            }
            ;
        });
    }
    @Override
    public void updateStock(Product product) throws ProductException {
        final Transaction transaction = session.beginTransaction();
        try {
            session.clear();
            session.saveOrUpdate(product);
            transaction.commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new ProductException("Error al actualizar el producto: "+product.getName());
        }
    }

    @Override
    public Product createProduct(Product product) throws ProductException {
        final Transaction transaction = session.beginTransaction();
        try {
            session.save(product);
                transaction.commit();
            return product;
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new ProductException("Error al crear el producto");
        }
    }
}
