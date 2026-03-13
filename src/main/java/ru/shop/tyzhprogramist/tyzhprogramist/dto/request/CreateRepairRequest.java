package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateRepairRequest {

    @NotBlank
    @Size(max = 100)
    private String deviceType;

    @NotBlank
    @Size(max = 2000)
    private String problemDescription;

    private BigDecimal estimatedPrice;
}