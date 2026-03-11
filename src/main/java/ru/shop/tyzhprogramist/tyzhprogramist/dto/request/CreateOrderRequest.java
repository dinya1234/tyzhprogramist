package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest (

@NotBlank(message = "выбор вариант получения не должен быть пустым")
 String deliveryMethod,

 @NotBlank(message = "адрес доставки или адрес магазина для самовывоза не может быть пустым")
 String deliveryAddress,

 @NotBlank(message = "обязательно нужно заполнить вариант оплаты")
 String paymentMethod,

 String comment
){ }
