package com.qlst.util;

import java.util.Collections;
import java.util.List;


public final class Page<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final int totalItems;
    private final int totalPages;
    private final int startItem;
    private final int endItem;

    public Page(List<T> content, int pageNumber, int pageSize,
                int totalItems, int totalPages, int startItem, int endItem) {
        this.content = content == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(content);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.startItem = startItem;
        this.endItem = endItem;
    }

    public List<T> getContent() {
        return content;
    }


    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getStartItem() {
        return startItem;
    }


    public int getEndItem() {
        return endItem;
    }

    public boolean isHasPrevious() {
        return pageNumber > 1;
    }

    public boolean isHasNext() {
        return pageNumber < totalPages;
    }
}
