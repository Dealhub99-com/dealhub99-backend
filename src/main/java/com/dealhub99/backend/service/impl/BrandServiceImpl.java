package com.dealhub99.backend.service.impl;

import com.dealhub99.backend.entity.Brand;
import com.dealhub99.backend.repository.BrandRepository;
import com.dealhub99.backend.service.BrandService;
import com.dealhub99.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public List<Brand> getAllActiveBrands() {
        return brandRepository.findByActiveTrue();
    }

    @Override
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public void deactivateBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
        brand.setActive(false);
        brandRepository.save(brand);
    }

    @Override
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found with ID: " + id);
        }
        brandRepository.deleteById(id);
    }
}
