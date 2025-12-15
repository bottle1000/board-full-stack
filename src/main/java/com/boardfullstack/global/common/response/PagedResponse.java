package com.boardfullstack.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {

    private final List<T> items;
    private final PageInfo pageInfo;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPage;
        private final boolean hasNext;
        private final boolean hsaPrevious;
    }

    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                new PageInfo(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.hasNext(),
                        page.hasPrevious())
        );
    }
}
