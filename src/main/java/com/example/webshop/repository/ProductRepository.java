package com.example.webshop.repository;

import com.example.webshop.model.Product;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductRepository {
    private final Map<Long, Product> products = new HashMap<>();
    private long nextId = 1;

    public ProductRepository() {
        initializeProducts();
    }

    private void initializeProducts() {
        addProduct(new Product(null, "Laptop", "High-performance laptop for professionals", 12999.99, "https://example.com/laptop.jpg", 15));
        addProduct(new Product(null, "Smartphone", "Latest smartphone with advanced features", 7999.99, "https://example.com/smartphone.jpg", 25));
        addProduct(new Product(null, "Headphones", "Wireless noise-cancelling headphones", 1999.99, "https://example.com/headphones.jpg", 40));
        addProduct(new Product(null, "Keyboard", "Mechanical keyboard for gaming and typing", 899.99, "https://example.com/keyboard.jpg", 30));
        addProduct(new Product(null, "Mouse", "Ergonomic wireless mouse", 499.99, "https://example.com/mouse.jpg", 50));
        addProduct(new Product(null, "Monitor", "27-inch 4K display monitor", 3499.99, "https://example.com/monitor.jpg", 20));
        addProduct(new Product(null, "Webcam", "HD webcam for video conferencing", 799.99, "https://example.com/webcam.jpg", 35));
        addProduct(new Product(null, "External SSD", "1TB portable external SSD", 1299.99, "https://example.com/ssd.jpg", 45));
    }

    private void addProduct(Product product) {
        product.setId(nextId++);
        products.put(product.getId(), product);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public void updateStock(Long id, int newStock) {
        Product product = products.get(id);
        if (product == null) {
            throw new IllegalArgumentException("Cannot update stock for non-existent product id: " + id);
        }
        product.setStock(newStock);
    }
}
