package com.dealhub99.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfileUpdateDTO {
    private String businessName;
    private String businessAddress;
    private String city;
    private Integer numberOfLocations;
    private String gstNumber;
    private String aadhaarNumber;
}
