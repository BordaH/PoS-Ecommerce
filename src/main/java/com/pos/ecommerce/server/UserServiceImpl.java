package com.pos.ecommerce.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pos.ecommerce.client.UserService;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.User;
import com.pos.ecommerce.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michalvlcek on 18.03.15.
 */
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
    @Override
    public List<UserDTO> getUsers() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<User> users = new ArrayList<User>(session.createQuery("from User").list());

        List<UserDTO> result = new ArrayList<UserDTO>(
                users != null ? users.size() : 0);
        session.getTransaction().commit();
        return result;
    }

    @Override
    public Long saveUser(UserDTO user) {
        Session session = HibernateUtil.getSession();

        return user.getId();
    }

    @Override
    public void saveBorrow(Long userId, Long carId) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.flush();

        session.getTransaction().commit();
    }
}