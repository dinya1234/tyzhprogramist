package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PcBuildComponentResponse(
        String componentType,
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity
) {
    public static PcBuildComponentResponse from(ProductItem item, Product product, String componentType) {
        if (item == null) return null;

        return PcBuildComponentResponse.builder()
                .componentType(componentType)
                .productId(product.getId())
                .productName(product.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}