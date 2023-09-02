package com.ecom.cms.Order;

import com.ecom.cms.Order.OrderItem.OrderItemDto;
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

    String status;

    BigDecimal subTotal;

    BigDecimal shippingCost;

    BigDecimal totalPrice;

    List<OrderItemDto> orderItems; // table : order_item

    User user; // table : user

    LocalDateTime createdDate;

    LocalDateTime updatedDate;
}
