package com.ecom.cms.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public ResponseEntity<String> addOrder(Map<String, Object> requestMap) {
        return null;
    }

    @Override
    public List<OrderDto> getAllOrder() {
        return null;
    }

    @Override
    public List<OrderDto> getOrderByUser(int userId) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, Object> requestMap) {
        return null;
    }
}
