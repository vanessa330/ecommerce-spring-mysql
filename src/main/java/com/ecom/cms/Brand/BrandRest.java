package com.ecom.cms.Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/brand")
public class BrandRest {

    @Autowired
    BrandService brandService;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewBrand(@RequestBody Map<String, Object> requestMap) {
        return brandService.addNewBrand(requestMap);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<?> getAllBrand() {
        return brandService.getAllBrand();
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateBrand(@RequestBody Map<String, Object> requestMap) {
        return brandService.updateBrand(requestMap);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable("id") int brandId) {
        return brandService.deleteBrand(brandId);
    }
}