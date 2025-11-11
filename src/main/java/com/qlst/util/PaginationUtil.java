package com.qlst.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class PaginationUtil {
    public static final int DEFAULT_PAGE_SIZE = 6;

    private PaginationUtil() {
    }

    public static int parsePage(String rawValue) {
        int defaultPage = 1;
        if (rawValue == null || rawValue.isBlank()) {
            return defaultPage;
        }
        try {
            int parsed = Integer.parseInt(rawValue);
            return parsed > 0 ? parsed : defaultPage;
        } catch (NumberFormatException ex) {
            return defaultPage;
        }
    }

    public static int parsePageSize(String rawValue, int defaultSize) {
        int fallback = defaultSize <= 0 ? DEFAULT_PAGE_SIZE : defaultSize;
        if (rawValue == null || rawValue.isBlank()) {
            return fallback;
        }
        try {
            int parsed = Integer.parseInt(rawValue);
            if (parsed <= 0) {
                return fallback;
            }
            return Math.min(parsed, 100); // keep extreme values in check
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    public static <T> Page<T> paginate(List<T> source, int requestedPage, int requestedSize) {
        List<T> data = source == null ? Collections.emptyList() : source;
        int totalItems = data.size();
        int pageSize = requestedSize <= 0 ? DEFAULT_PAGE_SIZE : requestedSize;
        int totalPages = totalItems == 0 ? 1 : (int) Math.ceil((double) totalItems / pageSize);
        int pageNumber = requestedPage <= 0 ? 1 : requestedPage;
        if (pageNumber > totalPages) {
            pageNumber = totalPages;
        }

        int fromIndex = totalItems == 0 ? 0 : (pageNumber - 1) * pageSize;
        if (fromIndex > totalItems) {
            fromIndex = Math.max(totalItems - pageSize, 0);
        }
        int toIndex = totalItems == 0 ? 0 : Math.min(fromIndex + pageSize, totalItems);

        List<T> pageContent;
        if (totalItems == 0) {
            pageContent = Collections.emptyList();
            pageNumber = 1;
            fromIndex = 0;
            toIndex = 0;
            totalPages = 1;
        } else {
            pageContent = new ArrayList<>(data.subList(fromIndex, toIndex));
        }

        int startItem = totalItems == 0 ? 0 : fromIndex + 1;
        int endItem = totalItems == 0 ? 0 : toIndex;

        return new Page<>(pageContent, pageNumber, pageSize, totalItems, totalPages, startItem, endItem);
    }
}
