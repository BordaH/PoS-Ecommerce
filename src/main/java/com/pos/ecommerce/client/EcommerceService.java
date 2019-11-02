package com.pos.ecommerce.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.exceptions.LoginException;

import java.util.List;

@RemoteServiceRelativePath("EcommerceService")
public interface EcommerceService extends RemoteService {

    UserDTO userOfsession();

    String login(String email, String password) throws LoginException;

    List<ItemDTO> getProduct(String text);

    OrderDTO sendOrder(OrderDTO order);

    List<OrderDTO> getOrders();

    String logOut();

    String loginGuest();

    List<OrderDTO> getOrder(String value);

    OrderDTO confirmOrder(OrderDTO order);

    OrderDTO sendOrderGuest(OrderDTO order);

    List<UserDTO> getUsers();

    List<UserDTO> getUser(String value);

}
