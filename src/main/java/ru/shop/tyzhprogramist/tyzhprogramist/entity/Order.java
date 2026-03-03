package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "delivery_method", nullable = false)
    private String deliveryMethod;  // курьер, самовывоз, хз что надо будет обсудить с добавлением енумов

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;   // карта онлайн, при получении, рассрочка тож самое что и с delivery_method

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.NEW;
    }

    public Order(User user, String deliveryMethod, String deliveryAddress,
                 String paymentMethod, BigDecimal totalPrice, String comment) {
        this.user = user;
        this.deliveryMethod = deliveryMethod;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.comment = comment;
        this.status = OrderStatus.NEW;
        this.createdAt = LocalDateTime.now();
    }
}