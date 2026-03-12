package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        String productSlug,
        String productImage,
        BigDecimal price,
        Integer quantity,
        BigDecimal totalPrice
) {
    public static CartItemResponse from(ProductItem item, Product product, String imageUrl) {
        if (item == null) return null;

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .productImage(imageUrl)
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}