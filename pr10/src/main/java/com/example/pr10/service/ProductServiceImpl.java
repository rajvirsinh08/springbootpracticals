package com.example.pr10.service;

import com.example.pr10.entity.Product;
import com.example.pr10.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create
    @Override
    public Product create(Product product) {

        return productRepository.save(product);
    }

    // Get all
    @Override
    public List<Product> getAll() {

        return productRepository.findAll();
    }

    // Get by ID
    @Override
    public Product getById(int id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: " + id
                        )
                );
    }

    // Update
    @Override
    public Product update(int id, Product updatedProduct) {

        Product existingProduct =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with id: " + id
                                )
                        );

        if (updatedProduct.getProductName() != null) {
            existingProduct.setProductName(
                    updatedProduct.getProductName()
            );
        }

        if (updatedProduct.getCategory() != null) {
            existingProduct.setCategory(
                    updatedProduct.getCategory()
            );
        }

        if (updatedProduct.getPrice() != 0) {
            existingProduct.setPrice(
                    updatedProduct.getPrice()
            );
        }

        if (updatedProduct.getQuantity() != 0) {
            existingProduct.setQuantity(
                    updatedProduct.getQuantity()
            );
        }

        return productRepository.save(existingProduct);
    }

    // Delete
    @Override
    public void delete(int id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException(
                    "Product not found with id: " + id
            );
        }

        productRepository.deleteById(id);
    }
}