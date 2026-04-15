package com.dealhub99.backend.repository;

import com.dealhub99.backend.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    
    // Fetch all leads for a specific seller
    List<Enquiry> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
    
    // Fetch inquiries made by a specific buyer
    List<Enquiry> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
    
    // Fetch inquiries for a specific product
    List<Enquiry> findByProductId(Long productId);
    
    // Check if buyer has already enquired about a product recently
    boolean existsByBuyerIdAndProductIdAndStatus(Long buyerId, Long productId, String status);
}
