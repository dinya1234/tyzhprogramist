package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.SiteSettingsResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SiteSettings;
import ru.shop.tyzhprogramist.tyzhprogramist.service.SiteSettingsService;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SiteSettingsController {

    private final SiteSettingsService siteSettingsService;

    @GetMapping("/public")
    public ResponseEntity<Object> getPublicSettings() {
        return ResponseEntity.ok(siteSettingsService.getSettingsResponse());
    }

    @GetMapping("/delivery-info")
    public ResponseEntity<Map<String, Object>> getDeliveryInfo(@RequestParam(required = false) BigDecimal orderAmount) {
        if (orderAmount == null) {
            return ResponseEntity.ok(Map.of(
                    "deliveryCost", siteSettingsService.getDeliveryCost(),
                    "freeDeliveryThreshold", siteSettingsService.getFreeDeliveryThreshold()
            ));
        }
        return ResponseEntity.ok(siteSettingsService.getDeliveryInfo(orderAmount));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<Map<String, String>> getContactInfo() {
        return ResponseEntity.ok(siteSettingsService.getContactInfo());
    }

    @GetMapping("/delivery-cost")
    public ResponseEntity<BigDecimal> getDeliveryCost() {
        return ResponseEntity.ok(siteSettingsService.getDeliveryCost());
    }

    @GetMapping("/free-delivery-threshold")
    public ResponseEntity<BigDecimal> getFreeDeliveryThreshold() {
        return ResponseEntity.ok(siteSettingsService.getFreeDeliveryThreshold());
    }

    @GetMapping("/pickup-address")
    public ResponseEntity<String> getPickupAddress() {
        return ResponseEntity.ok(siteSettingsService.getPickupAddress());
    }

    @GetMapping("/pickup-phone")
    public ResponseEntity<String> getPickupPhone() {
        return ResponseEntity.ok(siteSettingsService.getPickupPhone());
    }

    @GetMapping("/working-hours")
    public ResponseEntity<String> getWorkingHours() {
        return ResponseEntity.ok(siteSettingsService.getPickupWorkingHours());
    }

    @GetMapping("/is-free-delivery")
    public ResponseEntity<Boolean> isFreeDeliveryEligible(@RequestParam BigDecimal orderAmount) {
        return ResponseEntity.ok(siteSettingsService.isFreeDeliveryEligible(orderAmount));
    }

    @GetMapping("/calculate-delivery")
    public ResponseEntity<Map<String, Object>> calculateDelivery(@RequestParam BigDecimal orderAmount) {
        BigDecimal deliveryCost = siteSettingsService.calculateDeliveryCost(orderAmount);
        BigDecimal total = siteSettingsService.calculateTotalWithDelivery(orderAmount);

        return ResponseEntity.ok(Map.of(
                "orderAmount", orderAmount,
                "deliveryCost", deliveryCost,
                "totalWithDelivery", total
        ));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> getSettings() {
        return ResponseEntity.ok(SiteSettingsResponse.from(siteSettingsService.getSettings()));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> updateSettings(@Valid @RequestBody SiteSettings settings) {
        SiteSettings updated = siteSettingsService.updateSettings(settings);
        log.info("Админ обновил настройки сайта");
        return ResponseEntity.ok(SiteSettingsResponse.from(updated));
    }

    @PutMapping("/pickup-address")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> updatePickupAddress(@RequestParam String address) {
        SiteSettings settings = siteSettingsService.updatePickupAddress(address);
        log.info("Админ обновил адрес самовывоза: {}", address);
        return ResponseEntity.ok(SiteSettingsResponse.from(settings));
    }

    @PutMapping("/pickup-phone")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> updatePickupPhone(@RequestParam String phone) {
        SiteSettings settings = siteSettingsService.updatePickupPhone(phone);
        log.info("Админ обновил телефон самовывоза: {}", phone);
        return ResponseEntity.ok(SiteSettingsResponse.from(settings));
    }

    @PutMapping("/working-hours")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> updateWorkingHours(@RequestParam String hours) {
        SiteSettings settings = siteSettingsService.updateWorkingHours(hours);
        log.info("Админ обновил часы работы: {}", hours);
        return ResponseEntity.ok(SiteSettingsResponse.from(settings));
    }

    @PutMapping("/delivery-cost")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> updateDeliveryCost(@RequestParam BigDecimal cost) {
        SiteSettings settings = siteSettingsService.updateDeliveryCost(cost);
        log.info("Админ обновил стоимость доставки: {}", cost);
        return ResponseEntity.ok(SiteSettingsResponse.from(settings));
    }

    @PutMapping("/free-delivery-threshold")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> updateFreeDeliveryThreshold(@RequestParam BigDecimal threshold) {
        SiteSettings settings = siteSettingsService.updateFreeDeliveryThreshold(threshold);
        log.info("Админ обновил порог бесплатной доставки: {}", threshold);
        return ResponseEntity.ok(SiteSettingsResponse.from(settings));
    }

    @PostMapping("/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SiteSettingsResponse> resetToDefaults() {
        SiteSettings settings = siteSettingsService.resetToDefaults();
        log.info("Админ сбросил настройки сайта к значениям по умолчанию");
        return ResponseEntity.ok(SiteSettingsResponse.from(settings));
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initializeSettings() {
        siteSettingsService.initializeSettings();
        log.info("Админ инициализировал настройки сайта");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/duplicates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteDuplicates() {
        int deleted = siteSettingsService.deleteDuplicates();
        log.info("Админ удалил {} дубликатов настроек", deleted);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getSettingsStatistics() {
        return ResponseEntity.ok(siteSettingsService.getSettingsStatistics());
    }

    @GetMapping("/is-configured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> isFullyConfigured() {
        return ResponseEntity.ok(siteSettingsService.isFullyConfigured());
    }
}