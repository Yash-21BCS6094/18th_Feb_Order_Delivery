package com.example.Food_Ordering.config;

import com.example.Food_Ordering.service.JwtService;
import com.example.Food_Ordering.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        String requestURI = request.getRequestURI();

        // Skip authentication for login & signup
        if (requestURI.contains("/api/v1/auth/signup") || requestURI.contains("/api/v1/auth/login") || requestURI.contains("/api/v1/auth/log-out")) {
            filterChain.doFilter(request, response);
            return;
        }


        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }
        // I need to check that username here is not null and user is not already authenticated
        // SecurityContextHolder.getContext().getAuthentication() == null -> helps me check that current user is authenticated or not
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Here I need to pass the username of current provided token user and to get that I need UserDetails
            // which is implemented via Principal User
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);


            if(jwtService.validateToken(token, userDetails)){
                // Here I am getting the authToken from the provided Bearer token via UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Here I am setting the authentication context again back to the security filter chain
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request, response);

    }
}
