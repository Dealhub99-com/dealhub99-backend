package com.dealhub99.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformStatsDTO {
    private long totalUsers;
    private long totalSellers;
    private long totalProducts;
    private long totalBrands;
    private long totalCategories;
    private long totalLeads;
}
