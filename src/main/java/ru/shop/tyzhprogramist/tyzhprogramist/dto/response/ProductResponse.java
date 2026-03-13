package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private Double rating;
    private String mainImage;
    private Boolean isNew;
    private Boolean isBestseller;
    private Integer quantity;
    private Long categoryId;
    private String categoryName;

    public static ProductResponse from(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setPrice(product.getPrice());
        response.setOldPrice(product.getOldPrice());
        response.setRating(product.getRating());
        response.setIsNew(product.getIsNew());
        response.setIsBestseller(product.getIsBestseller());
        response.setQuantity(product.getQuantity());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }

        return response;
    }
}