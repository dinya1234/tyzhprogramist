package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RepairRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    List<RepairRequest> findByUser(User user);
    Page<RepairRequest> findByUser(User user, Pageable pageable);
    Page<RepairRequest> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM RepairRequest r WHERE r.user.id = :userId AND r.createdAt >= :since ORDER BY r.createdAt DESC")
    List<RepairRequest> findUserRecentRequests(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    List<RepairRequest> findByStatus(String status);
    Page<RepairRequest> findByStatus(String status, Pageable pageable);
    List<RepairRequest> findByUserAndStatus(User user, String status);

    @Query("SELECT r FROM RepairRequest r WHERE r.status IN :statuses ORDER BY r.createdAt DESC")
    List<RepairRequest> findByStatusIn(@Param("statuses") List<String> statuses);

    @Query("SELECT r FROM RepairRequest r WHERE r.status = 'Принята' ORDER BY r.createdAt ASC")
    List<RepairRequest> findNewRequests();

    @Query("SELECT r FROM RepairRequest r WHERE r.status IN ('Принята', 'Диагностика', 'Ремонт') ORDER BY r.createdAt DESC")
    List<RepairRequest> findInProgressRequests();

    @Query("SELECT r FROM RepairRequest r WHERE r.status IN ('Готов к выдаче', 'Выдан') ORDER BY r.createdAt DESC")
    List<RepairRequest> findCompletedRequests();

    List<RepairRequest> findByDeviceType(String deviceType);
    Page<RepairRequest> findByDeviceType(String deviceType, Pageable pageable);

    @Query("SELECT r.deviceType, COUNT(r) FROM RepairRequest r GROUP BY r.deviceType ORDER BY COUNT(r) DESC")
    List<Object[]> getDeviceTypeStatistics();

    List<RepairRequest> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RepairRequest> findByCreatedAtAfter(LocalDateTime date);
    List<RepairRequest> findByCreatedAtBefore(LocalDateTime date);

    @Query("SELECT r FROM RepairRequest r WHERE CAST(r.createdAt AS date) = CURRENT_DATE")
    List<RepairRequest> findTodayRequests();

    @Query("SELECT r FROM RepairRequest r WHERE EXTRACT(YEAR FROM r.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM r.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<RepairRequest> findCurrentMonthRequests();

    List<RepairRequest> findByEstimatedPriceGreaterThanEqual(BigDecimal minPrice);
    List<RepairRequest> findByFinalPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT r FROM RepairRequest r WHERE r.finalPrice IS NOT NULL AND r.estimatedPrice IS NOT NULL AND r.finalPrice != r.estimatedPrice")
    List<RepairRequest> findWithPriceDifference();

    long countByUser(User user);
    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(r.finalPrice), 0) FROM RepairRequest r " +
            "WHERE r.status = 'Выдан' AND r.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalRevenue(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r.status, COUNT(r), COALESCE(SUM(r.finalPrice), 0) FROM RepairRequest r GROUP BY r.status")
    List<Object[]> getStatusStatistics();

    @Query("SELECT FUNCTION('DATE', r.createdAt), COUNT(r), COALESCE(SUM(r.finalPrice), 0) " +
            "FROM RepairRequest r WHERE r.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', r.createdAt) ORDER BY FUNCTION('DATE', r.createdAt)")
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r.deviceType, AVG(r.finalPrice) FROM RepairRequest r " +
            "WHERE r.status = 'Выдан' GROUP BY r.deviceType")
    List<Object[]> getAveragePriceByDeviceType();

    @Modifying
    @Transactional
    @Query("UPDATE RepairRequest r SET r.status = :status WHERE r.id = :requestId")
    int updateStatus(@Param("requestId") Long requestId, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE RepairRequest r SET r.masterComment = :comment WHERE r.id = :requestId")
    int updateMasterComment(@Param("requestId") Long requestId, @Param("comment") String comment);

    @Modifying
    @Transactional
    @Query("UPDATE RepairRequest r SET r.estimatedPrice = :price WHERE r.id = :requestId")
    int updateEstimatedPrice(@Param("requestId") Long requestId, @Param("price") BigDecimal price);

    @Modifying
    @Transactional
    @Query("UPDATE RepairRequest r SET r.finalPrice = :price WHERE r.id = :requestId")
    int updateFinalPrice(@Param("requestId") Long requestId, @Param("price") BigDecimal price);

    @Modifying
    @Transactional
    @Query("UPDATE RepairRequest r SET r.status = 'Готов к выдаче', r.finalPrice = :finalPrice WHERE r.id = :requestId")
    int completeRepair(@Param("requestId") Long requestId, @Param("finalPrice") BigDecimal finalPrice);

    @Modifying
    @Transactional
    @Query("UPDATE RepairRequest r SET r.status = 'Выдан' WHERE r.id = :requestId")
    int markAsIssued(@Param("requestId") Long requestId);

    @Query("SELECT r FROM RepairRequest r WHERE r.status = 'Диагностика' AND r.createdAt < :date")
    List<RepairRequest> findStuckInDiagnostics(@Param("date") LocalDateTime date);

    @Query("SELECT r FROM RepairRequest r WHERE r.status = 'Готов к выдаче' AND r.createdAt < :date")
    List<RepairRequest> findReadyButNotIssued(@Param("date") LocalDateTime date);

    @Query("SELECT r FROM RepairRequest r WHERE r.masterComment IS NULL OR r.masterComment = ''")
    List<RepairRequest> findWithoutMasterComment();

    @Query("SELECT r FROM RepairRequest r WHERE r.status = 'Готов к выдаче' AND r.finalPrice IS NULL")
    List<RepairRequest> findReadyWithoutFinalPrice();

    @Query("SELECT r FROM RepairRequest r WHERE LOWER(r.problemDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<RepairRequest> searchByProblemDescription(@Param("searchTerm") String searchTerm);

    @Query("SELECT r FROM RepairRequest r WHERE LOWER(r.masterComment) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<RepairRequest> searchByMasterComment(@Param("searchTerm") String searchTerm);

    @Query("SELECT FUNCTION('DATE', r.createdAt), " +
            "COUNT(r), " +
            "SUM(CASE WHEN r.status = 'Выдан' THEN 1 ELSE 0 END) as completed, " +
            "COALESCE(SUM(r.finalPrice), 0) as revenue " +
            "FROM RepairRequest r " +
            "WHERE r.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', r.createdAt) " +
            "ORDER BY FUNCTION('DATE', r.createdAt)")
    List<Object[]> getRepairReport(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r.problemDescription, COUNT(r) as problemCount " +
            "FROM RepairRequest r " +
            "GROUP BY r.problemDescription " +
            "ORDER BY problemCount DESC")
    List<Object[]> getMostCommonProblems(Pageable pageable);

    @Query(value = "SELECT r.status, AVG(EXTRACT(EPOCH FROM (COALESCE(r.updated_at, r.created_at) - r.created_at))/86400) " +
            "FROM repair_requests r GROUP BY r.status", nativeQuery = true)
    List<Object[]> getAverageRepairTimeByStatus();

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM RepairRequest r WHERE r.status IN ('Выдан') AND r.createdAt < :date")
    int deleteOldCompletedRequests(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query("DELETE FROM RepairRequest r WHERE r.status = 'Отменена' AND r.createdAt < :date")
    int deleteOldCancelledRequests(@Param("date") LocalDateTime date);

    Page<RepairRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<RepairRequest> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<RepairRequest> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    @Query("SELECT COUNT(r) > 0 FROM RepairRequest r WHERE r.user.id = :userId AND r.status NOT IN ('Выдан', 'Отменена')")
    boolean hasActiveRequests(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) > 0 FROM RepairRequest r " +
            "WHERE r.user.id = :userId AND LOWER(r.problemDescription) LIKE LOWER(CONCAT('%', :description, '%')) " +
            "AND r.createdAt > :since")
    boolean hasSimilarRequest(@Param("userId") Long userId,
                              @Param("description") String description,
                              @Param("since") LocalDateTime since);
}