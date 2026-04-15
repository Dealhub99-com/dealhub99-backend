package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.ProductResponseDTO;
import com.dealhub99.backend.dto.ProductCreateDTO;
import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.Product;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.entity.Category;
import com.dealhub99.backend.entity.Brand;
import com.dealhub99.backend.service.ProductService;
import com.dealhub99.backend.service.CategoryService;
import com.dealhub99.backend.service.BrandService;
import com.dealhub99.backend.repository.UserRepository;
import com.dealhub99.backend.exception.ResourceNotFoundException;
import com.dealhub99.backend.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final UserRepository userRepository;

    // GET /api/products - Discovery (Active products)
    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getProducts() {
        List<ProductResponseDTO> products = productService.getActiveProducts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Products fetched successfully", products));
    }

    // GET /api/products/search - Multi-parameter search (Query, Location, Category, Brand, Type)
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String type) {
        
        List<ProductResponseDTO> products = productService.searchProducts(query, location, categoryId, brandId, type).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Found " + products.size() + " products matching your search", products));
    }

    // GET /api/products/popular - Popular products (Ordered by sales)
    @GetMapping("/popular")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getPopularProducts() {
        List<ProductResponseDTO> products = productService.getPopularProducts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Popular products fetched successfully", products));
    }

    // GET /api/products/{id} - Detailed view (Increments view count)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponseDTO>> getProductDetails(@PathVariable Long id) {
        return productService.getProductWithViewIncrement(id)
                .map(this::mapToDTO)
                .map(dto -> ResponseEntity.ok(BaseResponse.success("Product details fetched successfully", dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/products/category/{id} - Discovery (By category)
    @GetMapping("/category/{id}")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getByCategory(@PathVariable Long id) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(id).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Products for category fetched", products));
    }

    // GET /api/products/brand/{id} - Discovery (By brand)
    @GetMapping("/brand/{id}")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getByBrand(@PathVariable Long id) {
        List<ProductResponseDTO> products = productService.getProductsByBrand(id).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Products for brand fetched", products));
    }

    // GET /api/products/seller/{userId} - Discovery (By specific seller)
    @GetMapping("/seller/{userId}")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getBySeller(@PathVariable Long userId) {
        List<ProductResponseDTO> products = productService.getProductsBySeller(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Products for seller fetched", products));
    }

    // POST /api/products/{userId}/list - Seller lists a product
    @PostMapping("/{userId}/list")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<ProductResponseDTO>> createProduct(
            @PathVariable Long userId,
            @RequestBody ProductCreateDTO createDTO) {
        
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Seller user not found"));
                
        Category category = categoryService.getCategoryById(createDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
                
        Brand brand = createDTO.getBrandId() != null ? 
                brandService.getBrandById(createDTO.getBrandId()).orElse(null) : null;

        Product product = Product.builder()
                .name(createDTO.getName())
                .description(createDTO.getDescription())
                .price(createDTO.getPrice())
                .imageUrl(createDTO.getImageUrl())
                .productType(createDTO.getProductType())
                .status(createDTO.getStatus() != null ? createDTO.getStatus() : "Active")
                .approved(seller.getRole() == com.dealhub99.backend.entity.UserRole.ADMIN) // Auto-approve if listing user is ADMIN
                .category(category)
                .brand(brand)
                .seller(seller)
                .totalSales(0)
                .viewCount(0)
                .build();
        
        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(BaseResponse.success("Product listed successfully and is now active!", mapToDTO(saved)));
    }

    // PUT /api/products/{id} - Seller updates a product
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductCreateDTO updateDTO) {
        
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
                
        if (updateDTO.getCategoryId() == null) {
            throw new BadRequestException("Category ID is required");
        }

        Category category = categoryService.getCategoryById(updateDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + updateDTO.getCategoryId()));
                
        if (updateDTO.getBrandId() != null) {
            product.setBrand(brandService.getBrandById(updateDTO.getBrandId()).orElse(null));
        }

        product.setName(updateDTO.getName());
        product.setDescription(updateDTO.getDescription());
        product.setPrice(updateDTO.getPrice());
        product.setImageUrl(updateDTO.getImageUrl());
        product.setProductType(updateDTO.getProductType());
        product.setCategory(category);
        
        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(BaseResponse.success("Product updated successfully", mapToDTO(saved)));
    }

    // PUT /api/products/{id}/approve - Admin approves a product
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> approveProduct(@PathVariable Long id) {
        productService.approveProduct(id);
        return ResponseEntity.ok(BaseResponse.success("Product approved successfully", null));
    }

    // GET /api/products/pending - Admin gets pending products
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getPendingProducts() {
        List<ProductResponseDTO> products = productService.getPendingProducts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Pending products fetched", products));
    }

    // DELETE /api/products/{id} - Seller archives or deletes a product
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Void> archiveProduct(@PathVariable Long id) {
        productService.changeProductStatus(id, "Archived");
        return ResponseEntity.noContent().build();
    }

    // Mapper helper
    private ProductResponseDTO mapToDTO(Product p) {
        return ProductResponseDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .imageUrl(p.getImageUrl())
                .productType(p.getProductType())
                .status(p.getStatus())
                .approved(p.isApproved())
                .viewCount(p.getViewCount())
                .totalSales(p.getTotalSales())
                .categoryId(p.getCategory().getId())
                .categoryName(p.getCategory().getName())
                .brandId(p.getBrand() != null ? p.getBrand().getId() : null)
                .brandName(p.getBrand() != null ? p.getBrand().getName() : null)
                .sellerId(p.getSeller().getId())
                .sellerStoreName(p.getSeller().getSellerProfile() != null ? 
                        p.getSeller().getSellerProfile().getBusinessName() : p.getSeller().getFullName())
                .sellerStoreCity(p.getSeller().getSellerProfile() != null ? 
                        p.getSeller().getSellerProfile().getCity() : "Other")
                .uploadDate(p.getUploadDate())
                .build();
    }
}
