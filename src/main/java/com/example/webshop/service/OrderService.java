package com.example.webshop.service;

import com.example.webshop.exception.InsufficientStockException;
import com.example.webshop.exception.InvalidOrderException;
import com.example.webshop.exception.ProductNotFoundException;
import com.example.webshop.model.*;
import com.example.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Order createOrder(OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productService.getProductById(itemRequest.getProductId());
            
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                    product.getId(), 
                    itemRequest.getQuantity(), 
                    product.getStock()
                );
            }

            OrderItem orderItem = new OrderItem(
                product.getId(),
                itemRequest.getQuantity(),
                product.getPrice()
            );
            orderItems.add(orderItem);
            totalAmount += product.getPrice() * itemRequest.getQuantity();

            int newStock = product.getStock() - itemRequest.getQuantity();
            productService.updateStock(product.getId(), newStock);
        }

        Order order = new Order();
        order.setCustomerInfo(orderRequest.getCustomerInfo());
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new InvalidOrderException("Order request cannot be null");
        }

        if (orderRequest.getCustomerInfo() == null) {
            throw new InvalidOrderException("Customer information is required");
        }

        CustomerInfo customerInfo = orderRequest.getCustomerInfo();
        if (customerInfo.getName() == null || customerInfo.getName().trim().isEmpty()) {
            throw new InvalidOrderException("Customer name is required");
        }
        if (customerInfo.getEmail() == null || customerInfo.getEmail().trim().isEmpty()) {
            throw new InvalidOrderException("Customer email is required");
        }
        if (customerInfo.getAddress() == null || customerInfo.getAddress().trim().isEmpty()) {
            throw new InvalidOrderException("Customer address is required");
        }

        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new InvalidOrderException("Order must contain at least one item");
        }

        for (OrderItemRequest item : orderRequest.getItems()) {
            if (item.getProductId() == null) {
                throw new InvalidOrderException("Product ID is required for all items");
            }
            if (item.getQuantity() <= 0) {
                throw new InvalidOrderException("Quantity must be greater than 0");
            }
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new InvalidOrderException("Order not found with id: " + id));
    }
}
