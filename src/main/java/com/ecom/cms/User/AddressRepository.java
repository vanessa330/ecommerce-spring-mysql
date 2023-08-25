package com.ecom.cms.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
//    @Query(value = "SELECT * FROM address WHERE userId=:userId", nativeQuery = true)
//    List<Address> getByUser();

}
