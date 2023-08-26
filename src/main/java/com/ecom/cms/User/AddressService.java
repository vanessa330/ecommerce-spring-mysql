package com.ecom.cms.User;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AddressService {
    ResponseEntity<String> addNewAddress(Map<String, Object> requestMap);

    ResponseEntity<String> updateAddress(Map<String, Object> requestMap);

    List<AddressDto> getAddressByUser(int userId);

    ResponseEntity<String> deleteAddress(int addressId);
}
