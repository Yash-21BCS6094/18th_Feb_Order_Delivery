package com.example.Food_Ordering.dto;

import com.example.Food_Ordering.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID id;
    @NotBlank
    private String orderNum;
    @NotBlank
    private OrderStatus status;
    private UserDTO user;
    private UUID userId;
    @NotNull
    private List<ProductDTO> products;
}
