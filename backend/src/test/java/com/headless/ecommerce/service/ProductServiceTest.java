package com.headless.ecommerce.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category testCategory;
    private Product testProduct;
    private ProductResponse testProductResponse;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
            .id(1L)
            .name("Electronics")
            .sortOrder(0)
            .build();

        testProduct = Product.builder()
            .id(1L)
            .name("Test Product")
            .description("A test product")
            .price(new BigDecimal("99.99"))
            .stock(100)
            .imageUrl("/images/test.jpg")
            .status(ProductStatus.ON_SHELF)
            .salesCount(10)
            .category(testCategory)
            .build();

        testProductResponse = ProductResponse.builder()
            .id(1L)
            .name("Test Product")
            .description("A test product")
            .price(new BigDecimal("99.99"))
            .stock(100)
            .imageUrl("/images/test.jpg")
            .status("ON_SHELF")
            .salesCount(10)
            .categoryId(1L)
            .categoryName("Electronics")
            .build();
    }

    @Nested
    @DisplayName("Product CRUD Tests")
    class ProductCrudTests {

        @Test
        @DisplayName("Should create product successfully")
        void createProduct_success() {
            ProductCreateRequest request = ProductCreateRequest.builder()
                .name("New Product")
                .description("New product description")
                .price(new BigDecimal("49.99"))
                .stock(50)
                .imageUrl("/images/new.jpg")
                .categoryId(1L)
                .build();

            when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);
            when(productMapper.toResponse(any(Product.class))).thenReturn(testProductResponse);

            ProductResponse response = productService.createProduct(request);

            assertThat(response).isNotNull();
            assertThat(response.getName()).isEqualTo("Test Product");
            verify(productRepository).save(argThat(product ->
                product.getStatus() == ProductStatus.ON_SHELF &&
                product.getSalesCount() == 0));
        }

        @Test
        @DisplayName("Should throw exception when creating product with non-existent category")
        void createProduct_categoryNotFound_throwsException() {
            ProductCreateRequest request = ProductCreateRequest.builder()
                .name("New Product")
                .description("Description")
                .price(new BigDecimal("49.99"))
                .stock(50)
                .categoryId(999L)
                .build();

            when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Should update product fields successfully")
        void updateProduct_success() {
            ProductUpdateRequest request = ProductUpdateRequest.builder()
                .name("Updated Product")
                .price(new BigDecimal("79.99"))
                .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);
            when(productMapper.toResponse(any(Product.class))).thenReturn(testProductResponse);

            ProductResponse response = productService.updateProduct(1L, request);

            assertThat(response).isNotNull();
            verify(productRepository).save(argThat(product ->
                "Updated Product".equals(product.getName()) &&
                new BigDecimal("79.99").equals(product.getPrice())));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent product")
        void updateProduct_notFound_throwsException() {
            ProductUpdateRequest request = ProductUpdateRequest.builder()
                .name("Updated")
                .build();

            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(999L, request))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Should soft delete product by setting OFF_SHELF status")
        void deleteProduct_success() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);

            productService.deleteProduct(1L);

            verify(productRepository).save(argThat(product ->
                product.getStatus() == ProductStatus.OFF_SHELF));
        }
    }

    @Nested
    @DisplayName("Product Status Tests")
    class ProductStatusTests {

        @Test
        @DisplayName("Should update product status to ON_SHELF")
        void updateProductStatus_onShelf_success() {
            Product offShelfProduct = Product.builder()
                .id(1L)
                .name("Product")
                .status(ProductStatus.OFF_SHELF)
                .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(offShelfProduct));
            when(productRepository.save(any(Product.class))).thenReturn(offShelfProduct);
            when(productMapper.toResponse(any(Product.class))).thenReturn(testProductResponse);

            ProductResponse response = productService.updateProductStatus(1L, "ON_SHELF");

            assertThat(response).isNotNull();
            verify(productRepository).save(argThat(product ->
                product.getStatus() == ProductStatus.ON_SHELF));
        }

        @Test
        @DisplayName("Should throw exception for invalid product status")
        void updateProductStatus_invalidStatus_throwsException() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

            assertThatThrownBy(() -> productService.updateProductStatus(1L, "INVALID_STATUS"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid product status");
        }
    }

    @Nested
    @DisplayName("Product Query Tests")
    class ProductQueryTests {

        @Test
        @DisplayName("Should get product by ID")
        void getProductById_success() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

            ProductResponse response = productService.getProductById(1L);

            assertThat(response).isNotNull();
            assertThat(response.getName()).isEqualTo("Test Product");
        }

        @Test
        @DisplayName("Should throw exception when product not found by ID")
        void getProductById_notFound_throwsException() {
            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Should return paginated products with filters")
        void getProducts_withFilters_success() {
            Page<Product> productPage = new PageImpl<>(List.of(testProduct));
            when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(productPage);
            when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

            PageResponse<ProductResponse> response =
                productService.getProducts(0, 12, 1L, "test", "price,asc");

            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(1);
            assertThat(response.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return empty page when no products match")
        void getProducts_noResults_returnsEmptyPage() {
            Page<Product> emptyPage = new PageImpl<>(List.of());
            when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

            PageResponse<ProductResponse> response =
                productService.getProducts(0, 12, null, null, null);

            assertThat(response).isNotNull();
            assertThat(response.getContent()).isEmpty();
            assertThat(response.getTotalElements()).isEqualTo(0);
        }
    }
}
