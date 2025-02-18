package com.example.Food_Ordering.controller;

import com.example.Food_Ordering.dto.OrderDTO;
import com.example.Food_Ordering.entity.Product;
import com.example.Food_Ordering.enums.OrderStatus;
import com.example.Food_Ordering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderServices;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderServices.createOrder(orderDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable UUID orderId,
                                                @RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderServices.updateOrder(orderId, orderDTO), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN)")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderServices.getOrderById(orderId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {
        orderServices.deleteOrder(orderId);
        return new ResponseEntity<>("Order Deleted", HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(orderServices.getAllOrder(pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByStatus(@RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @PathVariable OrderStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(orderServices.getAllOrderByStatus(pageable, status), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        return new ResponseEntity<>(orderServices.getOrderByCustomerId(customerId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable UUID orderId,
                                                      @RequestParam OrderStatus status) {

        return new ResponseEntity<>(orderServices.updateOrderStatus(orderId, status), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId) {
        orderServices.cancelOrder(orderId);
        return new ResponseEntity<>("Cancelled order successfully", HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{orderId}/products")
    public ResponseEntity<OrderDTO> addProductsToOrder(@PathVariable UUID orderId,
                                                       @RequestBody List<Product> products) {
        return new ResponseEntity<>(orderServices.addProductsToOrder(orderId, products), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{orderId}/products")
    public ResponseEntity<OrderDTO> removeProductsFromOrder(@PathVariable UUID orderId,
                                                            @RequestBody List<UUID> productIds) {
        return new ResponseEntity<>(orderServices.deleteProductsFormOrder(orderId, productIds), HttpStatus.OK);
    }

}
