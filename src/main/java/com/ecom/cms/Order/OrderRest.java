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

    @GetMapping(path = "/get")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(path = "/getByUser/{id}")
    public List<OrderDto> getByUser(@PathVariable("id") int userId) {
        return orderService.getByUser(userId);
    }

    @PatchMapping(path = "/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> requestMap) {
        return orderService.updateStatus(requestMap);
    }

}
