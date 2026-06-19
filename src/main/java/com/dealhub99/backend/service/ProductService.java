package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getActiveProducts();
    List<Product> getPopularProducts();
    List<Product> getProductsByCategory(Long categoryId);
    List<Product> getProductsByBrand(Long brandId);
    List<Product> searchProducts(String query, String location, Long categoryId, Long brandId, String productType);
    List<Product> getPendingProducts();
    void approveProduct(Long id);
    void rejectProduct(Long id);
    Optional<Product> getProductWithViewIncrement(Long id);
    Optional<Product> getProductById(Long id);
    List<Product> getProductsBySeller(Long sellerId);
    Product saveProduct(Product product);
    void changeProductStatus(Long id, String status);
    void deleteProduct(Long id);
    void incrementSales(Long id);
}
