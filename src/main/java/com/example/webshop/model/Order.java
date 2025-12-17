package com.example.webshop.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private CustomerInfo customerInfo;
    private List<OrderItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;

    public Order() {
    }

    public Order(Long id, CustomerInfo customerInfo, List<OrderItem> items, double totalAmount, LocalDateTime orderDate) {
        this.id = id;
        this.customerInfo = customerInfo;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
