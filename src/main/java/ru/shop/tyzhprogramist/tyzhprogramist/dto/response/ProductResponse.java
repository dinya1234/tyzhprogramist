package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponse(
        Long id,
        String name,
        String slug,
        BigDecimal price,
        BigDecimal oldPrice,
        Double rating,
        String mainImage,
        Boolean isActive,
        Boolean isNew,
        Boolean isBestseller,
        Integer quantity
) {
    public static ProductResponse from(Product product) {
        if (product == null) return null;

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .price(product.getPrice())
                .oldPrice(product.getOldPrice())
                .rating(product.getRating())
                .isActive(product.getIsActive())
                .isNew(product.getIsNew())
                .isBestseller(product.getIsBestseller())
                .quantity(product.getQuantity())
                .build();
    }
}