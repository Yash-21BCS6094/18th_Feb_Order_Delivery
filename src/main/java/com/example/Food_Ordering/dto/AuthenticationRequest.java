package com.example.Food_Ordering.dto;
import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
