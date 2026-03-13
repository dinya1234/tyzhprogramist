package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;


@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String image;
    private Integer order;
    private Long parentId;
    private String parentName;

    public static CategoryResponse from(Category category) {
        if (category == null) return null;

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setImage(category.getImage());
        response.setOrder(category.getOrder());

        if (category.getParent() != null) {
            response.setParentId(category.getParent().getId());
            response.setParentName(category.getParent().getName());
        }

        return response;
    }
}