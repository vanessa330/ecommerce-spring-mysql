package com.ecom.cms.Order;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService {

    ResponseEntity<String> addOrder(Map<String, Object> requestMap);

    List<OrderDto> getAllOrder();

    List<OrderDto> getOrderByUser(int userId);

    ResponseEntity<String> updateStatus(Map<String, Object> requestMap);
}