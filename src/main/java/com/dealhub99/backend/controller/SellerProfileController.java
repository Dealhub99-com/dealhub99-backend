package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.SellerProfileResponseDTO;
import com.dealhub99.backend.dto.SellerProfileUpdateDTO;
import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.SellerProfile;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.service.SellerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerProfileController {

    private final SellerProfileService sellerProfileService;

    // GET /api/sellers - List all active shops/stores
    @GetMapping
    public ResponseEntity<BaseResponse<List<SellerProfileResponseDTO>>> getAllSellers() {
        List<SellerProfileResponseDTO> sellers = sellerProfileService.getAllActiveSellers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Active sellers fetched successfully", sellers));
    }

    // GET /api/sellers/search - Search shops by business name and city
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<SellerProfileResponseDTO>>> searchSellers(
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String city) {
        
        List<SellerProfileResponseDTO> sellers = sellerProfileService.searchSellers(storeName, city).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Found " + sellers.size() + " sellers matching your search", sellers));
    }

    // GET /api/sellers/{userId}/profile - Fetch a seller's business details
    @GetMapping("/{userId}/profile")
    public ResponseEntity<BaseResponse<SellerProfileResponseDTO>> getSellerProfile(@PathVariable Long userId) {
        return sellerProfileService.getProfileByUserId(userId)
                .map(this::mapToDTO)
                .map(dto -> ResponseEntity.ok(BaseResponse.success("Seller profile fetched successfully", dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/sellers/{userId}/profile - Update or initialize business profile
    // In a real app, userId should come from the authentication context.
    @PostMapping("/{userId}/profile")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<SellerProfileResponseDTO>> updateProfile(
            @PathVariable Long userId,
            @RequestBody SellerProfileUpdateDTO updateDTO) {
        
        SellerProfile profile = SellerProfile.builder()
                .businessName(updateDTO.getBusinessName())
                .businessAddress(updateDTO.getBusinessAddress())
                .city(updateDTO.getCity())
                .numberOfLocations(updateDTO.getNumberOfLocations())
                .gstNumber(updateDTO.getGstNumber())
                .aadhaarNumber(updateDTO.getAadhaarNumber())
                .businessCategory(updateDTO.getBusinessCategory())
                .productTypeFocus(updateDTO.getProductTypeFocus())
                .active(true)
                .build();
        
        SellerProfile saved = sellerProfileService.saveOrUpdateProfile(userId, profile);
        return ResponseEntity.ok(BaseResponse.success("Seller profile updated successfully!", mapToDTO(saved)));
    }

    // POST /api/sellers/{userId}/request-promotion
    @PostMapping("/{userId}/request-promotion")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<BaseResponse<String>> requestPromotion(
            @PathVariable Long userId,
            @RequestBody String message) {
        sellerProfileService.requestPromotion(userId, message);
        return ResponseEntity.ok(BaseResponse.success("Promotion request sent successfully!", null));
    }

    // Mapper helper
    private SellerProfileResponseDTO mapToDTO(SellerProfile profile) {
        User user = profile.getUser();
        return SellerProfileResponseDTO.builder()
                .id(profile.getId())
                .userId(user.getId())
                .businessName(profile.getBusinessName())
                .businessAddress(profile.getBusinessAddress())
                .city(profile.getCity())
                .numberOfLocations(profile.getNumberOfLocations())
                .gstNumber(profile.getGstNumber())
                .aadhaarNumber(profile.getAadhaarNumber())
                .businessCategory(profile.getBusinessCategory())
                .productTypeFocus(profile.getProductTypeFocus())
                .sellerName(user.getFullName())
                .sellerEmail(user.getEmail())
                .sellerPhone(user.getMobileNumber())
                .promoted(profile.isPromoted())
                .promotionRequested(profile.isPromotionRequested())
                .status(profile.isPromoted() ? "VERIFIED" : "PENDING")
                .supportRequestMessage(profile.getSupportRequestMessage())
                .build();
    }
}
