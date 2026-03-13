package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;

    public static CartItemResponse from(ProductItem item, Product product, String imageUrl) {
        if (item == null) return null;

        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setProductSlug(product.getSlug());
        response.setProductImage(imageUrl);
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
}