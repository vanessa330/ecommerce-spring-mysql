package com.ecom.cms.Dashboard;

import com.ecom.cms.Category.CategoryRepository;
import com.ecom.cms.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController implements DashboardService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    //    PENDING...
    //    Return total number of Category & Product
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", categoryRepository.count());
        map.put("product", productRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
