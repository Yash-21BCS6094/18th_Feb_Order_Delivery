package com.example.Food_Ordering.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "order_prod_details")
@Data
public class OrderProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private UUID prodId;

    @NotNull
    private UUID orderId;

    @NotNull
    private Long quantity;

}
