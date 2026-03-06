package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SiteSettings;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteSettingsRepository extends JpaRepository<SiteSettings, Long> {

    @Query("SELECT s FROM SiteSettings s WHERE s.id = 1")
    Optional<SiteSettings> findSingleton();

    @Query("SELECT s FROM SiteSettings s WHERE s.id = 1")
    SiteSettings getSingleton();

    @Query("SELECT COUNT(s) > 0 FROM SiteSettings s WHERE s.id = 1")
    boolean existsSingleton();

    @Query("SELECT COUNT(s) FROM SiteSettings s")
    long countSettings();

    @Transactional
    default SiteSettings getOrCreateDefault() {
        return findSingleton().orElseGet(() -> {
            SiteSettings defaultSettings = new SiteSettings(
                    "г. Ростов, ул. Программистов, д. 1",
                    "+7 (988) 123-45-67",
                    "Пн-Пт: 10:00-20:00, Сб-Вс: 11:00-18:00",
                    new BigDecimal("500.00"),
                    new BigDecimal("5000.00")
            );
            defaultSettings.setId(1L);
            return save(defaultSettings);
        });
    }

    @Modifying
    @Transactional
    @Query("UPDATE SiteSettings s SET s.pickupAddress = :pickupAddress WHERE s.id = 1")
    int updatePickupAddress(@Param("pickupAddress") String pickupAddress);

    @Modifying
    @Transactional
    @Query("UPDATE SiteSettings s SET s.pickupPhone = :pickupPhone WHERE s.id = 1")
    int updatePickupPhone(@Param("pickupPhone") String pickupPhone);

    @Modifying
    @Transactional
    @Query("UPDATE SiteSettings s SET s.pickupWorkingHours = :workingHours WHERE s.id = 1")
    int updateWorkingHours(@Param("workingHours") String workingHours);

    @Modifying
    @Transactional
    @Query("UPDATE SiteSettings s SET s.deliveryCost = :deliveryCost WHERE s.id = 1")
    int updateDeliveryCost(@Param("deliveryCost") BigDecimal deliveryCost);

    @Modifying
    @Transactional
    @Query("UPDATE SiteSettings s SET s.freeDeliveryThreshold = :threshold WHERE s.id = 1")
    int updateFreeDeliveryThreshold(@Param("threshold") BigDecimal threshold);

    @Query("SELECT s.deliveryCost FROM SiteSettings s WHERE s.id = 1")
    Optional<BigDecimal> getDeliveryCost();

    @Query("SELECT s.freeDeliveryThreshold FROM SiteSettings s WHERE s.id = 1")
    Optional<BigDecimal> getFreeDeliveryThreshold();

    @Query("SELECT s.pickupAddress FROM SiteSettings s WHERE s.id = 1")
    Optional<String> getPickupAddress();

    @Query("SELECT s.pickupPhone FROM SiteSettings s WHERE s.id = 1")
    Optional<String> getPickupPhone();

    @Query("SELECT s.pickupWorkingHours FROM SiteSettings s WHERE s.id = 1")
    Optional<String> getPickupWorkingHours();

    @Query("SELECT CASE WHEN :orderAmount >= s.freeDeliveryThreshold THEN true ELSE false END " +
            "FROM SiteSettings s WHERE s.id = 1")
    boolean isFreeDeliveryEligible(@Param("orderAmount") BigDecimal orderAmount);

    @Query("SELECT CASE WHEN :orderAmount >= s.freeDeliveryThreshold THEN 0 ELSE s.deliveryCost END " +
            "FROM SiteSettings s WHERE s.id = 1")
    BigDecimal calculateDeliveryCost(@Param("orderAmount") BigDecimal orderAmount);

    @Query("SELECT CASE WHEN s.pickupAddress IS NOT NULL " +
            "AND s.pickupPhone IS NOT NULL " +
            "AND s.pickupWorkingHours IS NOT NULL " +
            "AND s.deliveryCost IS NOT NULL " +
            "AND s.freeDeliveryThreshold IS NOT NULL " +
            "THEN true ELSE false END " +
            "FROM SiteSettings s WHERE s.id = 1")
    boolean isFullyConfigured();

    @Modifying
    @Transactional
    @Query("UPDATE SiteSettings s SET " +
            "s.pickupAddress = 'г. Москва, ул. Программистов, д. 1', " +
            "s.pickupPhone = '+7 (495) 123-45-67', " +
            "s.pickupWorkingHours = 'Пн-Пт: 10:00-20:00, Сб-Вс: 11:00-18:00', " +
            "s.deliveryCost = 500.00, " +
            "s.freeDeliveryThreshold = 5000.00 " +
            "WHERE s.id = 1")
    int resetToDefaults();

    @Query("SELECT " +
            "'Адрес: ' || s.pickupAddress || ', ' || " +
            "'Телефон: ' || s.pickupPhone || ', ' || " +
            "'Часы: ' || s.pickupWorkingHours || ', ' || " +
            "'Доставка: ' || s.deliveryCost || ' руб., ' || " +
            "'Бесплатно от: ' || s.freeDeliveryThreshold || ' руб.' as settingsInfo " +
            "FROM SiteSettings s WHERE s.id = 1")
    Optional<String> getSettingsInfo();

    @Query("SELECT s FROM SiteSettings s ORDER BY s.id ASC")
    List<SiteSettings> findAllSettings();

    @Modifying
    @Transactional
    @Query("DELETE FROM SiteSettings s WHERE s.id > 1")
    int deleteDuplicates();

    @Transactional
    default SiteSettings consolidateSettings() {
        List<SiteSettings> allSettings = findAllSettings();

        if (allSettings.isEmpty()) {
            return getOrCreateDefault();
        }

        SiteSettings mainSettings = allSettings.get(0);
        mainSettings.setId(1L);

        if (allSettings.size() > 1) {
            deleteDuplicates();
        }

        return save(mainSettings);
    }

    @Query("SELECT CASE WHEN COUNT(s) = 1 AND MIN(s.id) = 1 THEN true ELSE false END FROM SiteSettings s")
    boolean isSingletonValid();
}