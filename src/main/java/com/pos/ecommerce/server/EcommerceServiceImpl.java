package com.pos.ecommerce.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pos.ecommerce.client.EcommerceService;
import com.pos.ecommerce.client.dto.*;
import com.pos.ecommerce.client.entitites.*;
import com.pos.ecommerce.client.entitites.exceptions.InvalidPasswordException;
import com.pos.ecommerce.client.entitites.exceptions.LoginException;
import com.pos.ecommerce.client.entitites.exceptions.UnregisteredUserException;
import com.pos.ecommerce.dao.ProductDAO;
import com.pos.ecommerce.dao.UserDAO;
import com.pos.ecommerce.dao.impl.OrderDAOImpl;
import com.pos.ecommerce.dao.impl.ProductDAOImpl;
import com.pos.ecommerce.dao.impl.UserDAOImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

public class EcommerceServiceImpl extends RemoteServiceServlet implements EcommerceService {


    @Override
    public UserDTO userOfsession() {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        UserDTO dto = null;
        try {
            String id = (String) httpServletRequest.getSession(true).getAttribute("id");
            User userOfSession = getInstanceUserDAOImpl().getUserForEmail(id);
            if (userOfSession!=null){
                dto=createUserDTO(userOfSession);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return dto;
        }
    }

    @Override
    public String login(String email, String password) throws LoginException {
        return checkDataLogin(email,password);
    }

    @Override
    public List<ItemDTO> getProduct(String text) {
                return getInstanceProductDAO().getProduct(text.toLowerCase())
                .stream()
                .map(this::createItemDTO)
                .collect(Collectors.toList());
    }

    private ProductDAO getInstanceProductDAO() {
        return ProductDAOImpl.getInstance();
    }

    private ItemDTO createItemDTO(Product i) {
        ItemDTO itemDTO = new ItemDTO(i.getName(), i.getCode(), i.getPrice());
        return itemDTO;
    }

    private ItemDTO createItemDTO(Item i) {
        ItemDTO itemDTO = new ItemDTO(i.getName(), i.getCode(), i.getPrice());
        itemDTO.setId(i.getId());
        return itemDTO;
    }

    @Override
    public OrderDTO sendOrder(OrderDTO order) {
        order.setConfirm(true);
        Order order1 = createOrder(order);
        return saveOrder(order1)==null?null:order;
    }

    private Order saveOrder(Order order) {
        return getOrderDAOImpl().saveOrder(order);
    }

    private Order createOrder(OrderDTO orderDTO) {
        Order order = new Order(createClient(orderDTO.getUser()), createItems(orderDTO.getItems()), orderDTO.getNote());
        order.setCode(orderDTO.getCode());
        order.setAmountDiscount(orderDTO.getAmountDiscount());
        order.setDisount(orderDTO.getDisount());
        order.setConfirm(orderDTO.getConfirm());
        return order;
    }

    private List<Item> createItems(List<ItemDTO> items) {
        return items.stream().map(this::createItem).collect(Collectors.toList());
    }

    private Item createItem(ItemDTO i) {
        Item item = new Item(i.getName(), i.getCode(), i.getPrice());
        item.setQuantity(i.getQuantity());
        item.setId(i.getId()==null?UUID.randomUUID().toString():i.getId());
        return item;
    }

    private Client createClient(UserDTO user) {
        Client user1 = new Client(user.getFirstName(), user.getLastName(),
                user.getEmail(),
                user.getDni(), user.getDom(), user.getPhone());
        user1.setId(user.getId());
        user1.setGuest(user.getGuest());
        return user1;
    }

    private OrderDAOImpl getOrderDAOImpl() {
        return OrderDAOImpl.getInstance();
    }

    @Override
    public List<OrderDTO> getOrders() {
        return getOrderDAOImpl().getOrdes().stream().map(this::createOrderDTO).collect(Collectors.toList());
    }

    private OrderDTO createOrderDTO(Order o) {
        OrderDTO order = new OrderDTO();
        order.setUser(createUserDTO(o.getUser()));
        order.setAmountDiscount(o.getAmountDiscount());
        order.setDisount(o.getDisount());
        order.setCode(o.getCode());
        order.setConfirm(o.getConfirm());
        order.setItems(createItemsDTO(o.getItems()));
        order.setNote(o.getNote());
        order.setDate(o.getDate());
        return order;
    }

    private List<ItemDTO> createItemsDTO(List<Item> items) {
        return items.stream().map(i->createItemDTO(i)).collect(Collectors.toList());
    }

    @Override
    public String logOut() {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(false);
        String id = (String) session.getAttribute("id");
        if (id!=null){
                session.removeAttribute("id");
                session.invalidate();
            }
        return id;
    }

    @Override
    public String loginGuest() {
        UserDTO user = new UserDTO();
        user.setEmail("guest");
        user.setGuest(true);
        return saveSession(user);
    }

    @Override
    public List<OrderDTO> getOrder(String value) {
        return getOrderDAOImpl().getOrder(value).stream().map(this::createOrderDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO confirmOrder(OrderDTO orderDTO) {
        Order order = getOrderDAOImpl().getOrderForId(orderDTO.getCode());
        order.setConfirm(true);
        if (getOrderDAOImpl().saveOrder(order) != null) {
            orderDTO.setConfirm(true);
        } else {
            orderDTO.setConfirm(false);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO sendOrderGuest(OrderDTO order) {
        order.setConfirm(false);
      //  order.setCode(UUID.randomUUID().toString());
        Order order1 = createOrder(order);
        order1.getUser().setGuest(true);
        logOut();
        return saveOrder(order1)==null?null:order;
    }

    @Override
    public List<UserDTO> getUsers() {
        List<UserDTO> objectStream = new ArrayList<>();
                getInstanceUserDAOImpl().getClients().forEach(u -> objectStream.add(createUserDTO(u)));
        return objectStream;
    }

    @Override
    public List<UserDTO> getUser(String value) {
        String s = value.toLowerCase();
        return getUsers().stream().filter(u->u.getEmail().contains(value) || u.getFirstName().toLowerCase().contains(s)
                            ||u.getDni().contains(value) || u.getPhone().contains(value)
                            ||u.getLastName().toLowerCase().contains(s)).collect(Collectors.toList());

    }

    private String checkDataLogin(String email, String password) throws LoginException {
        UserDTO user = checkEmailValid(email);
        checkPasswordValid(user, password);
        return saveSession(user);
    }

    private String saveSession(UserDTO user) {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("id", user.getEmail());
        return user.getEmail();
    }

    private void checkPasswordValid(UserDTO user, String password) throws LoginException{
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }
    }

    private UserDTO checkEmailValid(String email) throws LoginException{
        User first =  getInstanceUserDAOImpl().getUserForEmail(email);
        if (first!=null){
            return createUserDTO(first);
        }else {
            throw new UnregisteredUserException();
        }
    }

    private UserDAO getInstanceUserDAOImpl() {
        return UserDAOImpl.getInstance();
    }

    private UserDTO createUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setDni(user.getDni());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDom(user.getDom());
        userDTO.setPhone(user.getPhone());
        userDTO.setGuest(user.getGuest());
        userDTO.setPassword(user.getPassword());
        userDTO.setId(user.getId());
        return userDTO;
    }
    private UserDTO createUserDTO(Client user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setDni(user.getDni());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDom(user.getDom());
        userDTO.setPhone(user.getPhone());
        userDTO.setGuest(user.getGuest());
        userDTO.setId(user.getId());
        return userDTO;
    }
}