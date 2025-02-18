package com.example.Food_Ordering.service;

import com.example.Food_Ordering.dto.OrderDTO;
import com.example.Food_Ordering.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(UUID id);
    Page<UserDTO> getAllUser(Pageable pageable);
    UserDTO updateUser(UUID customerId, UserDTO updatedCustomer);
    void deleteUser(UUID customerId);
    public UserDTO getUserByEmail(String email);
    List<UserDTO> searchUserByName(String name);
    List<OrderDTO> getAllOrder(UUID id);
}
