package com.honeybadgersoftware.productservice.utils.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private int offset;
    private int limit;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private int numberOfElements;

    public Page(List<T> content, int size, int pageNumber, int totalPages) {
        this.content = content;
        this.number = pageNumber;
        this.size = size;
        this.totalPages = totalPages;
    }
}
