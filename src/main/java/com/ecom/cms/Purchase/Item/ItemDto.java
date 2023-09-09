package com.ecom.cms.Purchase.Item;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ItemDto {

    Integer id;

    Integer productId;

    String productName;

    BigDecimal productPrice;

    Integer quantity;

    BigDecimal pricePerItem;

}
