package com.dealhub99.backend.service.impl;

import com.dealhub99.backend.entity.Wishlist;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.entity.Product;
import com.dealhub99.backend.repository.WishlistRepository;
import com.dealhub99.backend.repository.UserRepository;
import com.dealhub99.backend.repository.ProductRepository;
import com.dealhub99.backend.service.WishlistService;
import com.dealhub99.backend.exception.ResourceNotFoundException;
import com.dealhub99.backend.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Wishlist addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new BadRequestException("Product already in wishlist");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();
        
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        Wishlist w = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in your wishlist."));
        wishlistRepository.delete(w);
    }

    @Override
    public List<Wishlist> getWishlistByUser(Long userId) {
        return wishlistRepository.findByUserIdOrderByAddedAtDesc(userId);
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }
}
