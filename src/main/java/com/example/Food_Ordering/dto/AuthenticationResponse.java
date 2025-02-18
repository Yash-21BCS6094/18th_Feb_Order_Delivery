package com.example.Food_Ordering.dto;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
    private String message;
}
