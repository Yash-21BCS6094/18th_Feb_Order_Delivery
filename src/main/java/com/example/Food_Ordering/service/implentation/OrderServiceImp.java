package com.example.Food_Ordering.service.implentation;

import com.example.Food_Ordering.dto.OrderDTO;
import com.example.Food_Ordering.dto.ProductDTO;
import com.example.Food_Ordering.entity.Order;
import com.example.Food_Ordering.entity.Product;
import com.example.Food_Ordering.entity.Users;
import com.example.Food_Ordering.enums.OrderStatus;
import com.example.Food_Ordering.exceptions.ResourceNotFoundException;
import com.example.Food_Ordering.repository.OrderProductRepository;
import com.example.Food_Ordering.repository.OrderRepository;
import com.example.Food_Ordering.repository.ProductRepository;
import com.example.Food_Ordering.repository.UserRepository;
import com.example.Food_Ordering.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final OrderProductRepository orderProductRepository;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           ModelMapper modelMapper,
                           OrderProductRepository orderProductRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Validate Customer
        Users user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Create Order Object
        Order order = new Order();
        order.setOrderNum(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.INITIATED);
        order.setUsers(user);

        if (orderDTO.getProducts() != null && !orderDTO.getProducts().isEmpty()) {
            List<Product> products = productRepository.findAllById(
                    orderDTO.getProducts().stream()
                            .map(ProductDTO::getId)  // Assuming orderDTO contains a list of ProductDTO
                            .toList()
            );
            if (products.size() != orderDTO.getProducts().size()) {
                throw new IllegalArgumentException("Some products not found");
            }
            order.setProducts(products);
        }

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);

    }

    @Override
    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(UUID orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderDTO.getId()));

        if (orderDTO.getStatus() != null) {
            existingOrder.setStatus(orderDTO.getStatus());
        }

        if (orderDTO.getProducts() != null && !orderDTO.getProducts().isEmpty()) {
            List<Product> products = productRepository.findAllById(
                    orderDTO.getProducts().stream().map(ProductDTO::getId).collect(Collectors.toList())
            );

            if (products.size() != orderDTO.getProducts().size()) {
                throw new IllegalArgumentException("Some products not found");
            }

            existingOrder.setProducts(products);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return modelMapper.map(updatedOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find order")
        );

        if(orderStatus != OrderStatus.DELIVERED){
            order.setStatus(orderStatus);
        }
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO addProductsToOrder(UUID orderId, List<Product> products) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find order")
        );

        List<Product> existingProducts = order.getProducts();
        existingProducts.addAll(products);
        order.setProducts(existingProducts);
        orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO deleteProductsFormOrder(UUID orderId, List<UUID> productIds) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find order")
        );

        List<Product> existingProducts = order.getProducts();
        existingProducts.removeIf(product -> productIds.contains(product.getId()));
        order.setProducts(existingProducts);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> getAllOrder(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(order -> modelMapper.map(order, OrderDTO.class));
    }

    @Override
    public Page<OrderDTO> getAllOrderByStatus(Pageable page, OrderStatus orderStatus) {
        Page<Order> ordersPage = orderRepository.findByStatus(orderStatus, page);

        // Step 2: Convert List<Order> to List<OrderDTO>
        return (Page<OrderDTO>) ordersPage.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class));
    }

    @Override
    public List<OrderDTO> getOrderByCustomerId(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find customer")
        );
        List<Order> orders = order.getUsers().getOrders();

        return orders.stream()
                .map(ord -> modelMapper.map(ord, OrderDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find order")
        );
        orderRepository.delete(order);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find order with ID: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has already been delivered.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
