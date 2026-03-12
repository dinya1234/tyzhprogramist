package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductFullResponse(
        Long id,
        String sku,
        String name,
        String slug,
        String shortDescription,
        String fullDescription,
        BigDecimal price,
        BigDecimal oldPrice,
        Integer quantity,
        Double rating,
        Integer warrantyMonths,
        BigDecimal weight,
        Boolean isActive,
        Boolean isNew,
        Boolean isBestseller,
        Integer viewsCount,
        Integer purchaseCount,

        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDateTime createdAt,

        CategoryResponse category,
        List<String> images,
        List<ProductResponse> relatedProducts,
        List<ProductResponse> frequentlyBought
) {
    public static ProductFullResponse from(Product product) {
        if (product == null) return null;

        return ProductFullResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .slug(product.getSlug())
                .shortDescription(product.getShortDescription())
                .fullDescription(product.getFullDescription())
                .price(product.getPrice())
                .oldPrice(product.getOldPrice())
                .quantity(product.getQuantity())
                .rating(product.getRating())
                .warrantyMonths(product.getWarrantyMonths())
                .weight(product.getWeight())
                .isActive(product.getIsActive())
                .isNew(product.getIsNew())
                .isBestseller(product.getIsBestseller())
                .viewsCount(product.getViewsCount())
                .purchaseCount(product.getPurchaseCount())
                .createdAt(product.getCreatedAt())
                .category(CategoryResponse.simple(product.getCategory()))
                .images(product.getImages() != null ?
                        product.getImages().stream()
                                .filter(img -> img.getIsMain() || true)
                                .map(img -> img.getFilePath())
                                .collect(Collectors.toList()) : null)
                .relatedProducts(product.getRelatedProducts() != null ?
                        product.getRelatedProducts().stream()
                                .map(ProductResponse::from)
                                .collect(Collectors.toList()) : null)
                .frequentlyBought(product.getFrequentlyBoughtWith() != null ?
                        product.getFrequentlyBoughtWith().stream()
                                .map(ProductResponse::from)
                                .collect(Collectors.toList()) : null)
                .build();
    }
}