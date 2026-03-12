package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.domain.Page;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(
        List<T> content,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        Boolean last,
        Boolean first
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }
}