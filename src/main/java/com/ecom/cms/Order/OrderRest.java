package com.ecom.cms.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/order")
public class OrderRest {

    @Autowired
    OrderService orderService;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addOrder(@RequestBody Map<String, Object> requestMap) {
        return orderService.addOrder(requestMap);
    }

    @GetMapping(path = "get")
    public List<OrderDto> getAllOrder() {
        return orderService.getAllOrder();
    }

    @GetMapping(path = "get/{id}")
    public List<OrderDto> getOrderByUser(@PathVariable("id") int userId) {
        return orderService.getOrderByUser(userId);
    }

    @PatchMapping(path = "updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> requestMap) {
        return orderService.updateStatus(requestMap);
    }

}
