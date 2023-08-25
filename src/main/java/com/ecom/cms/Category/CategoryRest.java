package com.ecom.cms.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/category")
public class CategoryRest {

    @Autowired
    CategoryService categoryService;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewCategory(@RequestBody Map<String, Object> requestMap) {
        return categoryService.addNewCategory(requestMap);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<?> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateCategory(@RequestBody Map<String, Object> requestMap) {
        return categoryService.updateCategory(requestMap);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") int categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}