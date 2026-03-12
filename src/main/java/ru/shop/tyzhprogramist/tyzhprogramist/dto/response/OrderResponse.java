package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Order;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponse(
        Long id,
        String orderNumber,
        OrderStatus status,
        String deliveryMethod,
        String deliveryAddress,
        String paymentMethod,
        BigDecimal totalPrice,
        String comment,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime createdAt,

        UserResponse user,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order, List<OrderItemResponse> items) {
        if (order == null) return null;

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber("ORD-" + order.getId())
                .status(order.getStatus())
                .deliveryMethod(order.getDeliveryMethod())
                .deliveryAddress(order.getDeliveryAddress())
                .paymentMethod(order.getPaymentMethod())
                .totalPrice(order.getTotalPrice())
                .comment(order.getComment())
                .createdAt(order.getCreatedAt())
                .user(UserResponse.from(order.getUser()))
                .items(items)
                .build();
    }
}