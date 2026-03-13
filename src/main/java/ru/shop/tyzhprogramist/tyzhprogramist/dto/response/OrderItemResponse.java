package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;

    public static OrderItemResponse from(ProductItem item, Product product) {
        if (item == null) return null;

        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setProductSlug(product.getSlug());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
}