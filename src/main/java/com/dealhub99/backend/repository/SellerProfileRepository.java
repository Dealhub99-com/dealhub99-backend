package com.dealhub99.backend.repository;

import com.dealhub99.backend.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    
    // Fetch profile by user ID
    Optional<SellerProfile> findByUserId(Long userId);
    
    // Fetch profile by business name
    Optional<SellerProfile> findByBusinessName(String businessName);

    // Global Seller Search: Search by business name and location (City)
    List<SellerProfile> findByBusinessNameContainingIgnoreCaseAndCityContainingIgnoreCaseAndActiveTrue(String businessName, String city);
    
    // Fetch all active profiles for shop listing
    List<SellerProfile> findByActiveTrue();
}
