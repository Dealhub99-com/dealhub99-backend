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
public class WishlistResponseDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImageUrl;
    private String categoryName;
    private LocalDateTime addedAt;
}
