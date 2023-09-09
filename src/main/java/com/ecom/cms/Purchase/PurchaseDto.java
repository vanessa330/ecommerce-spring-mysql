package com.ecom.cms.Purchase;

import com.ecom.cms.Purchase.Item.ItemDto;
import com.ecom.cms.User.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseDto {

    Integer id;

    String status;

    BigDecimal subTotal;

    BigDecimal shippingCost;

    BigDecimal totalPrice;

    List<ItemDto> items; // table : item

    UserDto user; // table : user

    LocalDateTime createdDate;

    LocalDateTime updatedDate;
}
