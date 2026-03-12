package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String image,
        Integer order,
        CategoryResponse parent,
        List<CategoryResponse> children
) {
}