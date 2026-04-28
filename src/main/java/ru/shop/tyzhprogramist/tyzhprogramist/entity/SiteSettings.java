package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "site_settings")
public class SiteSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "pickup_phone", nullable = false, length = 20)
    private String pickupPhone;

    @Column(name = "pickup_working_hours", nullable = false)
    private String pickupWorkingHours;

    @Column(name = "delivery_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryCost;

    @Column(name = "free_delivery_threshold", nullable = false, precision = 10, scale = 2)
    private BigDecimal freeDeliveryThreshold;

    public SiteSettings() {}

    public SiteSettings(String pickupAddress, String pickupPhone,
                        String pickupWorkingHours,
                        BigDecimal deliveryCost, BigDecimal freeDeliveryThreshold) {
        this.pickupAddress = pickupAddress;
        this.pickupPhone = pickupPhone;
        this.pickupWorkingHours = pickupWorkingHours;
        this.deliveryCost = deliveryCost;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }
}