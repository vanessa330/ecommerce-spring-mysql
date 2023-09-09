package com.ecom.cms.Product.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "UPDATE image SET product_id=:productId WHERE id=:id", nativeQuery = true)
    @Transactional
    @Modifying
    Integer updateProductId(@Param("id") Integer id, @Param("productId") Integer productId);
}
