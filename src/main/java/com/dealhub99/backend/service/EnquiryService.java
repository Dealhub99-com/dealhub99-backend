package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.Enquiry;
import java.util.List;

public interface EnquiryService {
    Enquiry createEnquiry(Long buyerId, Long productId, String message);
    List<Enquiry> getLeadsForSeller(Long sellerId);
    List<Enquiry> getInterestsForBuyer(Long buyerId);
    void updateStatus(Long enquiryId, String status);
    void deleteEnquiry(Long id);
}
