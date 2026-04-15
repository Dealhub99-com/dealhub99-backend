package com.dealhub99.backend.repository;

import com.dealhub99.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Fetch only top-level categories (parent is null)
    List<Category> findByParentIsNullAndActiveTrue();
    
    // Fetch sub-categories for a parent category
    List<Category> findByParentIdAndActiveTrue(Long parentId);
    
    // Fetch all active categories
    List<Category> findByActiveTrue();
}
