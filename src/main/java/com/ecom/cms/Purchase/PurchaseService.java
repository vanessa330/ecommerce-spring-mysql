package com.ecom.cms.Purchase;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PurchaseService {

    ResponseEntity<String> addPurchase(Map<String, Object> requestMap);

    List<PurchaseDto> getAllPurchases();

    List<PurchaseDto> getByUser(int userId);

    ResponseEntity<String> updateStatus(Map<String, Object> requestMap);
}
