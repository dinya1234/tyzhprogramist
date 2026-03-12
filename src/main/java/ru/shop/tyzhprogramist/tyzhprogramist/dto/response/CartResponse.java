package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Cart;
import java.math.BigDecimal;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartResponse(
        Long id,
        BigDecimal totalPrice,
        Integer totalItems,
        List<CartItemResponse> items
) {
    public static CartResponse from(Cart cart, List<CartItemResponse> items,
                                    BigDecimal totalPrice, Integer totalItems) {
        if (cart == null) return null;

        return CartResponse.builder()
                .id(cart.getId())
                .totalPrice(totalPrice)
                .totalItems(totalItems)
                .items(items)
                .build();
    }
}