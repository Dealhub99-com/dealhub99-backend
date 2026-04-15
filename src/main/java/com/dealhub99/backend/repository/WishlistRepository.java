package com.dealhub99.backend.repository;

import com.dealhub99.backend.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    // Fetch user's wishlist
    List<Wishlist> findByUserIdOrderByAddedAtDesc(Long userId);
    
    // Check if product is already in user's wishlist
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    
    // Find specific record for removal
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
}
