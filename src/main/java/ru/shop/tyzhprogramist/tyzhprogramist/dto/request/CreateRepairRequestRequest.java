package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateRepairRequestRequest (
        @NotBlank
        String  deviceType,
        @NotBlank
        String problemDescription,

        BigDecimal estimatedPrice
        ){
}
