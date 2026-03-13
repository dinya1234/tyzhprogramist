package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Cart;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private BigDecimal totalPrice;
    private Integer totalItems;
    private List<CartItemResponse> items = new ArrayList<>();

    public static CartResponse from(Cart cart, List<CartItemResponse> items,
                                    BigDecimal totalPrice, Integer totalItems) {
        if (cart == null) return null;

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setTotalPrice(totalPrice);
        response.setTotalItems(totalItems);
        response.setItems(items);
        return response;
    }
}