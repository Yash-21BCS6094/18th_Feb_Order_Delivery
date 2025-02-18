package com.example.Food_Ordering.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class AddressDTO {
    private UUID id;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @JsonIgnore
    private UserDTO user;
}
