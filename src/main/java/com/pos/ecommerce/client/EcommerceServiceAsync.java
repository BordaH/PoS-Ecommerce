package com.pos.ecommerce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.exceptions.LoginException;

import java.util.List;


public interface EcommerceServiceAsync {

    void userOfsession(AsyncCallback<UserDTO> booleanAsyncCallbackb);
    void login(String email, String password, AsyncCallback<String> stringAsyncCallback) throws LoginException;
    void getProduct(String text, AsyncCallback<List<ItemDTO>> listAsyncCallback);
    void sendOrder(OrderDTO order, AsyncCallback<OrderDTO> booleanAsyncCallback);
    void getOrders(AsyncCallback<List<OrderDTO>> listAsyncCallback);

    void logOut(AsyncCallback<String> userAsyncCallback);

    void loginGuest(AsyncCallback<String> stringAsyncCallback);

    void getOrder(String value, AsyncCallback<List<OrderDTO>> listAsyncCallback);

    void confirmOrder(OrderDTO order, AsyncCallback<OrderDTO> orderAsyncCallback);

    void sendOrderGuest(OrderDTO order, AsyncCallback<OrderDTO> orderAsyncCallback);

    void getUsers(AsyncCallback<List<UserDTO>> listAsyncCallback);

    void getUser(String value, AsyncCallback<List<UserDTO>> listAsyncCallback);
}
