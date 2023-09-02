package com.ecom.cms.Product;

import com.ecom.cms.Brand.Brand;
import com.ecom.cms.Image.Image;
import com.ecom.cms.Image.ImageRepository;
import com.ecom.cms.JWT.JwtFilter;
import com.ecom.cms.Category.Category;
import com.ecom.cms.UTILS.MainConstants;
import com.ecom.cms.UTILS.MainUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProductController implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            if (!validateProductMap(requestMap)) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Product product = getProductFromMap(requestMap, true);
            productRepository.save(product);

            // Update Image Schema product_id if imageIds value is present
            ObjectMapper mapper = new ObjectMapper();
            List<Integer> imageIds = mapper.convertValue(requestMap.get("imageIds"), new TypeReference<List<Integer>>() {
            });
            if (!imageIds.isEmpty()) {
                updateImageProductId(imageIds, product.getId());
            }

            return MainUtils.getResponseEntity("Product added successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, Object> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("price") && requestMap.containsKey("quantity")) {
            return true;
        } else {
            return false;
        }
    }

    //        JSON requestMap
//        {
//        "name": "string",
//        "price": 1234.56,
//        "quantity": 10,
//        "color": "red",
//        "brandId": 1,
//        "categoryId": 2,
//        "material": "titanium",
//        "weight": "120g",
//        "dimensions": "23cm(H) x 8cm(D)",
//        "id": 8
//        "imageIds": [1,2,3]
//         }
    private Product getProductFromMap(Map<String, Object> requestMap, boolean isNew) {
        BigDecimal price = new BigDecimal(requestMap.get("price").toString());

        Product product = Product.builder()
                .name(requestMap.get("name").toString())
                .price(price)
                .quantity(((Number) requestMap.get("quantity")).intValue())
                .color(requestMap.get("color").toString())
                .brand(Brand.builder().id(((Number) requestMap.get("brandId")).intValue()).build())
                .category(Category.builder().id(((Number) requestMap.get("categoryId")).intValue()).build())
                .material(requestMap.get("material").toString())
                .weight(requestMap.get("weight").toString())
                .dimensions(requestMap.get("dimensions").toString())
                .createdDate(LocalDateTime.now())
                .build();

        if (!isNew) { // updateProduct() setId()
            product.setId(((Number) requestMap.get("id")).intValue());
            product.setUpdatedDate(LocalDateTime.now());
        }
        return product;
    }

    private void updateImageProductId(List<Integer> imageIds, int productId) {
        for (int imageId : imageIds) {
            Optional<Image> optional = imageRepository.findById(imageId);
            if (optional.isPresent()) {
                imageRepository.updateProductId(imageId, productId);
            }
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public ProductDto getProductById(int productId) {
        Product product = productRepository.findById(productId).get();
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public List<ProductDto> getByCategory(int categoryId) {
        List<Product> productList = productRepository.findByCategory(categoryId);
        List<ProductDto> productDtoList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public List<ProductDto> getByBrand(int brandId) {
        List<Product> productList = productRepository.findByBrand(brandId);
        List<ProductDto> productDtoList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            if (!validateProductMap(requestMap)) {
                return MainUtils.getResponseEntity(MainConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            int productId = ((Number) requestMap.get("id")).intValue();
            Optional<Product> optional = productRepository.findById(productId);
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Product id does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            Product product = getProductFromMap(requestMap, false);
            productRepository.save(product);

            // Update Image Schema product_id if imageIds value is present
            ObjectMapper mapper = new ObjectMapper();
            List<Integer> imageIds = mapper.convertValue(requestMap.get("imageIds"), new TypeReference<List<Integer>>() {
            });
            if (!imageIds.isEmpty()) {
                updateImageProductId(imageIds, productId);
            }

            return MainUtils.getResponseEntity("Product updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(int productId) {
        try {
            if (!jwtFilter.isAdmin()) {
                return MainUtils.getResponseEntity(MainConstants.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED);
            }

            Optional<Product> optional = productRepository.findById(productId);
            if (!optional.isPresent()) {
                return MainUtils.getResponseEntity("Product id does not exist.", HttpStatus.NOT_ACCEPTABLE);
            }

            productRepository.deleteById(productId); // also delete associated images

            return MainUtils.getResponseEntity("Product deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MainUtils.getResponseEntity(MainConstants.ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
