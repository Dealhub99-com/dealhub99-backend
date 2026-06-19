package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.EnquiryResponseDTO;
import com.dealhub99.backend.dto.EnquiryCreateDTO;
import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.Enquiry;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.entity.Product;
import com.dealhub99.backend.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
@Transactional
public class EnquiryController {

    private final EnquiryService enquiryService;

    // POST /api/enquiries/{buyerId} - Buyer sends a new lead
    @PostMapping("/{buyerId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<EnquiryResponseDTO>> createEnquiry(
            @PathVariable Long buyerId,
            @RequestBody EnquiryCreateDTO createDTO) {
        
        Enquiry saved = enquiryService.createEnquiry(buyerId, createDTO.getProductId(), createDTO.getMessage());
        return ResponseEntity.ok(BaseResponse.success("Your enquiry has been sent to the seller!", mapToDTO(saved)));
    }

    // GET /api/enquiries/seller/{sellerId} - Seller views their leads
    @GetMapping("/seller/{sellerId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<EnquiryResponseDTO>>> getLeads(@PathVariable Long sellerId) {
        List<EnquiryResponseDTO> leads = enquiryService.getLeadsForSeller(sellerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Fetched all leads for your products", leads));
    }

    // GET /api/enquiries/buyer/{buyerId} - Buyer views their sent leads
    @GetMapping("/buyer/{buyerId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<EnquiryResponseDTO>>> getInterests(@PathVariable Long buyerId) {
        List<EnquiryResponseDTO> interests = enquiryService.getInterestsForBuyer(buyerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Fetched all your product enquiries", interests));
    }

    // PATCH /api/enquiries/{id}/status - Update status (e.g., Contacted, Closed)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        enquiryService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
    // DELETE /api/enquiries/{id} - Delete enquiry permanently
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEnquiry(@PathVariable Long id) {
        enquiryService.deleteEnquiry(id);
        return ResponseEntity.noContent().build();
    }

    // Mapper helper
    private EnquiryResponseDTO mapToDTO(Enquiry e) {
        User buyer = e.getBuyer();
        User seller = e.getSeller();
        Product p = e.getProduct();

        return EnquiryResponseDTO.builder()
                .id(e.getId())
                .productId(p.getId())
                .productName(p.getName())
                .productPrice(p.getPrice())
                .buyerId(buyer.getId())
                .buyerName(buyer.getFullName())
                .buyerEmail(buyer.getEmail())
                .buyerPhone(buyer.getMobileNumber())
                .sellerId(seller.getId())
                .sellerStoreName(seller.getSellerProfile() != null ? 
                        seller.getSellerProfile().getBusinessName() : seller.getFullName())
                .sellerPhone(seller.getMobileNumber())
                .message(e.getMessage())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
