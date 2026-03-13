package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Order;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private OrderStatus status;
    private String deliveryMethod;
    private String deliveryAddress;
    private String paymentMethod;
    private BigDecimal totalPrice;
    private String comment;
    private LocalDateTime createdAt;

    private Long userId;
    private String userName;
    private String userEmail;

    private List<OrderItemResponse> items = new ArrayList<>();

    public static OrderResponse from(Order order) {
        if (order == null) return null;

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber("ORD-" + order.getId());
        response.setStatus(order.getStatus());
        response.setDeliveryMethod(order.getDeliveryMethod());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setTotalPrice(order.getTotalPrice());
        response.setComment(order.getComment());
        response.setCreatedAt(order.getCreatedAt());

        if (order.getUser() != null) {
            response.setUserId(order.getUser().getId());
            response.setUserName(order.getUser().getUsername());
            response.setUserEmail(order.getUser().getEmail());
        }

        return response;
    }
}