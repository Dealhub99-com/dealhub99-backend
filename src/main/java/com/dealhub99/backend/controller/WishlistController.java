package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.WishlistResponseDTO;
import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.Wishlist;
import com.dealhub99.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // POST /api/wishlist/{userId}/add/{productId} - Add product to wishlist
    @PostMapping("/{userId}/add/{productId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<WishlistResponseDTO>> addToWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        
        Wishlist saved = wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.ok(BaseResponse.success("Product added to your wishlist!", mapToDTO(saved)));
    }

    // DELETE /api/wishlist/{userId}/remove/{productId} - Remove product from wishlist
    @DeleteMapping("/{userId}/remove/{productId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/wishlist/{userId} - Fetch all items for the logged-in buyer
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<WishlistResponseDTO>>> getWishlist(@PathVariable Long userId) {
        List<WishlistResponseDTO> wishlist = wishlistService.getWishlistByUser(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Wishlist fetched successfully", wishlist));
    }

    // GET /api/wishlist/check/{userId}?productId={productId} - Check if product is in wishlist
    @GetMapping("/check/{userId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Boolean>> checkWishlist(
            @PathVariable Long userId,
            @RequestParam Long productId) {
        boolean exists = wishlistService.isInWishlist(userId, productId);
        return ResponseEntity.ok(BaseResponse.success("Check successful", exists));
    }

    // Mapper helper
    private WishlistResponseDTO mapToDTO(Wishlist w) {
        return WishlistResponseDTO.builder()
                .id(w.getId())
                .userId(w.getUser().getId())
                .productId(w.getProduct().getId())
                .productName(w.getProduct().getName())
                .productPrice(w.getProduct().getPrice())
                .productImageUrl(w.getProduct().getImageUrl())
                .categoryName(w.getProduct().getCategory().getName())
                .addedAt(w.getAddedAt())
                .build();
    }
}
