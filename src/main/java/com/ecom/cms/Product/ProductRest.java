package com.ecom.cms.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/product")
public class ProductRest {

    @Autowired
    ProductService productService;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, Object> requestMap) {
        return productService.addNewProduct(requestMap);
    }

    @GetMapping("/get")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/getById/{id}")
    public ProductDto getProductById(@PathVariable("id") int productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/getByBrand/{id}")
    public List<ProductDto> getByBrand(@PathVariable("id") int brandId) {
        return productService.getByBrand(brandId);
    }

    @GetMapping(path = "/getByCategory/{id}")
    public List<ProductDto> getByCategory(@PathVariable("id") int categoryId) {
        return productService.getByCategory(categoryId);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String, Object> requestMap) {
        return productService.updateProduct(requestMap);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int productId) {
        return productService.deleteProduct(productId);
    }
}