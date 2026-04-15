package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getTopLevelCategories();
    List<Category> getSubCategories(Long parentId);
    List<Category> getAllActiveCategories();
    Optional<Category> getCategoryById(Long id);
    Category saveCategory(Category category);
    void deactivateCategory(Long id);
    void deleteCategory(Long id);
}
