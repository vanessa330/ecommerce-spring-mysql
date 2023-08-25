package com.ecom.cms.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM product WHERE category_id=:id", nativeQuery = true)
    List<Product> findByCategory(@Param("id") int id);

    @Query(value = "SELECT * FROM product WHERE brand_id=:id", nativeQuery = true)
    List<Product> findByBrand(@Param("id") int id);
}
