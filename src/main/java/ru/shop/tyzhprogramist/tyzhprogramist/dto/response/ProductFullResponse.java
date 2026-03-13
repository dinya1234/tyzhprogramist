package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductFullResponse {
    private Long id;
    private String sku;
    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private Integer quantity;
    private Double rating;
    private Integer warrantyMonths;
    private BigDecimal weight;
    private Boolean isActive;
    private Boolean isNew;
    private Boolean isBestseller;
    private Integer viewsCount;
    private Integer purchaseCount;
    private LocalDateTime createdAt;

    private Long categoryId;
    private String categoryName;
    private String categorySlug;

    private List<String> images = new ArrayList<>();
    private String mainImage;

    private List<ProductResponse> relatedProducts = new ArrayList<>();

    private List<ProductResponse> frequentlyBought = new ArrayList<>();

    public static ProductFullResponse from(Product product) {
        if (product == null) return null;

        ProductFullResponse response = new ProductFullResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setShortDescription(product.getShortDescription());
        response.setFullDescription(product.getFullDescription());
        response.setPrice(product.getPrice());
        response.setOldPrice(product.getOldPrice());
        response.setQuantity(product.getQuantity());
        response.setRating(product.getRating());
        response.setWarrantyMonths(product.getWarrantyMonths());
        response.setWeight(product.getWeight());
        response.setIsActive(product.getIsActive());
        response.setIsNew(product.getIsNew());
        response.setIsBestseller(product.getIsBestseller());
        response.setViewsCount(product.getViewsCount());
        response.setPurchaseCount(product.getPurchaseCount());
        response.setCreatedAt(product.getCreatedAt());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
            response.setCategorySlug(product.getCategory().getSlug());
        }

        return response;
    }
}