package com.example.webshop.service;

import com.example.webshop.exception.ProductNotFoundException;
import com.example.webshop.model.Product;
import com.example.webshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void updateStock(Long productId, int newStock) {
        productRepository.updateStock(productId, newStock);
    }
}
