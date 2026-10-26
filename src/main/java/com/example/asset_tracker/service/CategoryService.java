package com.example.asset_tracker.service;

import com.example.asset_tracker.dto.CategoryRequest;
import com.example.asset_tracker.dto.CategoryResponse;
import com.example.asset_tracker.entity.Category;
import com.example.asset_tracker.exception.ResourceNotFoundException;
import com.example.asset_tracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Category already exists: " + request.name());
        }
        Category category = new Category(request.name());
        category = categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName());
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
            .map(c -> new CategoryResponse(c.getId(), c.getName()))
            .toList();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }
}