package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SiteSettings;
import java.math.BigDecimal;

@Data
public class SiteSettingsResponse {
    private String pickupAddress;
    private String pickupPhone;
    private String pickupWorkingHours;
    private BigDecimal deliveryCost;
    private BigDecimal freeDeliveryThreshold;

    public static SiteSettingsResponse from(SiteSettings settings) {
        if (settings == null) return null;

        SiteSettingsResponse response = new SiteSettingsResponse();
        response.setPickupAddress(settings.getPickupAddress());
        response.setPickupPhone(settings.getPickupPhone());
        response.setPickupWorkingHours(settings.getPickupWorkingHours());
        response.setDeliveryCost(settings.getDeliveryCost());
        response.setFreeDeliveryThreshold(settings.getFreeDeliveryThreshold());
        return response;
    }
}