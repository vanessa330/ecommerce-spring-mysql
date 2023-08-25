package com.ecom.cms.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM user WHERE email=:email", nativeQuery = true)
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT email FROM user WHERE role='admin'", nativeQuery = true)
    List<String> getAllAdminEmail();

    @Query(value = "UPDATE user SET status=:status WHERE id=:id", nativeQuery = true)
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Query(value = "UPDATE user SET role=:role WHERE id=:id", nativeQuery = true)
    @Transactional
    @Modifying
    Integer updateRole(@Param("role") String role, @Param("id") Integer id);

    @Query(value = "UPDATE user SET wishlist=:wishlist WHERE email=:email", nativeQuery = true)
    @Transactional
    @Modifying
    Integer updateWishlist(@Param("wishlist") String wishlist, @Param("email") String email);
}
