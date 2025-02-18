package com.example.Food_Ordering.service;

import com.example.Food_Ordering.dto.OrderDTO;
import com.example.Food_Ordering.entity.Product;
import com.example.Food_Ordering.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(UUID orderId);
    OrderDTO updateOrder(UUID orderId, OrderDTO orderDTO);
    OrderDTO updateOrderStatus(UUID orderId, OrderStatus orderStatus);
    OrderDTO addProductsToOrder(UUID orderId, List<Product> products);
    OrderDTO deleteProductsFormOrder(UUID orderId, List<UUID> productIds);
    Page<OrderDTO> getAllOrder(Pageable pageable);
    Page<OrderDTO> getAllOrderByStatus(Pageable page, OrderStatus orderStatus);

    List<OrderDTO> getOrderByCustomerId(UUID customerId);
    void deleteOrder(UUID orderId);
    void cancelOrder(UUID orderId);
}
