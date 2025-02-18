package com.example.Food_Ordering.repository;

import com.example.Food_Ordering.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    List<Users> findByFirstNameContainingIgnoreCase(String firstName);
    Users     findByUsername(String username);
}
