package com.ecom.cms.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query(value = "SELECT * FROM address WHERE user_id=:userId", nativeQuery = true)
    List<Address> findByUser(@Param("userId") int userId);

    @Query(value = "UPDATE address SET default_address=false WHERE user_id=:userId", nativeQuery = true)
    @Transactional
    @Modifying
    Integer clearDefaultAddress(@Param("userId") int userId);

}
