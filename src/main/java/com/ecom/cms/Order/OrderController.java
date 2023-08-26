package com.ecom.cms.Order;

import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addOrder(Map<String, Object> requestMap) {
        try {
            if (!validateOrderMap(requestMap)) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Order order = getOrderFromMap(requestMap);
            orderRepository.save(order);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateOrderMap(Map<String, Object> requestMap) {
        if (requestMap.containsKey("totalPrice") && requestMap.containsKey(("product"))) {
            return true;
        } else {
            return false;
        }
    }


    //        JSON requestMap
//        {
//        "totalPrice": number,
//        "product": {[], []}
//         }
    private Order getOrderFromMap(Map<String, Object> requestMap) {
        Order order = Order.builder()
//                .totalPrice(requestMap.get("totalPrice"))
//                .product(requestMap.get("product"))
                .createdDate(LocalDateTime.now())
                .build();

        return order;
    }

    @Override
    public List<OrderDto> getAllOrder() {
        List<Order> orderList = orderRepository.findAll();
        List<OrderDto> orderDtoList = orderList.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @Override
    public List<OrderDto> getOrderByUser(int userId) {
        List<Order> orderList = orderRepository.findByUser(userId);
        List<OrderDto> orderDtoList = orderList.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, Object> requestMap) {
        try {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
