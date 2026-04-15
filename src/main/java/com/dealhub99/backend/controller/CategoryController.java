package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.CategoryCreateDTO;
import com.dealhub99.backend.dto.CategoryResponseDTO;
import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.Category;
import com.dealhub99.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // GET /api/categories - All top-level categories
    @GetMapping
    public ResponseEntity<BaseResponse<List<CategoryResponseDTO>>> getTopLevelCategories() {
        List<CategoryResponseDTO> categories = categoryService.getTopLevelCategories().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Top level categories fetched successfully", categories));
    }

    // GET /api/categories/{id}/subcategories - Sub-categories for a parent
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<BaseResponse<List<CategoryResponseDTO>>> getSubCategories(@PathVariable Long id) {
        List<CategoryResponseDTO> subcategories = categoryService.getSubCategories(id).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("Sub categories fetched successfully", subcategories));
    }

    // GET /api/categories/all - All active categories
    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<CategoryResponseDTO>>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllActiveCategories().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success("All categories fetched successfully", categories));
    }

    // POST /api/categories - Create/Update Category (Simplified for now)
    @PostMapping
    public ResponseEntity<BaseResponse<CategoryResponseDTO>> createCategory(@RequestBody CategoryCreateDTO createDTO) {
        Category category = Category.builder()
                .name(createDTO.getName())
                .description(createDTO.getDescription())
                .iconUrl(createDTO.getIconUrl())
                .active(true)
                .build();

        if (createDTO.getParentId() != null) {
            categoryService.getCategoryById(createDTO.getParentId()).ifPresent(category::setParent);
        }

        Category saved = categoryService.saveCategory(category);
        return ResponseEntity.ok(BaseResponse.success("Category created successfully", mapToDTO(saved)));
    }

    // DELETE /api/categories/{id} - Deactivate Category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }

    // Mapping helper
    private CategoryResponseDTO mapToDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .subCategories(category.getSubCategories() != null ? 
                    category.getSubCategories().stream().map(this::mapToDTO).collect(Collectors.toList()) : null)
                .build();
    }
}
