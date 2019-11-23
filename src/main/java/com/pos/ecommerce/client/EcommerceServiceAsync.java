package com.pos.ecommerce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pos.ecommerce.client.dto.ItemDTO;
import com.pos.ecommerce.client.dto.OrderDTO;
import com.pos.ecommerce.client.dto.UserDTO;
import com.pos.ecommerce.client.entitites.exceptions.CreateException;
import com.pos.ecommerce.client.entitites.exceptions.LoginException;
import com.pos.ecommerce.client.entitites.exceptions.SaveOrderException;
import com.pos.ecommerce.client.entitites.exceptions.ProductException;

import java.util.List;


public interface EcommerceServiceAsync {

    void userOfsession(AsyncCallback<UserDTO> booleanAsyncCallbackb);
    void login(String email, String password, AsyncCallback<String> stringAsyncCallback) throws LoginException;
    void getProduct(String text, AsyncCallback<List<ItemDTO>> listAsyncCallback);
    void sendOrder(OrderDTO order, AsyncCallback<OrderDTO> booleanAsyncCallback) throws CreateException, SaveOrderException;
    void getOrders(AsyncCallback<List<OrderDTO>> listAsyncCallback);

    void logOut(AsyncCallback<String> userAsyncCallback);

    void loginGuest(AsyncCallback<String> stringAsyncCallback);

    void getOrder(String value, AsyncCallback<List<OrderDTO>> listAsyncCallback);

    void confirmOrder(OrderDTO order, AsyncCallback<OrderDTO> orderAsyncCallback) throws SaveOrderException;

    void sendOrderGuest(OrderDTO order, AsyncCallback<OrderDTO> orderAsyncCallback) throws CreateException, SaveOrderException;

    void getUsers(AsyncCallback<List<UserDTO>> listAsyncCallback);

    void getUser(String value, AsyncCallback<List<UserDTO>> listAsyncCallback);

    void sendOrderMP(OrderDTO order, AsyncCallback<String> orderDTOAsyncCallback);

    void getStockFor(String code, AsyncCallback<List<ItemDTO>> listAsyncCallback);

    void updateProduct(ItemDTO itemDTO, AsyncCallback<ItemDTO> itemDTOAsyncCallback) throws ProductException;

    void createNewProduct(ItemDTO itemDTO, AsyncCallback<ItemDTO> itemDTOAsyncCallback) throws ProductException;
}
