package com.ecom.cms.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {

     Integer id;

     String firstName;

     String lastName;

     String company;

     String phone;

     String address1;

     String address2;

     String city;

     String country;

     String zipCode;

     Boolean defaultAddress;

}
