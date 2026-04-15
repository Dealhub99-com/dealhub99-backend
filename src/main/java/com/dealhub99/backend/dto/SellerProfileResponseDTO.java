package com.dealhub99.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfileResponseDTO {
    private Long id;
    private Long userId;
    private String businessName;
    private String businessAddress;
    private String city;
    private Integer numberOfLocations;
    private String gstNumber;
    private String aadhaarNumber;
    private String sellerName; // From User entity
    private String sellerEmail; // From User entity
    private String sellerPhone; // From User entity
    private boolean promoted;
    private boolean promotionRequested;
    private String supportRequestMessage;
}
