package com.ecom.cms.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query(value = "SELECT * FROM purchase WHERE user_id=:userId", nativeQuery = true)
    List<Purchase> findByUser(@Param("userId") int userId);

    @Query(value = "UPDATE purchase SET status=:status WHERE id=:id", nativeQuery = true)
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

}
