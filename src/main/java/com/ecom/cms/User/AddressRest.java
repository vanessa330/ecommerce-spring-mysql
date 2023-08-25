package com.ecom.cms.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/address")
public class AddressRest {

    @Autowired
    AddressService addressService;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewAddress(@RequestBody Map<String, Object> requestMap) {
        return addressService.addNewAddress(requestMap);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updatedAddress(@RequestBody Map<String, Object> requestMap) {
        return addressService.updateAddress(requestMap);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") int addressId) {
        return addressService.deleteAddress(addressId);
    }
}