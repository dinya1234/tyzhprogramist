package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SiteSettings;
import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SiteSettingsResponse(
        String pickupAddress,
        String pickupPhone,
        String pickupWorkingHours,
        BigDecimal deliveryCost,
        BigDecimal freeDeliveryThreshold,
        Boolean isFullyConfigured
) {
    public static SiteSettingsResponse from(SiteSettings settings) {
        if (settings == null) return null;

        return SiteSettingsResponse.builder()
                .pickupAddress(settings.getPickupAddress())
                .pickupPhone(settings.getPickupPhone())
                .pickupWorkingHours(settings.getPickupWorkingHours())
                .deliveryCost(settings.getDeliveryCost())
                .freeDeliveryThreshold(settings.getFreeDeliveryThreshold())
                .isFullyConfigured(
                        settings.getPickupAddress() != null &&
                                settings.getPickupPhone() != null &&
                                settings.getPickupWorkingHours() != null &&
                                settings.getDeliveryCost() != null &&
                                settings.getFreeDeliveryThreshold() != null
                )
                .build();
    }
}