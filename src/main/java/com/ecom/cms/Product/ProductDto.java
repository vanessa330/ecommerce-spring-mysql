package com.ecom.cms.Product;

import com.ecom.cms.Product.Image.ImageDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDto {

    Integer id;

    String name;

    BigDecimal price;

    Integer quantity;

    String color;

    Integer brandId; // table : brand

    String brandName;

    Integer categoryId; // table : category

    String categoryName;

    List<ImageDto> images; // table : image

    String material;

    String weight;

    String dimensions;

    LocalDateTime createdDate;

    LocalDateTime updatedDate;

}