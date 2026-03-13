package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotBlank
    private String deliveryMethod;

    @NotBlank
    @Size(max = 500)
    private String deliveryAddress;

    @NotBlank
    private String paymentMethod;

    @Size(max = 1000)
    private String comment;
}