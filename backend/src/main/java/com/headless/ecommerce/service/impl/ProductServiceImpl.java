package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.request.ProductCreateRequest;
import com.headless.ecommerce.dto.request.ProductUpdateRequest;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.mapper.ProductMapper;
import com.headless.ecommerce.model.Category;
import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.enums.ProductStatus;
import com.headless.ecommerce.repository.CategoryRepository;
import com.headless.ecommerce.repository.ProductRepository;
import com.headless.ecommerce.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ProductService for product management.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                               CategoryRepository categoryRepository,
                               ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public PageResponse<ProductResponse> getProducts(int page, int size,
                                                      Long categoryId, String keyword, String sort) {
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Only show on-shelf products for public queries
            predicates.add(cb.equal(root.get("status"), ProductStatus.ON_SHELF));
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> content = productPage.getContent().stream()
            .map(productMapper::toResponse)
            .toList();

        return PageResponse.of(content, productPage.getTotalElements(),
            productPage.getTotalPages(), productPage.getNumber(), productPage.getSize());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = findById(id);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Product product = Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .stock(request.getStock())
            .imageUrl(request.getImageUrl())
            .status(ProductStatus.ON_SHELF)
            .salesCount(0)
            .category(category)
            .build();

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = findById(id);

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findById(id);
        product.setStatus(ProductStatus.OFF_SHELF);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProductStatus(Long id, String status) {
        Product product = findById(id);
        try {
            product.setStatus(ProductStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid product status: " + status);
        }
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    /**
     * Parses a sort string like "price,asc" into a Sort object.
     *
     * @param sort the sort string
     * @return the Sort object
     */
    private Sort parseSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = parts.length > 1
            ? Sort.Direction.fromString(parts[1].trim())
            : Sort.Direction.ASC;
        return Sort.by(direction, field);
    }
}
