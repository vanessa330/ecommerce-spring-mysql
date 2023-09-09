package com.ecom.cms.Purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/purchase")
public class PurchaseRest {

    @Autowired
    PurchaseService purchaseService;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addPurchase(@RequestBody Map<String, Object> requestMap) {
        return purchaseService.addPurchase(requestMap);
    }

    @GetMapping(path = "/get")
    public List<PurchaseDto> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping(path = "/getByUser/{id}")
    public List<PurchaseDto> getByUser(@PathVariable("id") int userId) {
        return purchaseService.getByUser(userId);
    }

    @PatchMapping(path = "/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> requestMap) {
        return purchaseService.updateStatus(requestMap);
    }

}
