package com.headless.ecommerce.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.headless.ecommerce.dto.request.CartItemRequest;
import com.headless.ecommerce.dto.response.CartItemResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.mapper.CartMapper;
import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.enums.ProductStatus;
import com.headless.ecommerce.repository.ProductRepository;
import com.headless.ecommerce.service.CartService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of CartService using Redis Hash for storage.
 */
@Service
public class CartServiceImpl implements CartService {

    private static final String CART_KEY_PREFIX = "cart:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final ObjectMapper objectMapper;

    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate,
                            ProductRepository productRepository,
                            CartMapper cartMapper,
                            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CartItemResponse> getCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        if (entries.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> productIds = entries.keySet().stream()
            .map(k -> Long.parseLong(k.toString()))
            .toList();

        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
            .map(product -> {
                String quantityStr = entries.get(product.getId().toString()).toString();
                int quantity = parseQuantity(quantityStr);
                return cartMapper.toCartItemResponse(product, quantity);
            })
            .toList();
    }

    @Override
    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new BusinessException("Product not found with id: " + request.getProductId()));

        if (product.getStatus() != ProductStatus.ON_SHELF) {
            throw new BusinessException("Product is not available for purchase");
        }

        String key = CART_KEY_PREFIX + userId;
        String field = String.valueOf(request.getProductId());

        // Check if item already exists in cart
        Object existing = redisTemplate.opsForHash().get(key, field);
        int newQuantity = request.getQuantity();
        if (existing != null) {
            int existingQty = parseQuantity(existing.toString());
            newQuantity += existingQty;
        }

        // Validate stock
        if (newQuantity > product.getStock()) {
            throw new BusinessException("Insufficient stock. Available: " + product.getStock());
        }

        // Store as JSON with productId and quantity
        String value = toJson(new CartItem(request.getProductId(), newQuantity));
        redisTemplate.opsForHash().put(key, field, value);

        return cartMapper.toCartItemResponse(product, newQuantity);
    }

    @Override
    public CartItemResponse updateQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            removeItem(userId, productId);
            return null;
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException("Product not found with id: " + productId));

        if (quantity > product.getStock()) {
            throw new BusinessException("Insufficient stock. Available: " + product.getStock());
        }

        String key = CART_KEY_PREFIX + userId;
        String field = String.valueOf(productId);
        String value = toJson(new CartItem(productId, quantity));
        redisTemplate.opsForHash().put(key, field, value);

        return cartMapper.toCartItemResponse(product, quantity);
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        String key = CART_KEY_PREFIX + userId;
        String field = String.valueOf(productId);
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public List<CartItemResponse> mergeCart(Long userId, List<CartItemRequest> localItems) {
        String key = CART_KEY_PREFIX + userId;

        for (CartItemRequest localItem : localItems) {
            String field = String.valueOf(localItem.getProductId());
            Object existing = redisTemplate.opsForHash().get(key, field);

            if (existing != null) {
                // Take max of local and Redis quantity
                int redisQty = parseQuantity(existing.toString());
                int mergedQty = Math.max(localItem.getQuantity(), redisQty);
                String value = toJson(new CartItem(localItem.getProductId(), mergedQty));
                redisTemplate.opsForHash().put(key, field, value);
            } else {
                // Add new item from local cart
                String value = toJson(new CartItem(localItem.getProductId(), localItem.getQuantity()));
                redisTemplate.opsForHash().put(key, field, value);
            }
        }

        return getCart(userId);
    }

    @Override
    public void clearCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    /**
     * Parses a quantity value from a Redis-stored JSON string.
     *
     * @param value the stored value
     * @return the quantity
     */
    private int parseQuantity(String value) {
        try {
            CartItem item = objectMapper.readValue(value, CartItem.class);
            return item.getQuantity();
        } catch (JsonProcessingException e) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return 1;
            }
        }
    }

    /**
     * Serializes a CartItem to JSON.
     *
     * @param item the cart item
     * @return the JSON string
     */
    private String toJson(CartItem item) {
        try {
            return objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            return String.valueOf(item.getQuantity());
        }
    }

    /**
     * Internal cart item DTO for Redis storage.
     */
    private static class CartItem {
        private Long productId;
        private int quantity;

        CartItem(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
