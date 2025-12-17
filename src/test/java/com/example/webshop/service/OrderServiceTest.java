package com.example.webshop.service;

import com.example.webshop.exception.InsufficientStockException;
import com.example.webshop.exception.InvalidOrderException;
import com.example.webshop.model.*;
import com.example.webshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Product testProduct;
    private CustomerInfo testCustomer;
    private OrderRequest validOrderRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1L, "Test Product", "Description", 100.0, "http://test.jpg", 10);
        testCustomer = new CustomerInfo("John Doe", "123 Main St", "john@example.com");
        
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        validOrderRequest = new OrderRequest(testCustomer, Arrays.asList(itemRequest));
    }

    @Test
    void createOrder_WithValidRequest_ShouldCreateOrder() {
        when(productService.getProductById(1L)).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        Order result = orderService.createOrder(validOrderRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(200.0, result.getTotalAmount());
        assertEquals(1, result.getItems().size());
        assertEquals(testCustomer.getName(), result.getCustomerInfo().getName());
        
        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).updateStock(1L, 8);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_WithInsufficientStock_ShouldThrowException() {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 20);
        OrderRequest orderRequest = new OrderRequest(testCustomer, Arrays.asList(itemRequest));
        
        when(productService.getProductById(1L)).thenReturn(testProduct);

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderRequest));
        verify(productService, never()).updateStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithNullCustomerInfo_ShouldThrowException() {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest orderRequest = new OrderRequest(null, Arrays.asList(itemRequest));

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithEmptyItems_ShouldThrowException() {
        OrderRequest orderRequest = new OrderRequest(testCustomer, Collections.emptyList());

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithInvalidQuantity_ShouldThrowException() {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 0);
        OrderRequest orderRequest = new OrderRequest(testCustomer, Arrays.asList(itemRequest));

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithMissingCustomerName_ShouldThrowException() {
        CustomerInfo invalidCustomer = new CustomerInfo("", "123 Main St", "john@example.com");
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest orderRequest = new OrderRequest(invalidCustomer, Arrays.asList(itemRequest));

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithMissingCustomerEmail_ShouldThrowException() {
        CustomerInfo invalidCustomer = new CustomerInfo("John Doe", "123 Main St", "");
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest orderRequest = new OrderRequest(invalidCustomer, Arrays.asList(itemRequest));

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_WithMissingCustomerAddress_ShouldThrowException() {
        CustomerInfo invalidCustomer = new CustomerInfo("John Doe", "", "john@example.com");
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest orderRequest = new OrderRequest(invalidCustomer, Arrays.asList(itemRequest));

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(orderRequest));
        verify(orderRepository, never()).save(any(Order.class));
    }
}
