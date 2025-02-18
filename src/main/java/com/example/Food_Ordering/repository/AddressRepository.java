package com.example.Food_Ordering.repository;

import com.example.Food_Ordering.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>{}
