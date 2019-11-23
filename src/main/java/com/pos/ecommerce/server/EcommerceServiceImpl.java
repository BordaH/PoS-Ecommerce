package com.pos.ecommerce.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.*;
import com.pos.ecommerce.client.EcommerceService;
import com.pos.ecommerce.client.dto.*;
import com.pos.ecommerce.client.entitites.*;
import com.pos.ecommerce.client.entitites.Item;
import com.pos.ecommerce.client.entitites.exceptions.*;
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
        itemDTO.setQuantity(i.getQuantity());
        itemDTO.setId(i.getId().toString());
        return itemDTO;
    }

    private ItemDTO createItemDTO(Item i) {
        ItemDTO itemDTO = new ItemDTO(i.getName(), i.getCode(), i.getPrice());
        itemDTO.setId(i.getId().toString());
        return itemDTO;
    }

    @Override
    public OrderDTO sendOrder(OrderDTO order) throws CreateException, SaveOrderException {
        order.setConfirm(true);
        Order order1 = createOrder(order);
        order1 = saveOrder(order1);
        getInstanceProductDAO().updateStocks(order1.getItems());
        return order;
    }

    private Order saveOrder(Order order) throws SaveOrderException {
        return getOrderDAOImpl().saveOrder(order);
    }

    private Order createOrder(OrderDTO orderDTO) throws CreateException {
        Client client = createClient(orderDTO.getUser());
        List<Item> items = createItems(orderDTO.getItems());
        Order order = new Order(client, items, orderDTO.getNote());
        order.setCode(orderDTO.getCode());
        order.setAmountDiscount(orderDTO.getAmountDiscount());
        order.setDisount(orderDTO.getDisount());
        order.setConfirm(orderDTO.getConfirm());
        return order;
    }

    private List<Item> createItems(List<ItemDTO> items) throws CreateException {
        List<Item> collect = new ArrayList<>();
        for (ItemDTO item : items) {
            Item item1 = createItem(item);
            collect.add(item1);
        }
        return collect;
    }

    private Item createItem(ItemDTO i) throws CreateException {
        try{
            Item item = new Item(i.getName(), i.getCode(), i.getPrice());
            item.setQuantity(i.getQuantity());
            return item;
        }catch (Exception e){
            throw  new CreateException(e,"Error al crear los items del pedido.");
        }

    }

    private Client createClient(UserDTO user) throws CreateException {
        try{

            Client user1 = new Client(user.getFirstName(), user.getLastName(),
                    user.getEmail(),
                    user.getDni(), user.getDom(), user.getPhone());
            user1.setId(user.getId());
            user1.setGuest(user.getGuest());
            return user1;
        }catch (Exception e){
            throw new CreateException(e,"Error al crear el usuario del pedido.");
        }
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
    public OrderDTO confirmOrder(OrderDTO orderDTO) throws SaveOrderException {
        Order order = getOrderDAOImpl().getOrderForId(orderDTO.getCode());
        order.setConfirm(true);
        if (getOrderDAOImpl().updateOrder(order) != null) {
            orderDTO.setConfirm(true);
        } else {
            orderDTO.setConfirm(false);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO sendOrderGuest(OrderDTO order) throws CreateException, SaveOrderException {
        order.setConfirm(false);
        order.getUser().setGuest(true);
        Order order1 = createOrder(order);
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

    @Override
    public String sendOrderMP(OrderDTO order) {
        try {
            MercadoPago.SDK.setClientId("8622882976678792");
        } catch (MPException e) {
            e.printStackTrace();
        }
        try {
            MercadoPago.SDK.setClientSecret("1F37sFnvRM61IW72AKXAXIm8F1kIcfrZ");
        } catch (MPException e) {
            e.printStackTrace();
        }

        try {
            MercadoPago.SDK.setAccessToken("TEST-8622882976678792-112204-91071c16bfb917839cd94eafba6e1cb1-102442715");
        } catch (MPConfException e) {
            e.printStackTrace();
        }
        Preference preference = new Preference();
        order.getItems().stream().forEach(i->{
            com.mercadopago.resources.datastructures.preference.Item item = new com.mercadopago.resources.datastructures.preference.Item();
            item.setId(i.getId());
            item.setTitle(i.getName());
            item.setDescription(i.getName());
            item.setQuantity(1);
            item.setUnitPrice((float) i.getTotal());
            item.setCurrencyId("ARS");
            preference.appendItem(item);
        });
      BackUrls backUrls = new BackUrls();

       /* backUrls.setFailure("http://localhost:8080/Ecommerce/Ecommerce/checkMP");
        backUrls.setPending("http://localhost:8080/Ecommerce/Ecommerce/checkMP");
        backUrls.setSuccess("http://localhost:8080/Ecommerce/Ecommerce/checkMP");*/

        backUrls.setFailure("http://localhost:8888/Ecommerce/checkMP");
        backUrls.setPending("http://localhost:8888/Ecommerce/checkMP");
        backUrls.setSuccess("http://localhost:8888/Ecommerce/checkMP");
        preference.setBackUrls(backUrls);
        com.mercadopago.resources.datastructures.preference.Payer payer = new com.mercadopago.resources.datastructures.preference.Payer();
        payer.setName(order.getUser().getFullName())
                .setSurname("Naranjo")
                .setEmail("henryborda17@gmail.com")
                .setDateCreated("2018-06-02T12:58:41.425-04:00")
                .setIdentification(new Identification()
                        .setType("DNI")
                        .setNumber("12345678"))
                .setAddress(new Address()
                        .setStreetName("Monte Natalia Báez")
                        .setZipCode("59820"));
        preference.setPayer(payer);
        preference.setExternalReference("Order_123");
        Preference p = null;
        String initPoint = null;
        try {
            p = preference.save();
            initPoint = p.getInitPoint();
        } catch (MPException e) {
            e.printStackTrace();
        }

        System.out.println(initPoint);
        return initPoint;
    }

    @Override
    public List<ItemDTO> getStockFor(String code) {
        return getInstanceProductDAO().getProductForCode(code).stream()
                .map(p->createItemDTO(p)).collect(Collectors.toList());
    }

    @Override
    public ItemDTO updateProduct(ItemDTO itemDTO) throws ProductException {
        getInstanceProductDAO().updateStock(createProduct(itemDTO));
        return itemDTO;
    }

    @Override
    public ItemDTO createNewProduct(ItemDTO itemDTO) throws ProductException {
        Product product = getInstanceProductDAO().createProduct(createProduct(itemDTO));
        itemDTO.setId(product.getId().toString());
        return itemDTO;
    }

    private Product createProduct(ItemDTO itemDTO) {
        Product p = new Product(itemDTO.getName(),itemDTO.getCode(),itemDTO.getPrice(),itemDTO.getQuantity());
        try {
            p.setId(Long.valueOf(itemDTO.getId()));
        }catch (Exception e){
        }
        return p;
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