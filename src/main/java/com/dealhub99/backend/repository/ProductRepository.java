package com.dealhub99.backend.repository;

import com.dealhub99.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Fetch active products for discovery
    List<Product> findByStatus(String status);
    
    // Filter by Category
    List<Product> findByCategoryIdAndStatus(Long categoryId, String status);
    
    // Filter by Brand
    List<Product> findByBrandIdAndStatus(Long brandId, String status);
    
    // Fetch products by Seller
    List<Product> findBySellerId(Long sellerId);
    
    // Global Search: Only show APPROVED active products
    @Query("SELECT p FROM Product p " +
           "JOIN p.seller s " +
           "LEFT JOIN s.sellerProfile sp " +
           "WHERE p.status = 'Active' AND p.approved = true " +
           "AND (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:location IS NULL OR LOWER(sp.city) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
           "AND (:productType IS NULL OR LOWER(p.productType) = LOWER(:productType))")
    List<Product> searchProducts(String query, String location, Long categoryId, Long brandId, String productType);

    // Fetch pending products for Admin approval
    List<Product> findByApprovedFalseOrderByUploadDateDesc();

    // Fetch popular products (ordered by sales or upload date)
    @Query("SELECT p FROM Product p WHERE p.status = 'Active' ORDER BY p.totalSales DESC")
    List<Product> findPopularProducts();
}
