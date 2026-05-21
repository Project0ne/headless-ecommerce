package com.headless.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.headless.ecommerce.dto.request.CartItemRequest;
import com.headless.ecommerce.dto.response.CartItemResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.mapper.CartMapper;
import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.enums.ProductStatus;
import com.headless.ecommerce.repository.ProductRepository;
import com.headless.ecommerce.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CartServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Tests")
class CartServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product testProduct;
    private CartItemResponse testCartItemResponse;
    private final Long USER_ID = 1L;
    private final String CART_KEY = "cart:1";

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
            .id(1L)
            .name("Test Product")
            .price(new BigDecimal("99.99"))
            .stock(100)
            .imageUrl("/images/test.jpg")
            .status(ProductStatus.ON_SHELF)
            .build();

        testCartItemResponse = CartItemResponse.builder()
            .productId(1L)
            .productName("Test Product")
            .productImage("/images/test.jpg")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(2)
            .available(true)
            .stock(100)
            .build();
    }

    @Nested
    @DisplayName("Get Cart Tests")
    class GetCartTests {

        @Test
        @DisplayName("Should return empty list when cart is empty")
        void getCart_emptyCart_returnsEmptyList() {
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.entries(CART_KEY)).thenReturn(new HashMap<>());

            List<CartItemResponse> result = cartService.getCart(USER_ID);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return cart items when cart has items")
        void getCart_withItems_returnsItems() {
            Map<Object, Object> entries = new HashMap<>();
            entries.put("1", "{\"productId\":1,\"quantity\":2}");

            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.entries(CART_KEY)).thenReturn(entries);
            when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(testProduct));
            when(cartMapper.toCartItemResponse(testProduct, 2)).thenReturn(testCartItemResponse);

            List<CartItemResponse> result = cartService.getCart(USER_ID);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getProductId()).isEqualTo(1L);
            assertThat(result.get(0).getQuantity()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Add to Cart Tests")
    class AddToCartTests {

        @Test
        @DisplayName("Should add new item to cart successfully")
        void addToCart_newItem_success() {
            CartItemRequest request = CartItemRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.get(CART_KEY, "1")).thenReturn(null);
            when(cartMapper.toCartItemResponse(testProduct, 2)).thenReturn(testCartItemResponse);

            CartItemResponse response = cartService.addToCart(USER_ID, request);

            assertThat(response).isNotNull();
            assertThat(response.getQuantity()).isEqualTo(2);
            verify(hashOperations).put(eq(CART_KEY), eq("1"), anyString());
        }

        @Test
        @DisplayName("Should accumulate quantity when adding existing item")
        void addToCart_existingItem_accumulatesQuantity() {
            CartItemRequest request = CartItemRequest.builder()
                .productId(1L)
                .quantity(3)
                .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.get(CART_KEY, "1")).thenReturn("{\"productId\":1,\"quantity\":2}");

            CartItemResponse updatedResponse = CartItemResponse.builder()
                .productId(1L).quantity(5).build();
            when(cartMapper.toCartItemResponse(testProduct, 5)).thenReturn(updatedResponse);

            CartItemResponse response = cartService.addToCart(USER_ID, request);

            assertThat(response).isNotNull();
            assertThat(response.getQuantity()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should throw exception when product not found")
        void addToCart_productNotFound_throwsException() {
            CartItemRequest request = CartItemRequest.builder()
                .productId(999L)
                .quantity(1)
                .build();

            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.addToCart(USER_ID, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Product not found");
        }

        @Test
        @DisplayName("Should throw exception when product is off shelf")
        void addToCart_offShelfProduct_throwsException() {
            Product offShelfProduct = Product.builder()
                .id(1L)
                .status(ProductStatus.OFF_SHELF)
                .build();

            CartItemRequest request = CartItemRequest.builder()
                .productId(1L)
                .quantity(1)
                .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(offShelfProduct));

            assertThatThrownBy(() -> cartService.addToCart(USER_ID, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("not available for purchase");
        }

        @Test
        @DisplayName("Should throw exception when exceeding stock")
        void addToCart_exceedsStock_throwsException() {
            CartItemRequest request = CartItemRequest.builder()
                .productId(1L)
                .quantity(200)
                .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.get(CART_KEY, "1")).thenReturn(null);

            assertThatThrownBy(() -> cartService.addToCart(USER_ID, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Insufficient stock");
        }
    }

    @Nested
    @DisplayName("Update Quantity Tests")
    class UpdateQuantityTests {

        @Test
        @DisplayName("Should update quantity successfully")
        void updateQuantity_success() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(cartMapper.toCartItemResponse(testProduct, 5)).thenReturn(
                CartItemResponse.builder().productId(1L).quantity(5).build());

            CartItemResponse response = cartService.updateQuantity(USER_ID, 1L, 5);

            assertThat(response).isNotNull();
            assertThat(response.getQuantity()).isEqualTo(5);
            verify(hashOperations).put(eq(CART_KEY), eq("1"), anyString());
        }

        @Test
        @DisplayName("Should remove item when quantity is 0 or negative")
        void updateQuantity_zeroOrNegative_removesItem() {
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);

            CartItemResponse response = cartService.updateQuantity(USER_ID, 1L, 0);

            assertThat(response).isNull();
            verify(hashOperations).delete(CART_KEY, "1");
        }

        @Test
        @DisplayName("Should throw exception when updating quantity exceeds stock")
        void updateQuantity_exceedsStock_throwsException() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

            assertThatThrownBy(() -> cartService.updateQuantity(USER_ID, 1L, 200))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Insufficient stock");
        }
    }

    @Nested
    @DisplayName("Remove Item Tests")
    class RemoveItemTests {

        @Test
        @DisplayName("Should remove item from cart successfully")
        void removeItem_success() {
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);

            cartService.removeItem(USER_ID, 1L);

            verify(hashOperations).delete(CART_KEY, "1");
        }
    }

    @Nested
    @DisplayName("Clear Cart Tests")
    class ClearCartTests {

        @Test
        @DisplayName("Should clear cart successfully")
        void clearCart_success() {
            cartService.clearCart(USER_ID);

            verify(redisTemplate).delete(CART_KEY);
        }
    }

    @Nested
    @DisplayName("Merge Cart Tests")
    class MergeCartTests {

        @Test
        @DisplayName("Should merge local cart items with server cart")
        void mergeCart_success() {
            CartItemRequest localItem = CartItemRequest.builder()
                .productId(1L)
                .quantity(3)
                .build();

            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.get(CART_KEY, "1")).thenReturn("{\"productId\":1,\"quantity\":2}");

            // After merge, getCart is called
            Map<Object, Object> mergedEntries = new HashMap<>();
            mergedEntries.put("1", "{\"productId\":1,\"quantity\":3}");
            when(hashOperations.entries(CART_KEY)).thenReturn(mergedEntries);
            when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(testProduct));
            when(cartMapper.toCartItemResponse(eq(testProduct), anyInt())).thenReturn(testCartItemResponse);

            List<CartItemResponse> result = cartService.mergeCart(USER_ID, List.of(localItem));

            assertThat(result).isNotNull();
            // Merge uses max quantity: max(3, 2) = 3
            verify(hashOperations).put(eq(CART_KEY), eq("1"), anyString());
        }

        @Test
        @DisplayName("Should add new local items not in server cart")
        void mergeCart_newItems_added() {
            CartItemRequest localItem = CartItemRequest.builder()
                .productId(2L)
                .quantity(1)
                .build();

            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            when(hashOperations.get(CART_KEY, "2")).thenReturn(null);

            // After merge, getCart
            Map<Object, Object> entries = new HashMap<>();
            when(hashOperations.entries(CART_KEY)).thenReturn(entries);
            when(productRepository.findAllById(anyList())).thenReturn(List.of());
            when(hashOperations.entries(CART_KEY)).thenReturn(entries);

            List<CartItemResponse> result = cartService.mergeCart(USER_ID, List.of(localItem));

            assertThat(result).isNotNull();
            verify(hashOperations).put(eq(CART_KEY), eq("2"), anyString());
        }
    }
}
