package com.example.pr2.controller;

import com.example.pr2.entity.Product;
import com.example.pr2.repository.ProductRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product) {

        Product savedProduct =
                productRepository.save(product);

        return new ResponseEntity<>(
                savedProduct,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Iterable<Product>> getAllProducts() {

        return ResponseEntity.ok(
                productRepository.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable int id) {

        Optional<Product> product =
                productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable int id) {

        Optional<Product> product =
                productRepository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sorted")
    public ResponseEntity<Iterable<Product>> getSortedProducts(
            @RequestParam(defaultValue = "price") String sortBy) {

        Sort sort = Sort.by(sortBy);

        return ResponseEntity.ok(
                productRepository.findAll(sort)
        );
    }

    @PostMapping("/batch")
    public ResponseEntity<Iterable<Product>> createProducts(
            @RequestBody Iterable<Product> products) {

        Iterable<Product> savedProducts =
                productRepository.saveAll(products);

        return new ResponseEntity<>(
                savedProducts,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/batch/flush")
    public ResponseEntity<Iterable<Product>> createProductsAndFlush(
            @RequestBody Iterable<Product> products) {

        Iterable<Product> savedProducts =
                productRepository.saveAll(products);

        productRepository.flush();

        return new ResponseEntity<>(
                savedProducts,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestBody Product productDetails) {

        Optional<Product> existingProduct =
                productRepository.findById(id);

        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = existingProduct.get();

        if (productDetails.getProductName() != null) {
            product.setProductName(
                    productDetails.getProductName()
            );
        }

        if (productDetails.getCategory() != null) {
            product.setCategory(
                    productDetails.getCategory()
            );
        }

        if (productDetails.getPrice() != 0) {
            product.setPrice(
                    productDetails.getPrice()
            );
        }

        if (productDetails.getQuantity() != 0) {
            product.setQuantity(
                    productDetails.getQuantity()
            );
        }

        Product updatedProduct =
                productRepository.save(product);

        return ResponseEntity.ok(updatedProduct);
    }
}