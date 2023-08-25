package com.ecom.cms.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, Object> requestMap);

    ResponseEntity<?> getAllCategory();

    ResponseEntity<String> updateCategory(Map<String, Object> requestMap);

    ResponseEntity<String> deleteCategory(int categoryId);

}
