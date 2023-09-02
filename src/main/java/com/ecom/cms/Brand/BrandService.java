package com.ecom.cms.Brand;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface BrandService {

    ResponseEntity<String> addNewBrand(Map<String, Object> requestMap);

    ResponseEntity<?> getAllBrand();

    ResponseEntity<String> updateBrand(Map<String, Object> requestMap);

    ResponseEntity<String> deleteBrand(int categoryId);

}
