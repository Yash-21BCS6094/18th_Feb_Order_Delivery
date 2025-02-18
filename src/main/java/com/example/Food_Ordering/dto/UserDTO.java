package com.example.Food_Ordering.dto;

import com.example.Food_Ordering.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class UserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String username;
    private String role;
    @NotBlank
    private String password;
    private AddressDTO address;
    private List<OrderDTO> orders;
}
