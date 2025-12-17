package com.example.webshop.repository;

import com.example.webshop.model.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderRepository {
    private final Map<Long, Order> orders = new HashMap<>();
    private long nextId = 1;

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(nextId++);
        }
        orders.put(order.getId(), order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
}
