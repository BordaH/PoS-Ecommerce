package com.pos.ecommerce.dao;

import com.pos.ecommerce.client.entitites.Client;
import com.pos.ecommerce.client.entitites.User;

import java.util.List;

public interface UserDAO {

    User getUserForEmail(String email);

    List<Client> getClients();
}
