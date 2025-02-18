package com.example.Food_Ordering.service.implentation;

import com.example.Food_Ordering.dto.OrderDTO;
import com.example.Food_Ordering.dto.UserDTO;
import com.example.Food_Ordering.entity.Users;
import com.example.Food_Ordering.exceptions.ResourceNotFoundException;
import com.example.Food_Ordering.repository.AddressRepository;
import com.example.Food_Ordering.repository.ProductRepository;
import com.example.Food_Ordering.repository.UserRepository;
import com.example.Food_Ordering.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Users users = modelMapper.map(userDTO, Users.class);
        Users savedUsers = userRepository.save(users);
        return modelMapper.map(savedUsers, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(UUID id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        UserDTO userDTO = modelMapper.map(users, UserDTO.class);
        userDTO.setPassword("");
        return userDTO;
    }

    @Override
    public Page<UserDTO> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).
                map(customer -> modelMapper.map(customer, UserDTO.class));
    }

    @Override
    public UserDTO updateUser(UUID customerId, UserDTO updatedCustomer) {
        Users users = userRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find customer")
        );
        Users updatedCustomerEntity = modelMapper.map(updatedCustomer, Users.class);
        users.setAddress(updatedCustomerEntity.getAddress());
        users.setFirstName(updatedCustomerEntity.getFirstName());
        users.setLastName(updatedCustomerEntity.getLastName());
        users.setUsername(updatedCustomerEntity.getUsername());

        Users savedUsers = userRepository.save(users);
        return modelMapper.map(savedUsers, UserDTO.class);
    }

    @Override
    public void deleteUser(UUID id) {
        Users users = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Cannot find customer"));
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        Users users = userRepository.findByUsername(email);
        if(users == null){
            throw new UsernameNotFoundException("Cannot find username in UserServiceImp");
        }
        return modelMapper.map(users, UserDTO.class);
    }

    @Override
    public List<UserDTO> searchUserByName(String name) {
        List<Users> users = userRepository.findByFirstNameContainingIgnoreCase(name);
        List<UserDTO> userDTOS = (List<UserDTO>) users.stream().map((customer) -> modelMapper.map(customer, UserDTO.class));
        return userDTOS;
    }

    @Override
    public List<OrderDTO> getAllOrder(UUID id) {
        Users users = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find customer")
        );

        return users.getOrders().stream().map(order -> modelMapper.map(order, OrderDTO.class)).
                toList();
    }

}
