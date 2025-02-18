package com.example.Food_Ordering.repository;

import com.example.Food_Ordering.entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Tokens, UUID> {
    @Query("""
        SELECT t FROM Tokens t 
        WHERE t.user.id = :userId AND t.isLoggedOut = false
       """)
    List<Tokens> findAllTokensByUser(UUID userId);
    Optional<Tokens> findByToken(String token);
}
