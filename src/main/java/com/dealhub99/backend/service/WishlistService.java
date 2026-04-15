package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.Wishlist;
import java.util.List;

public interface WishlistService {
    Wishlist addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    List<Wishlist> getWishlistByUser(Long userId);
    boolean isInWishlist(Long userId, Long productId);
}
