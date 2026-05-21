package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Paginated response wrapper.
 *
 * @param <T> the type of content items
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    /**
     * Creates a PageResponse from Spring Data Page.
     *
     * @param content the list of items on the current page
     * @param totalElements the total number of elements
     * @param totalPages the total number of pages
     * @param number the current page number (0-based)
     * @param size the page size
     * @param <T> the content type
     * @return the PageResponse
     */
    public static <T> PageResponse<T> of(List<T> content, long totalElements,
                                          int totalPages, int number, int size) {
        return new PageResponse<>(content, totalElements, totalPages, number, size);
    }
}
