package com.ecom.cms.Order;

import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.Order.OrderItem.OrderItem;
import com.ecom.cms.Order.OrderItem.OrderItemRepository;
import com.ecom.cms.Product.Product;
import com.ecom.cms.Product.ProductRepository;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import com.ecom.cms.User.User;
import com.ecom.cms.User.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addOrder(Map<String, Object> requestMap) {
        try {
            Order order = buildOrderFromMap(requestMap);
            orderRepository.save(order);

            return MainUtils.getResponseEntity("Order added successfully", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //        JSON requestMap
//    {
//        "orderItems": [{
//            "productId": 1, "quantity": 2
//        },
//        {
//            "productId": 2, "quantity": 3
//        }],
//         "userId": 1
//    }
    private Order buildOrderFromMap(Map<String, Object> requestMap) {

//        1. Build the OrderItem models.
        BigDecimal subTotal = BigDecimal.valueOf(0.00);

        List<OrderItem> orderItems = new ArrayList<>();
        for (Map<String, Object> orderItemMap : (List<Map<String, Object>>) requestMap.get("orderItems")) {

            Integer productId = ((Number) orderItemMap.get("productId")).intValue();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + productId));

            int quantity = ((Number) orderItemMap.get("quantity")).intValue();

            OrderItem newOrderItem = OrderItem.builder()
                    .quantity(quantity)
                    .pricePerUnit(product.getPrice())
                    .pricePerItem(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .product(product)
                    .build();

            subTotal = subTotal.add(newOrderItem.getPricePerItem());
            orderItems.add(newOrderItem);
            orderItemRepository.save(newOrderItem);
        }

//        2. Build the Order model
        BigDecimal shippingCost = BigDecimal.valueOf(30.00); // set default to $30

        Order newOrder = Order.builder()
                .status("pending")
                .subTotal(subTotal)
                .shippingCost(shippingCost)
                .totalPrice(subTotal.add(shippingCost))
                .createdDate(LocalDateTime.now())
                .build();

//        3. In the Order model, set the user
        int userId;
        if (requestMap.containsKey("userId")) {
            userId = ((Number) requestMap.get("userId")).intValue();
        } else {
            userId = 1; // Set default value to 1 for the guest
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        newOrder.setUser(user);

//        4. Update each orderItem order_id
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(newOrder);
        }

//        5. return to the addOrder()
        return newOrder;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        List<OrderDto> orderDtoList = orderList.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @Override
    public List<OrderDto> getByUser(int userId) {
        List<Order> orderList = orderRepository.findByUser(userId);
        List<OrderDto> orderDtoList = orderList.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, Object> requestMap) {
        try {
//            if (!isNew) {
//                order.setId(((Number) requestMap.get("id")).intValue());
//                order.setUpdatedDate(LocalDateTime.now());
//            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
