package com.headless.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.security.JwtAuthenticationFilter;
import com.headless.ecommerce.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * WebMvcTest for ProductController.
 */
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ProductResponse createTestProductResponse() {
        return ProductResponse.builder()
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
    @DisplayName("GET /api/v1/products")
    class GetProductsTests {

        @Test
        @DisplayName("Should return paginated product list")
        void getProducts_success() throws Exception {
            ProductResponse product = createTestProductResponse();
            PageResponse<ProductResponse> pageResponse = PageResponse.of(
                List.of(product), 1, 1, 0, 12);

            when(productService.getProducts(0, 12, null, null, null))
                .thenReturn(pageResponse);

            mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("Test Product"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
        }

        @Test
        @DisplayName("Should filter products by category and keyword")
        void getProducts_withFilters() throws Exception {
            PageResponse<ProductResponse> emptyPage = PageResponse.of(
                List.of(), 0, 0, 0, 12);

            when(productService.getProducts(0, 12, 1L, "test", "price,asc"))
                .thenReturn(emptyPage);

            mockMvc.perform(get("/api/v1/products")
                    .param("categoryId", "1")
                    .param("keyword", "test")
                    .param("sort", "price,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content").isEmpty());
        }

        @Test
        @DisplayName("Should use custom pagination parameters")
        void getProducts_customPagination() throws Exception {
            PageResponse<ProductResponse> pageResponse = PageResponse.of(
                List.of(), 0, 0, 2, 20);

            when(productService.getProducts(2, 20, null, null, null))
                .thenReturn(pageResponse);

            mockMvc.perform(get("/api/v1/products")
                    .param("page", "2")
                    .param("size", "20"))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products/{id}")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return product detail by ID")
        void getProductById_success() throws Exception {
            ProductResponse product = createTestProductResponse();
            when(productService.getProductById(1L)).thenReturn(product);

            mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(99.99));
        }

        @Test
        @DisplayName("Should return 404 for non-existent product")
        void getProductById_notFound() throws Exception {
            when(productService.getProductById(999L))
                .thenThrow(new ResourceNotFoundException("Product", "id", 999L));

            mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products/search")
    class SearchProductsTests {

        @Test
        @DisplayName("Should search products by keyword")
        void searchProducts_success() throws Exception {
            ProductResponse product = createTestProductResponse();
            PageResponse<ProductResponse> pageResponse = PageResponse.of(
                List.of(product), 1, 1, 0, 12);

            when(productService.getProducts(0, 12, null, "Test", null))
                .thenReturn(pageResponse);

            mockMvc.perform(get("/api/v1/products/search")
                    .param("keyword", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Test Product"));
        }
    }
}
