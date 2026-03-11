package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    Optional<User> findByUsernameOrEmail(@Param("login") String login);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);
    Page<User> findByRole(UserRole role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role IN ('ADMIN', 'CONSULTANT') AND u.isActive = true")
    List<User> findAvailableConsultants();

    List<User> findByIsActiveTrue();
    List<User> findByIsActiveFalse();

    @Query("SELECT u FROM User u WHERE u.lastActivity > :date")
    List<User> findByLastActivityAfter(@Param("date") LocalDateTime date);

    @Query("SELECT u FROM User u WHERE u.lastActivity < :date OR u.lastActivity IS NULL")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);

    List<User> findByEmailVerifiedFalse();
    List<User> findByConsentToChatDataTrue();
    List<User> findByNotificationsTrue();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cart WHERE u.id = :userId")
    Optional<User> findByIdWithCart(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders WHERE u.id = :userId")
    Optional<User> findByIdWithOrders(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.pcBuilds WHERE u.id = :userId")
    Optional<User> findByIdWithPcBuilds(@Param("userId") Long userId);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.cart " +
            "LEFT JOIN FETCH u.orders " +
            "LEFT JOIN FETCH u.pcBuilds " +
            "LEFT JOIN FETCH u.feedbacks " +
            "LEFT JOIN FETCH u.repairRequests " +
            "WHERE u.id = :userId")
    Optional<User> findByIdWithAllData(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<User> findByPhone(String phone);

    @Query("SELECT COUNT(u) FROM User u WHERE u.dateJoined BETWEEN :startDate AND :endDate")
    long countNewUsersBetween(@Param("startDate") LocalDateTime startDate,
                              @Param("endDate") LocalDateTime endDate);

    // ИСПРАВЛЕНО: Используем FUNCTION для DATE
    @Query("SELECT FUNCTION('DATE', u.dateJoined), COUNT(u) FROM User u " +
            "GROUP BY FUNCTION('DATE', u.dateJoined) " +
            "ORDER BY FUNCTION('DATE', u.dateJoined) DESC")
    List<Object[]> getRegistrationStatistics();

    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> getRoleStatistics();

    @Query("SELECT u, COUNT(o) as orderCount FROM User u " +
            "JOIN u.orders o " +
            "GROUP BY u " +
            "ORDER BY orderCount DESC")
    List<User> findTopUsersByOrderCount(Pageable pageable);

    @Query("SELECT u, SUM(o.totalPrice) as totalSpent FROM User u " +
            "JOIN u.orders o " +
            "GROUP BY u " +
            "ORDER BY totalSpent DESC")
    List<User> findTopUsersByTotalSpent(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.id IN :userIds")
    int updateUserActiveStatus(@Param("userIds") List<Long> userIds,
                               @Param("isActive") boolean isActive);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastActivity = :now WHERE u.id = :userId")
    int updateLastActivity(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.id = :userId")
    int verifyEmail(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.notifications = :enabled WHERE u.id = :userId")
    int updateNotificationSettings(@Param("userId") Long userId, @Param("enabled") boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.consentToChatData = :consent WHERE u.id = :userId")
    int updateChatDataConsent(@Param("userId") Long userId, @Param("consent") boolean consent);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.emailVerified = false AND u.dateJoined < :date")
    int deleteUnverifiedUsers(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :userId")
    int deleteUserCascade(@Param("userId") Long userId);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.orders o " +
            "ORDER BY o.createdAt DESC")
    List<User> findAllWithLastOrder();

    @Query("SELECT COUNT(u) FROM User u WHERE CAST(u.lastActivity AS date) = CURRENT_DATE")
    long countActiveToday();
}