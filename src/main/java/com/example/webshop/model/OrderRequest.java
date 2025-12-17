package com.example.webshop.model;

import java.util.List;

public class OrderRequest {
    private CustomerInfo customerInfo;
    private List<OrderItemRequest> items;

    public OrderRequest() {
    }

    public OrderRequest(CustomerInfo customerInfo, List<OrderItemRequest> items) {
        this.customerInfo = customerInfo;
        this.items = items;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
