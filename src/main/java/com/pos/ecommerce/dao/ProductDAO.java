package com.pos.ecommerce.dao;

import com.pos.ecommerce.client.entitites.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> getProduct(String text);
}
