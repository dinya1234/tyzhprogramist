package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PcBuildComponentResponse {
    private String componentType;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}