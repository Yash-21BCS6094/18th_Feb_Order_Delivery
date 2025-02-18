package com.example.Food_Ordering.service;

import com.example.Food_Ordering.entity.PrincipalUser;
import com.example.Food_Ordering.entity.Users;
import com.example.Food_Ordering.exceptions.ResourceNotFoundException;
import com.example.Food_Ordering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.rmi.server.LogStream.log;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log("I am in MyUserDetailsService and found user");
        Users users = userRepository.findByUsername(username);
        if(users == null){
            log("I am in MyUserDetailsService and cannot find user");
            throw new UsernameNotFoundException("user not found");
        }
        return new PrincipalUser(users);
    }
}
