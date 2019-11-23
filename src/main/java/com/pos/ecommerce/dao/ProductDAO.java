package com.pos.ecommerce.dao;

import com.pos.ecommerce.client.entitites.Item;
import com.pos.ecommerce.client.entitites.Product;
import com.pos.ecommerce.client.entitites.exceptions.ProductException;

import java.util.List;

public interface ProductDAO {
    List<Product> getProduct(String text);

    List<Product> getProductForCode(String code);

    void updateStocks(List<Item> items);

    void updateStock(Product product) throws ProductException;

    Product createProduct(Product product) throws ProductException;
}
