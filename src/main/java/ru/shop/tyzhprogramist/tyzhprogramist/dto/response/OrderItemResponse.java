package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        String productSlug,
        BigDecimal price,
        Integer quantity,
        BigDecimal totalPrice
) {
    public static OrderItemResponse from(ProductItem item, Product product) {
        if (item == null) return null;

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}