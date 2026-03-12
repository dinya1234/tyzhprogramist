package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String image,
        Integer order,
        CategoryResponse parent,
        List<CategoryResponse> children
) {
    public static CategoryResponse from(Category category) {
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .image(category.getImage())
                .order(category.getOrder())
                .parent(CategoryResponse.from(category.getParent()))
                .children(category.getChildren() != null ?
                        category.getChildren().stream()
                                .map(CategoryResponse::from)
                                .collect(Collectors.toList()) : null)
                .build();
    }


    public static CategoryResponse simple(Category category) {
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .image(category.getImage())
                .build();
    }
}