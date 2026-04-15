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
    private String productType; // New / Used
    private String status; // Active / Draft / Archived
    private boolean approved;
    private long viewCount;
    private Integer totalSales;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Long sellerId;
    private String sellerStoreName;
    private String sellerStoreCity; // For location-based filtering
    private LocalDateTime uploadDate;
}
