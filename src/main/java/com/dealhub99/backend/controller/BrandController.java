package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.BrandResponseDTO;
import com.dealhub99.backend.dto.BrandCreateDTO;
import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.Brand;
import com.dealhub99.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // GET /api/brands - Fetch all active brands for the UI
    @GetMapping
    public ResponseEntity<BaseResponse<List<BrandResponseDTO>>> getBrands() {
        List<BrandResponseDTO> brands = brandService.getAllActiveBrands().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Brands fetched successfully", brands));
    }

    // POST /api/brands - Admin: Add new brand
    @PostMapping
    public ResponseEntity<BaseResponse<BrandResponseDTO>> createBrand(@RequestBody BrandCreateDTO brandDTO) {
        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .description(brandDTO.getDescription())
                .logoUrl(brandDTO.getLogoUrl())
                .active(true)
                .build();
        
        Brand saved = brandService.saveBrand(brand);
        return ResponseEntity.ok(BaseResponse.success("Brand created successfully", mapToDTO(saved)));
    }

    // DELETE /api/brands/{id} - Admin: Deactivate brand
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateBrand(@PathVariable Long id) {
        brandService.deactivateBrand(id);
        return ResponseEntity.noContent().build();
    }

    // Mapper helper
    private BrandResponseDTO mapToDTO(Brand brand) {
        return BrandResponseDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .build();
    }
}
