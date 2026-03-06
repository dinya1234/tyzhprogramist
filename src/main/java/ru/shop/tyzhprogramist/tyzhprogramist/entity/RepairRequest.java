package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "repair_requests")
public class RepairRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_type", nullable = false, length = 100)
    private String deviceType;

    @Column(name = "problem_description", nullable = false, length = 2000)
    private String problemDescription;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "master_comment", length = 2000)
    private String masterComment;

    @Column(name = "estimated_price", precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(name = "final_price", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public RepairRequest() {
        this.createdAt = LocalDateTime.now();
        this.status = "Принята";
    }

    public RepairRequest(User user, String deviceType, String problemDescription,
                         BigDecimal estimatedPrice) {
        this.user = user;
        this.deviceType = deviceType;
        this.problemDescription = problemDescription;
        this.estimatedPrice = estimatedPrice;
        this.status = "Принята";
        this.createdAt = LocalDateTime.now();
    }
}