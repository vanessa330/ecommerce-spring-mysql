package com.ecom.cms.Order.OrderItem;

import com.ecom.cms.Product.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto {

    Integer id;

    Integer quantity;

    BigDecimal pricePerUnit;

    BigDecimal pricePerItem;

    ProductDto product; // table : product

}
