package com.example.Food_Ordering.service;

import com.example.Food_Ordering.dto.AddressDTO;
import java.util.UUID;

public interface AddressService {
    AddressDTO updateAddress(UUID addressId, AddressDTO addressDTO);
    AddressDTO getAddressById(UUID addressId);
    void deleteAddress(UUID addressId);
}
