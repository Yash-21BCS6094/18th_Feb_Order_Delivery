package com.example.Food_Ordering.service;

import com.example.Food_Ordering.dto.AuthenticationRequest;
import com.example.Food_Ordering.dto.AuthenticationResponse;
import com.example.Food_Ordering.dto.UserDTO;
import com.example.Food_Ordering.entity.Address;
import com.example.Food_Ordering.entity.Tokens;
import com.example.Food_Ordering.entity.Users;
import com.example.Food_Ordering.enums.Role;
import com.example.Food_Ordering.exceptions.ResourceNotFoundException;
import com.example.Food_Ordering.repository.TokenRepository;
import com.example.Food_Ordering.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager manager;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 JwtService jwtService,
                                 ModelMapper modelMapper,
                                 AuthenticationManager manager,
                                 TokenRepository tokenRepository){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.manager = manager;
        this.tokenRepository = tokenRepository;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // While registering the user
    public AuthenticationResponse register(UserDTO userDTO){
        Users user = new Users();
        user.setAddress(modelMapper.map(userDTO.getAddress(), Address.class));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        System.out.println(userDTO.getRole());
        if(userDTO.getRole().equalsIgnoreCase("USER")){
            user.setRole(Role.USER);
            System.out.println("**** " + user.getRole());
        }else if(userDTO.getRole().equalsIgnoreCase("ADMIN")){
            user.setRole(Role.ADMIN);
            System.out.println("### " + user.getRole());
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.refreshToken(user);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        // Now I need to save the token generated in token table corresponding to
        // the user [for both jwt and refresh token]
        Tokens jwtTok = new Tokens();
        jwtTok.setToken(jwtToken);
        jwtTok.setLoggedOut(false);
        jwtTok.setUser(user);
        tokenRepository.save(jwtTok);

        Tokens refTok = new Tokens();
        refTok.setToken(refreshToken);
        refTok.setLoggedOut(false);
        refTok.setUser(user);
        tokenRepository.save(refTok);

        // While registering we are building an authentication response and
        // then passing the user jwtToken and refresh token
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);
        response.setRefreshToken(refreshToken);

        return response;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ));

        Users user = userRepository.findByUsername(authenticationRequest.getUsername());

        AuthenticationResponse response = new AuthenticationResponse();
        if(authentication.isAuthenticated()){
            response.setMessage("User authenticated");
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.refreshToken(user);
            response.setToken(jwtToken);
            response.setRefreshToken(refreshToken);
            Tokens jwtTok = new Tokens();
            jwtTok.setToken(jwtToken);
            jwtTok.setLoggedOut(false);
            jwtTok.setUser(user);

            // Setting all the tokens as false before giving the new one

            List<Tokens> validTokenList = tokenRepository.findAllTokensByUser(user.getId());

            if(!validTokenList.isEmpty()){
                validTokenList.forEach(t -> {
                    t.setLoggedOut(true);
                });
            }
            validTokenList.add(jwtTok);
            tokenRepository.saveAll(validTokenList);

        }else{
            response.setMessage("User not verified");
        }
        return response;
    }

    public AuthenticationResponse refreshToken(AuthenticationRequest authenticationRequest){

        Users user = userRepository.findByUsername(authenticationRequest.getUsername());

        if(user == null){
            throw new ResourceNotFoundException("Cannot find user");
        }

        String newRefreshToken = jwtService.generateToken(user);
        String jwtToken = jwtService.refreshToken(user);

        // This refresh token method is used when the token expires user can give a
        // refresh token and generate a new token from the server.
        AuthenticationResponse response = new AuthenticationResponse();
        response.setRefreshToken(newRefreshToken);
        response.setToken(jwtToken);
        return response;
    }

    public AuthenticationResponse logOutUser(AuthenticationRequest authenticationRequest){
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ));

        System.out.println("I am in logOutUser");
        Users user = userRepository.findByUsername(authenticationRequest.getUsername());

        AuthenticationResponse response = new AuthenticationResponse();
        if(authentication.isAuthenticated()){
            response.setMessage("User authenticated");

            List<Tokens> validTokenList = tokenRepository.findAllTokensByUser(user.getId());

            if(!validTokenList.isEmpty()){
                validTokenList.forEach(t -> {
                    t.setLoggedOut(true);
                });
            }
            response.setToken("Generate via registering");
            response.setRefreshToken("Please register first");
            response.setMessage("You are logged Out");
            tokenRepository.saveAll(validTokenList);

        }else{
            response.setMessage("User not verified");
        }
        System.out.println(response);
        return response;

    }

}