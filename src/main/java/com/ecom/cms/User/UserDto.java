package com.ecom.cms.User;

import com.ecom.cms.Image.ImageDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
//    do not return the password & oneTimeToken

    private Integer id;

    private String name;

    private String email;

    private String status;

    private String role;

    List<AddressDto> address; // table : address

    private String wishlist;
}
