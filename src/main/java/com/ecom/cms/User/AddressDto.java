package com.ecom.cms.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String company;

    private String phone;

    private String address1;

    private String address2;

    private String city;

    private String country;

    private String zipCode;

    private Boolean defaultAddress;

}
