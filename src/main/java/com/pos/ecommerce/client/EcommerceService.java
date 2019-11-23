package com.pos.ecommerce.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.exceptions.CreateException;
import com.pos.ecommerce.client.entitites.exceptions.LoginException;
import com.pos.ecommerce.client.entitites.exceptions.SaveOrderException;
import com.pos.ecommerce.client.entitites.exceptions.ProductException;

import java.util.List;

@RemoteServiceRelativePath("EcommerceService")
public interface EcommerceService extends RemoteService {

    UserDTO userOfsession();

    String login(String email, String password) throws LoginException;

    List<ItemDTO> getProduct(String text);

    OrderDTO sendOrder(OrderDTO order) throws CreateException, SaveOrderException;

    List<OrderDTO> getOrders();

    String logOut();

    String loginGuest();

    List<OrderDTO> getOrder(String value);

    OrderDTO confirmOrder(OrderDTO order) throws SaveOrderException;

    OrderDTO sendOrderGuest(OrderDTO order) throws CreateException, SaveOrderException;

    List<UserDTO> getUsers();

    List<UserDTO> getUser(String value);

    String sendOrderMP(OrderDTO order);

    List<ItemDTO> getStockFor(String code);

    ItemDTO updateProduct(ItemDTO itemDTO) throws ProductException;


    ItemDTO createNewProduct(ItemDTO itemDTO) throws ProductException;
}
