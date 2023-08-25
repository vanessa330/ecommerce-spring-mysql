package com.ecom.cms.Order;

import com.ecom.cms.Product.ProductDto;
import com.ecom.cms.User.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {

    Integer id;

    String orderNumber;

    String status;

    BigDecimal totalPrice;

    List<ProductDto> product; // find in table : product

    User user; // find in table : user

    LocalDateTime createdDate;

    LocalDateTime updatedDate;
}
