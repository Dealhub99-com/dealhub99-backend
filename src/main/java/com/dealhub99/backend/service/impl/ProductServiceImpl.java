package com.dealhub99.backend.service.impl;

import com.dealhub99.backend.entity.Product;
import com.dealhub99.backend.repository.ProductRepository;
import com.dealhub99.backend.service.ProductService;
import com.dealhub99.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getActiveProducts() {
        return productRepository.findByStatus("Active").stream()
                .filter(Product::isApproved)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getPopularProducts() {
        return productRepository.findPopularProducts().stream()
                .filter(Product::isApproved)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndStatus(categoryId, "Active").stream()
                .filter(Product::isApproved)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByBrand(Long brandId) {
        return productRepository.findByBrandIdAndStatus(brandId, "Active").stream()
                .filter(Product::isApproved)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchProducts(String query, String location, Long categoryId, Long brandId, String productType) {
        return productRepository.searchProducts(query, location, categoryId, brandId, productType);
    }

    @Override
    public List<Product> getPendingProducts() {
        return productRepository.findByApprovedFalseOrderByUploadDateDesc();
    }

    @Override
    public void approveProduct(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        p.setApproved(true);
        productRepository.save(p);
    }

    @Override
    public Optional<Product> getProductWithViewIncrement(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        p.setViewCount(p.getViewCount() + 1);
        return Optional.of(productRepository.save(p));
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void changeProductStatus(Long id, String status) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        p.setStatus(status);
        productRepository.save(p);
    }

    @Override
    public void incrementSales(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        p.setTotalSales(p.getTotalSales() + 1);
        productRepository.save(p);
    }
}
