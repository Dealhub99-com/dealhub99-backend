package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> getAllActiveBrands();
    Optional<Brand> getBrandById(Long id);
    Brand saveBrand(Brand brand);
    void deactivateBrand(Long id);
    void deleteBrand(Long id);
}
