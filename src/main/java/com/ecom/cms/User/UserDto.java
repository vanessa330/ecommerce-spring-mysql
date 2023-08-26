package com.ecom.cms.User;

import com.ecom.cms.Image.ImageDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
//    Do not return the password & oneTimeToken

    Integer id;

    String name;

    String email;

    String status;

    String role;

    List<AddressDto> address; // table : address

    String wishlist;
}
