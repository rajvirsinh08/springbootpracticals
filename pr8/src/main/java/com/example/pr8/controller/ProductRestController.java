package com.example.pr8.controller;

import com.example.pr8.entity.Product;
import com.example.pr8.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create Product
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

    // Pagination and Sorting
    @GetMapping("/page")
    public Page<Product> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "productId") String sortBy) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortBy)
                );

        return productRepository.findAll(pageable);
    }

    // Get single product
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(
            @PathVariable int id) {

        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestBody Product updatedProduct) {

        return productRepository.findById(id)
                .map(product -> {

                    if (updatedProduct.getProductName() != null) {
                        product.setProductName(
                                updatedProduct.getProductName()
                        );
                    }

                    if (updatedProduct.getCategory() != null) {
                        product.setCategory(
                                updatedProduct.getCategory()
                        );
                    }

                    if (updatedProduct.getPrice() != 0) {
                        product.setPrice(
                                updatedProduct.getPrice()
                        );
                    }

                    if (updatedProduct.getQuantity() != 0) {
                        product.setQuantity(
                                updatedProduct.getQuantity()
                        );
                    }

                    Product savedProduct =
                            productRepository.save(product);

                    return ResponseEntity.ok(savedProduct);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable int id) {

        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}