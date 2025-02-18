package com.example.Food_Ordering.config;

import com.example.Food_Ordering.entity.Tokens;
import com.example.Food_Ordering.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        String token = authHeader.substring(7);
        // Get stored token from the database
        Tokens storedToken = tokenRepository.findByToken(token).orElse(null);
        // Mark the token as logout

        if(token != null){
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }

        // save it to database

    }
}
