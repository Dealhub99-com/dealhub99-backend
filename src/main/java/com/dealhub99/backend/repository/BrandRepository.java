package com.dealhub99.backend.repository;

import com.dealhub99.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    
    // Fetch all active brands
    List<Brand> findByActiveTrue();
    
    // Search brands by name
    List<Brand> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}
