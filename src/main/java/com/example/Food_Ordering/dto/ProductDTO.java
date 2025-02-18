package com.example.Food_Ordering.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private Double price;
//    private OrderDTO orders;
}
