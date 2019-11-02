package com.pos.ecommerce.dao.impl;

import com.pos.ecommerce.client.entitites.Client;
import com.pos.ecommerce.client.entitites.User;
import com.pos.ecommerce.dao.UserDAO;
import com.pos.ecommerce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.security.SecureRandom;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static UserDAO instance;
    private static SecureRandom random = new SecureRandom();
    private static final int LENGTH = 32;
    private Session session;

    public static UserDAO getInstance() {
        if (instance==null){
            instance = new UserDAOImpl();
        }
        return instance;
    }

    private UserDAOImpl(){
        session = HibernateUtil.getSession();
    }

    @Override
    public User getUserForEmail(String email) {
        return (User) session.createCriteria(User.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }
    @Override
    public List<Client> getClients() {
        return (List<Client>)session.createCriteria(Client.class).add(Restrictions.eq("guest",false)).list();
    }

}
