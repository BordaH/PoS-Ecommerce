package com.ecommerce.server;

import com.ecommerce.dominio.*;
import com.ecommerce.dominio.exceptions.InvalidPasswordException;
import com.ecommerce.dominio.exceptions.LoginException;
import com.ecommerce.dominio.exceptions.UnregisteredUserException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ecommerce.client.EcommerceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EcommerceServiceImpl extends RemoteServiceServlet implements EcommerceService {

     private static SecureRandom random = new SecureRandom();
    private static final int LENGTH = 32;

    @Override
    public User userOfsession() {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        String id = (String) httpServletRequest.getSession(true).getAttribute("id");
        return id!=null ? (User) httpServletRequest.getSession(true).getAttribute(id) :null;
    }

    @Override
    public Boolean login(String email, String password) throws LoginException {
        return checkDataLogin(email,password);
    }

    @Override
    public List<Item> getProduct(String text) {
        return Repository.getInstance().items().stream().filter(p->p.getName().contains(text)||p.getCode().contains(text)).collect(Collectors.toList());
    }

    @Override
    public Order createOrder(String email, String dom, String phone, String note, List<Item> items)
    {
        return Repository.getInstance().addOrder(new Order(email,dom,phone,note,items));
    }

    @Override
    public List<Order> getOrders() {
        return Repository.getInstance().orders();
    }

    @Override
    public User logOut() {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(false);
        User user = (User) session.getAttribute((String) session.getAttribute("id"));
        session.removeAttribute("id");
        session.removeAttribute((String) session.getAttribute("id"));
        session.invalidate();
        return user;
    }

    @Override
    public String loginGuest() {
        return getSessionIdFor("guest");
    }

    public boolean checkDataLogin(String email, String password) throws LoginException {
        User user = checkEmailValid(email);
        user = checkPasswordValid(user,password);
        return saveSession(user);
    }

    private boolean saveSession(User user) {
        String sessionId = getSessionIdFor(user.getEmail());
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("id", sessionId);
        session.setAttribute(sessionId,user);
        //TODO:GUARDAR ID EN BASE CON USUARIO
        return true;
    }

    private String getSessionIdFor(String email) {
        BigInteger bigInteger = new BigInteger(130, random);
        String sessionId = String.valueOf(bigInteger.toString(LENGTH)).concat(email);
        return sessionId;
    }

    private User checkPasswordValid(User user, String password) throws LoginException{
       if (user.getPassword().equals(password)){
            return user;
        }else {
            throw new InvalidPasswordException();
        }
    }

    private User checkEmailValid(String email) throws LoginException{
        Optional<User> first = Repository.getInstance().users().stream().filter(u -> u.getEmail().equals(email)).findFirst();
        if (first.isPresent()){
            return first.get();
        }else {
            throw new UnregisteredUserException();
        }
    }
}