package com.dealhub99.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private java.util.List<String> imageUrls;
    private String productType; // New / Used
    private String status; // Active / Draft / Archived
    private boolean approved;
    private Integer yearOfPurchase;
    private String usage;
    private String ownersCount;
    private String locationCity;
    private String locationState;
    private long viewCount;
    private Integer totalSales;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Long sellerId;
    private String sellerStoreName;
    private String sellerStoreCity; // For location-based filtering
    private boolean sellerPromoted;
    private LocalDateTime uploadDate;
    private String sellerPhone;
    private String sellerEmail;
    private String sellerAddress;
}
