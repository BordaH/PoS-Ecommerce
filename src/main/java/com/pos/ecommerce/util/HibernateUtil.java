package com.pos.ecommerce.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;

/**
 * HibernateUtil
 *
 * @author Michal Vlček<mychalvlcek@gmail.com>
 * @date 18.03.15
 */
public class HibernateUtil {

    private static SessionFactory ourSessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static Session getSession() throws HibernateException {
        if (ourSessionFactory == null) {
            try {
                ourSessionFactory = SessionFactoryProvider.getInstance().createSession();
            } catch (RuntimeException e) {
                throw e;
            }
        }
        return ourSessionFactory.openSession();
    }
}
