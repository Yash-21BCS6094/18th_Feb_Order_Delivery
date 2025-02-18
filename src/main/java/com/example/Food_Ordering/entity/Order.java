package com.example.Food_Ordering.entity;

import com.example.Food_Ordering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "orderNum")
    private String orderNum;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // A user places multiple orders
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    // Many-to-Many Relationship with Products
    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}
