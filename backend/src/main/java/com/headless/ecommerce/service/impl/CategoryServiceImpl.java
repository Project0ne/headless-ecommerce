package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.request.CategoryCreateRequest;
import com.headless.ecommerce.dto.response.CategoryResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.mapper.CategoryMapper;
import com.headless.ecommerce.model.Category;
import com.headless.ecommerce.repository.CategoryRepository;
import com.headless.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of CategoryService for category management.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                                CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNullOrderBySortOrderAsc();
        return rootCategories.stream()
            .map(this::toCategoryResponseWithChildren)
            .toList();
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = Category.builder()
            .name(request.getName())
            .icon(request.getIcon())
            .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
            .build();

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getParentId()));
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getParentId()));
            if (isDescendant(parent, id)) {
                throw new BusinessException("Cannot set a descendant as parent category");
            }
            category.setParent(parent);
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getChildren().isEmpty()) {
            throw new BusinessException("Cannot delete category with child categories");
        }

        categoryRepository.delete(category);
    }

    /**
     * Converts a Category entity to a CategoryResponse with children populated.
     *
     * @param category the Category entity
     * @return the CategoryResponse with nested children
     */
    private CategoryResponse toCategoryResponseWithChildren(Category category) {
        CategoryResponse response = categoryMapper.toResponse(category);
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryResponse> children = category.getChildren().stream()
                .map(this::toCategoryResponseWithChildren)
                .toList();
            response.setChildren(children);
        }
        return response;
    }

    /**
     * Checks if the target category is a descendant of the given parent.
     *
     * @param parent the parent category to check
     * @param targetId the ID of the target category
     * @return true if the target is a descendant
     */
    private boolean isDescendant(Category parent, Long targetId) {
        Category current = parent;
        while (current != null) {
            if (current.getId().equals(targetId)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
