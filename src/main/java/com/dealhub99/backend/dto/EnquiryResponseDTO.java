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
public class EnquiryResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Long buyerId;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private Long sellerId;
    private String sellerStoreName;
    private String sellerPhone;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
