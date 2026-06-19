package com.dealhub99.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private java.util.List<String> imageUrls;
    private String productType;
    private Long categoryId;
    private Long brandId;
    private Integer yearOfPurchase;
    private String usage;
    private String ownersCount;
    private String locationCity;
    private String locationState;
    private String status;
}
