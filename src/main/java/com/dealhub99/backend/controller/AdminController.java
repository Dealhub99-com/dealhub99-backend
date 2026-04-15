package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.*;
import com.dealhub99.backend.entity.*;
import com.dealhub99.backend.repository.*;
import com.dealhub99.backend.service.ProductService;
import com.dealhub99.backend.service.SellerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final EnquiryRepository enquiryRepository;
    private final ProductService productService;
    private final SellerProfileService sellerProfileService;

    // GET /api/admin/dashboard - High level stats
    @GetMapping("/dashboard")
    public ResponseEntity<BaseResponse<PlatformStatsDTO>> getAdminDashboard() {
        PlatformStatsDTO stats = PlatformStatsDTO.builder()
                .totalUsers(userRepository.count())
                .totalSellers(userRepository.findByRole(UserRole.SELLER).size())
                .totalProducts(productRepository.count())
                .totalBrands(brandRepository.count())
                .totalCategories(categoryRepository.count())
                .totalLeads(enquiryRepository.count())
                .build();
        return ResponseEntity.ok(BaseResponse.success("Admin dashboard stats loaded", stats));
    }

    // GET /api/admin/sellers - List all sellers with profile info
    @GetMapping("/sellers")
    public ResponseEntity<BaseResponse<List<SellerProfileResponseDTO>>> getAllSellers() {
        List<SellerProfileResponseDTO> sellers = userRepository.findByRole(UserRole.SELLER).stream()
                .map(user -> mapToSellerDTO(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("All registered sellers fetched successfully", sellers));
    }

    // GET /api/admin/products - List all products across platform
    @GetMapping("/products")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getAllProducts() {
        List<ProductResponseDTO> products = productRepository.findAll().stream()
                .map(p -> mapToProductDTO(p))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("All platform products fetched successfully", products));
    }

    // GET /api/admin/enquiries - List all platform enquiries (leads)
    @GetMapping("/enquiries")
    public ResponseEntity<BaseResponse<List<EnquiryResponseDTO>>> getAllEnquiries() {
        List<EnquiryResponseDTO> enquiries = enquiryRepository.findAll().stream()
                .map(e -> mapToEnquiryDTO(e))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("All platform leads fetched successfully", enquiries));
    }

    // GET /api/admin/pending-products - List products waiting for approval
    @GetMapping("/pending-products")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getPendingProducts() {
        List<ProductResponseDTO> pending = productService.getPendingProducts().stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Products pending approval fetched successfully", pending));
    }

    // PUT /api/admin/product/{id}/approve - Admin approve product
    @PutMapping("/product/{id}/approve")
    public ResponseEntity<Void> approveProduct(@PathVariable Long id) {
        productService.approveProduct(id);
        return ResponseEntity.ok().build();
    }

    // DELETE /api/admin/product/{id} - Admin remove product
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/admin/seller/{id}/promote - Toggle shop promotion
    @PatchMapping("/seller/{id}/promote")
    public ResponseEntity<Void> toggleSellerPromotion(@PathVariable Long id, @RequestParam boolean status) {
        userRepository.findById(id).ifPresent(user -> {
            sellerProfileService.togglePromotion(id, status);
        });
        return ResponseEntity.noContent().build();
    }

    // Mapping helpers (Simplified versions of those in other controllers)
    private SellerProfileResponseDTO mapToSellerDTO(User user) {
        SellerProfile profile = user.getSellerProfile();
        return SellerProfileResponseDTO.builder()
                .userId(user.getId())
                .sellerName(user.getFullName())
                .sellerEmail(user.getEmail())
                .sellerPhone(user.getMobileNumber())
                .businessName(profile != null ? profile.getBusinessName() : "N/A")
                .businessAddress(profile != null ? profile.getBusinessAddress() : "N/A")
                .city(profile != null ? profile.getCity() : "N/A")
                .promoted(profile != null && profile.isPromoted())
                .promotionRequested(profile != null && profile.isPromotionRequested())
                .supportRequestMessage(profile != null ? profile.getSupportRequestMessage() : null)
                .build();
    }

    private ProductResponseDTO mapToProductDTO(Product p) {
        return ProductResponseDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .status(p.getStatus())
                .categoryName(p.getCategory().getName())
                .sellerStoreName(p.getSeller().getSellerProfile() != null ? 
                    p.getSeller().getSellerProfile().getBusinessName() : p.getSeller().getFullName())
                .build();
    }

    private EnquiryResponseDTO mapToEnquiryDTO(Enquiry e) {
        return EnquiryResponseDTO.builder()
                .id(e.getId())
                .productName(e.getProduct().getName())
                .buyerName(e.getBuyer().getFullName())
                .sellerStoreName(e.getSeller().getSellerProfile() != null ? 
                    e.getSeller().getSellerProfile().getBusinessName() : e.getSeller().getFullName())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
