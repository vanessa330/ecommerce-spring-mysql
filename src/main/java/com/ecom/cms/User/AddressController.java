package com.ecom.cms.User;

import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AddressController implements AddressService {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<String> addNewAddress(Map<String, Object> requestMap) {
        try {
            if ((boolean) requestMap.get("defaultAddress")) {
                addressRepository.clearDefaultAddress((int) requestMap.get("userId"));
            }

            Address address = getAddressFromMap(requestMap, true);
            addressRepository.save(address);

            return MainUtils.getResponseEntity("Address added successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //        JSON requestMap
//        {
//        "firstName": "first string",
//        "lastName": "last string",
//        "company": "string",
//        "phone": 66554433,
//        "address1": "address1",
//        "address2": "address2",
//        "city": "Tokyo",
//        "country": "Japan",
//        "zipCode": "1000",
//        "defaultAddress": true
//        "userId": 1,
//        "id": 1
//         }
    private Address getAddressFromMap(Map<String, Object> requestMap, boolean isNew) {
        Address address = Address.builder()
                .firstName(requestMap.get("firstName").toString())
                .lastName(requestMap.get("lastName").toString())
                .company(requestMap.get("company").toString())
                .phone(((Number) requestMap.get("phone")).intValue())
                .address1(requestMap.get("address1").toString())
                .address2(requestMap.get("address2").toString())
                .city(requestMap.get("city").toString())
                .country(requestMap.get("country").toString())
                .zipCode(requestMap.get("zipCode").toString())
                .defaultAddress((boolean) requestMap.get("defaultAddress"))
                .build();

        Integer userId = ((Number) requestMap.get("userId")).intValue();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        address.setUser(user);

        if (!isNew) { // updateAddress
            address.setId(((Number) requestMap.get("id")).intValue());
        }
        return address;
    }

    @Override
    public ResponseEntity<String> updateAddress(Map<String, Object> requestMap) {
        try {

            int addressId = ((Number) requestMap.get("id")).intValue();
            Optional<Address> optional = addressRepository.findById(addressId);
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Address id does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            if ((boolean) requestMap.get("defaultAddress")) {
                addressRepository.clearDefaultAddress((int) requestMap.get("userId"));
            }

            Address address = getAddressFromMap(requestMap, false);
            addressRepository.save(address);

            return MainUtils.getResponseEntity("Address updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<AddressDto> getAddressByUser(int userId) {
        List<Address> addressList = addressRepository.findByUser(userId);
        List<AddressDto> addressDtoList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .collect(Collectors.toList());
        return addressDtoList;
    }


    @Override
    public ResponseEntity<String> deleteAddress(int addressId) {
        try {
            Optional<Address> optional = addressRepository.findById(addressId);
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Address id does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            addressRepository.deleteById(addressId);

            return MainUtils.getResponseEntity("Address deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
