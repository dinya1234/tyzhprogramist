package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SiteSettings;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.SiteSettingsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteSettingsService {

    private final SiteSettingsRepository siteSettingsRepository;

    @Transactional(readOnly = true)
    public SiteSettings getSettings() {
        return siteSettingsRepository.findSingleton()
                .orElseGet(() -> {
                    log.warn("Настройки сайта не найдены, создаются настройки по умолчанию");
                    return createDefaultSettings();
                });
    }

    @Transactional(readOnly = true)
    public Object getSettingsResponse() {
        SiteSettings settings = getSettings();
        return Map.of(
                "pickupAddress", settings.getPickupAddress(),
                "pickupPhone", settings.getPickupPhone(),
                "pickupWorkingHours", settings.getPickupWorkingHours(),
                "deliveryCost", settings.getDeliveryCost(),
                "freeDeliveryThreshold", settings.getFreeDeliveryThreshold()
        );
    }

    @Transactional
    public SiteSettings createDefaultSettings() {
        if (siteSettingsRepository.countSettings() > 0) {
            return siteSettingsRepository.findSingleton()
                    .orElseThrow(() -> new IllegalStateException("Настройки существуют, но не найдены"));
        }

        SiteSettings defaultSettings = new SiteSettings(
                "г. Ростов-на-Дону, ул. Программистов, д. 1",
                "+7 (863) 123-45-67",
                "Пн-Пт: 10:00-20:00, Сб-Вс: 11:00-18:00",
                new BigDecimal("500.00"),
                new BigDecimal("5000.00")
        );
        defaultSettings.setId(1L);

        SiteSettings saved = siteSettingsRepository.save(defaultSettings);
        log.info("Созданы настройки сайта по умолчанию");

        return saved;
    }

    @Transactional
    public SiteSettings updateSettings(SiteSettings updatedSettings) {
        SiteSettings settings = getSettings();

        settings.setPickupAddress(updatedSettings.getPickupAddress());
        settings.setPickupPhone(updatedSettings.getPickupPhone());
        settings.setPickupWorkingHours(updatedSettings.getPickupWorkingHours());
        settings.setDeliveryCost(updatedSettings.getDeliveryCost());
        settings.setFreeDeliveryThreshold(updatedSettings.getFreeDeliveryThreshold());

        SiteSettings saved = siteSettingsRepository.save(settings);
        log.info("Настройки сайта обновлены");

        return saved;
    }

    @Transactional
    public SiteSettings updatePickupAddress(String pickupAddress) {
        if (pickupAddress == null || pickupAddress.trim().isEmpty()) {
            throw new BadRequestException("Адрес самовывоза не может быть пустым");
        }

        siteSettingsRepository.updatePickupAddress(pickupAddress);
        log.info("Обновлен адрес самовывоза: {}", pickupAddress);

        return getSettings();
    }

    @Transactional
    public SiteSettings updatePickupPhone(String pickupPhone) {
        if (pickupPhone == null || pickupPhone.trim().isEmpty()) {
            throw new BadRequestException("Телефон самовывоза не может быть пустым");
        }

        siteSettingsRepository.updatePickupPhone(pickupPhone);
        log.info("Обновлен телефон самовывоза: {}", pickupPhone);

        return getSettings();
    }

    @Transactional
    public SiteSettings updateWorkingHours(String workingHours) {
        if (workingHours == null || workingHours.trim().isEmpty()) {
            throw new BadRequestException("Часы работы не могут быть пустыми");
        }

        siteSettingsRepository.updateWorkingHours(workingHours);
        log.info("Обновлены часы работы: {}", workingHours);

        return getSettings();
    }

    @Transactional
    public SiteSettings updateDeliveryCost(BigDecimal deliveryCost) {
        if (deliveryCost == null || deliveryCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Стоимость доставки не может быть отрицательной");
        }

        siteSettingsRepository.updateDeliveryCost(deliveryCost.setScale(2, RoundingMode.HALF_UP));
        log.info("Обновлена стоимость доставки: {}", deliveryCost);

        return getSettings();
    }

    @Transactional
    public SiteSettings updateFreeDeliveryThreshold(BigDecimal threshold) {
        if (threshold == null || threshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Порог бесплатной доставки не может быть отрицательным");
        }

        siteSettingsRepository.updateFreeDeliveryThreshold(threshold.setScale(2, RoundingMode.HALF_UP));
        log.info("Обновлен порог бесплатной доставки: {}", threshold);

        return getSettings();
    }

    @Transactional(readOnly = true)
    public BigDecimal getDeliveryCost() {
        return siteSettingsRepository.getDeliveryCost()
                .orElseThrow(() -> new NotFoundException("Стоимость доставки не настроена"));
    }

    @Transactional(readOnly = true)
    public BigDecimal getFreeDeliveryThreshold() {
        return siteSettingsRepository.getFreeDeliveryThreshold()
                .orElseThrow(() -> new NotFoundException("Порог бесплатной доставки не настроен"));
    }

    @Transactional(readOnly = true)
    public String getPickupAddress() {
        return siteSettingsRepository.getPickupAddress()
                .orElseThrow(() -> new NotFoundException("Адрес самовывоза не настроен"));
    }

    @Transactional(readOnly = true)
    public String getPickupPhone() {
        return siteSettingsRepository.getPickupPhone()
                .orElseThrow(() -> new NotFoundException("Телефон самовывоза не настроен"));
    }

    @Transactional(readOnly = true)
    public String getPickupWorkingHours() {
        return siteSettingsRepository.getPickupWorkingHours()
                .orElseThrow(() -> new NotFoundException("Часы работы не настроены"));
    }

    @Transactional(readOnly = true)
    public boolean isFreeDeliveryEligible(BigDecimal orderAmount) {
        if (orderAmount == null) {
            return false;
        }

        BigDecimal threshold = getFreeDeliveryThreshold();
        return orderAmount.compareTo(threshold) >= 0;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateDeliveryCost(BigDecimal orderAmount) {
        if (orderAmount == null) {
            return getDeliveryCost();
        }

        BigDecimal threshold = getFreeDeliveryThreshold();
        if (orderAmount.compareTo(threshold) >= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return getDeliveryCost();
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalWithDelivery(BigDecimal orderAmount) {
        if (orderAmount == null) {
            return getDeliveryCost();
        }

        BigDecimal deliveryCost = calculateDeliveryCost(orderAmount);
        return orderAmount.add(deliveryCost).setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDeliveryInfo(BigDecimal orderAmount) {
        BigDecimal deliveryCost = calculateDeliveryCost(orderAmount);
        boolean isFree = deliveryCost.compareTo(BigDecimal.ZERO) == 0;
        BigDecimal threshold = getFreeDeliveryThreshold();
        BigDecimal remainingForFree = isFree ? BigDecimal.ZERO :
                threshold.subtract(orderAmount).max(BigDecimal.ZERO);

        return Map.of(
                "deliveryCost", deliveryCost,
                "isFree", isFree,
                "freeDeliveryThreshold", threshold,
                "remainingForFree", remainingForFree
        );
    }

    @Transactional(readOnly = true)
    public boolean isFullyConfigured() {
        return siteSettingsRepository.isFullyConfigured();
    }

    @Transactional(readOnly = true)
    public String getSettingsInfo() {
        return siteSettingsRepository.getSettingsInfo()
                .orElse("Настройки не настроены");
    }

    @Transactional(readOnly = true)
    public List<SiteSettings> getAllSettings() {
        return siteSettingsRepository.findAllSettings();
    }

    @Transactional(readOnly = true)
    public boolean isSingletonValid() {
        return siteSettingsRepository.isSingletonValid();
    }

    @Transactional
    public SiteSettings consolidateSettings() {
        SiteSettings consolidated = siteSettingsRepository.consolidateSettings();
        log.info("Настройки сайта консолидированы");
        return consolidated;
    }

    @Transactional
    public SiteSettings resetToDefaults() {
        siteSettingsRepository.resetToDefaults();
        log.info("Настройки сайта сброшены к значениям по умолчанию");
        return getSettings();
    }

    @Transactional
    public int deleteDuplicates() {
        long count = siteSettingsRepository.countSettings();
        if (count <= 1) {
            return 0;
        }

        int deleted = siteSettingsRepository.deleteDuplicates();
        log.info("Удалено {} дубликатов настроек", deleted);
        return deleted;
    }

    public void validateSettings(SiteSettings settings) {
        if (settings.getPickupAddress() == null || settings.getPickupAddress().trim().isEmpty()) {
            throw new BadRequestException("Адрес самовывоза не может быть пустым");
        }

        if (settings.getPickupPhone() == null || settings.getPickupPhone().trim().isEmpty()) {
            throw new BadRequestException("Телефон самовывоза не может быть пустым");
        }

        if (settings.getPickupWorkingHours() == null || settings.getPickupWorkingHours().trim().isEmpty()) {
            throw new BadRequestException("Часы работы не могут быть пустыми");
        }

        if (settings.getDeliveryCost() == null) {
            throw new BadRequestException("Стоимость доставки должна быть указана");
        }

        if (settings.getDeliveryCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Стоимость доставки не может быть отрицательной");
        }

        if (settings.getFreeDeliveryThreshold() == null) {
            throw new BadRequestException("Порог бесплатной доставки должен быть указан");
        }

        if (settings.getFreeDeliveryThreshold().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Порог бесплатной доставки не может быть отрицательным");
        }
    }

    @Transactional(readOnly = true)
    public Object getSettingsStatistics() {
        SiteSettings settings = getSettings();

        return Map.of(
                "hasPickupAddress", settings.getPickupAddress() != null,
                "hasPickupPhone", settings.getPickupPhone() != null,
                "hasWorkingHours", settings.getPickupWorkingHours() != null,
                "deliveryCost", settings.getDeliveryCost(),
                "freeDeliveryThreshold", settings.getFreeDeliveryThreshold(),
                "isFullyConfigured", isFullyConfigured(),
                "singletonValid", isSingletonValid(),
                "settingsCount", siteSettingsRepository.countSettings()
        );
    }

    @Transactional
    public void initializeSettings() {
        if (siteSettingsRepository.countSettings() == 0) {
            createDefaultSettings();
            log.info("Настройки сайта инициализированы при запуске");
        } else if (!isSingletonValid()) {
            consolidateSettings();
            log.info("Настройки сайта сконсолидированы при запуске");
        } else {
            log.info("Настройки сайта уже существуют и валидны");
        }
    }

    public String getFormattedPickupAddress() {
        return "Адрес самовывоза: " + getPickupAddress();
    }

    public String getFormattedPickupPhone() {
        return "Телефон: " + getPickupPhone();
    }

    public String getFormattedWorkingHours() {
        return "Часы работы: " + getPickupWorkingHours();
    }

    public String getFormattedDeliveryInfo() {
        BigDecimal deliveryCost = getDeliveryCost();
        BigDecimal threshold = getFreeDeliveryThreshold();

        return String.format(
                "Доставка: %s руб. (бесплатно при заказе от %s руб.)",
                deliveryCost.toString(),
                threshold.toString()
        );
    }

    public Map<String, String> getContactInfo() {
        return Map.of(
                "address", getPickupAddress(),
                "phone", getPickupPhone(),
                "workingHours", getPickupWorkingHours(),
                "email", "info@tyzhprogramist.ru"
        );
    }
}