package com.ecom.cms.Product;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String, Object> requestMap);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(int productId);

    List<ProductDto> getByCategory(int categoryId);

    List<ProductDto> getByBrand(int brandId);

    ResponseEntity<String> updateProduct(Map<String, Object> requestMap);

    ResponseEntity<String> deleteProduct(int productId);


}
