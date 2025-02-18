package com.example.Food_Ordering.controller;

import com.example.Food_Ordering.dto.AddressDTO;
import com.example.Food_Ordering.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    @Autowired
    private final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    // updating address
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable UUID addressId,
                                                    @RequestBody AddressDTO addressDTO) {
        AddressDTO add = addressService.updateAddress(addressId, addressDTO);

        return new ResponseEntity<>(add, HttpStatus.OK);
    }

    // Getting an address
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID addressId) {
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    // Delete the address
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable UUID addressId) {
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>("Address deleted succeddfully", HttpStatus.NO_CONTENT);
    }

}
