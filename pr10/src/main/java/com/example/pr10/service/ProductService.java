package com.example.pr10.service;

import com.example.pr10.entity.Product;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> getAll();

    Product getById(int id);

    Product update(int id, Product product);

    void delete(int id);
}