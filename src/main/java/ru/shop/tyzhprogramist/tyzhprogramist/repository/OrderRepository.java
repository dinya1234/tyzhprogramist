package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Order;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.OrderStatus;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
    Page<Order> findByUser(User user, Pageable pageable);
    Page<Order> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.createdAt >= :since ORDER BY o.createdAt DESC")
    List<Order> findUserRecentOrders(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    List<Order> findByStatus(OrderStatus status);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    List<Order> findByUserAndStatus(User user, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status IN :statuses ORDER BY o.createdAt DESC")
    List<Order> findByStatusIn(@Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.status = 'NEW' ORDER BY o.createdAt ASC")
    List<Order> findNewOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'CANCELLED' AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findCancelledOrdersBetween(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByCreatedAtAfter(LocalDateTime date);
    List<Order> findByCreatedAtBefore(LocalDateTime date);

    @Query("SELECT o FROM Order o WHERE CAST(o.createdAt AS date) = CURRENT_DATE")
    List<Order> findTodayOrders();

    @Query("SELECT o FROM Order o WHERE EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE)")
    List<Order> findCurrentMonthOrders();

    List<Order> findByTotalPriceGreaterThanEqual(BigDecimal minPrice);
    List<Order> findByTotalPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Order> findByDeliveryMethod(String deliveryMethod);
    List<Order> findByPaymentMethod(String paymentMethod);

    @Query("SELECT o FROM Order o WHERE o.deliveryMethod = :deliveryMethod AND o.paymentMethod = :paymentMethod")
    List<Order> findByDeliveryAndPayment(@Param("deliveryMethod") String deliveryMethod,
                                         @Param("paymentMethod") String paymentMethod);

    @Query("SELECT o FROM Order o WHERE LOWER(o.deliveryAddress) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Order> searchByDeliveryAddress(@Param("address") String address);

    long countByUser(User user);
    long countByUserAndStatus(User user, OrderStatus status);
    long countByStatus(OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.user.id = :userId AND o.status != 'CANCELLED'")
    BigDecimal sumTotalSpentByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status != 'CANCELLED'")
    BigDecimal sumTotalBetween(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(AVG(o.totalPrice), 0) FROM Order o WHERE o.status != 'CANCELLED'")
    BigDecimal getAverageOrderValue();

    @Query("SELECT o.status, COUNT(o), COALESCE(SUM(o.totalPrice), 0) FROM Order o GROUP BY o.status")
    List<Object[]> getOrderStatusStatistics();

    @Query("SELECT FUNCTION('DATE', o.createdAt), COUNT(o), COALESCE(SUM(o.totalPrice), 0) " +
            "FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', o.createdAt) ORDER BY FUNCTION('DATE', o.createdAt)")
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    List<Order> findTopByOrderByTotalPriceDesc(Pageable pageable);
    List<Order> findTopByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT o.user.id, o.user.username, COUNT(o), COALESCE(SUM(o.totalPrice), 0) " +
            "FROM Order o WHERE o.status != 'CANCELLED' " +
            "GROUP BY o.user.id, o.user.username " +
            "ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> getTopUsersBySpending(Pageable pageable);

    @Query("SELECT o.deliveryMethod, COUNT(o) FROM Order o GROUP BY o.deliveryMethod ORDER BY COUNT(o) DESC")
    List<Object[]> getPopularDeliveryMethods();

    @Query("SELECT o.paymentMethod, COUNT(o) FROM Order o GROUP BY o.paymentMethod ORDER BY COUNT(o) DESC")
    List<Object[]> getPopularPaymentMethods();

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") OrderStatus status);

    default int markAsPaid(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.PAID);
    }

    default int markAsShipped(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.SHIPPED);
    }

    default int markAsDelivered(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    default int cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id IN :orderIds")
    int bulkUpdateStatus(@Param("orderIds") List<Long> orderIds, @Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status = 'PAID' AND o.createdAt < :date")
    List<Order> findPaidNotShippedOrders(@Param("date") LocalDateTime date);

    @Query("SELECT o FROM Order o WHERE o.status = 'SHIPPED' AND o.createdAt < :date")
    List<Order> findShippedNotDeliveredOrders(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.status = 'CANCELLED' AND o.createdAt < :date")
    int deleteOldCancelledOrders(@Param("date") LocalDateTime date);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.product.id = :productId")
    List<Order> findOrdersContainingProduct(@Param("productId") Long productId);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i " +
            "WHERE i.product.id = :productId AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersContainingProductBetween(@Param("productId") Long productId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('DATE', o.createdAt), " +
            "COUNT(o), " +
            "COALESCE(SUM(o.totalPrice), 0), " +
            "COALESCE(SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END), 0) " +
            "FROM Order o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', o.createdAt) " +
            "ORDER BY FUNCTION('DATE', o.createdAt)")
    List<Object[]> getSalesReport(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o.status, COUNT(o), COALESCE(SUM(o.totalPrice), 0) " +
            "FROM Order o WHERE o.user.id = :userId " +
            "GROUP BY o.status")
    List<Object[]> getUserOrderReport(@Param("userId") Long userId);

    boolean existsByUser(User user);
    boolean existsByUserAndStatus(User user, OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE CAST(o.createdAt AS date) = CURRENT_DATE")
    long getTodayOrderCount();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE CAST(o.createdAt AS date) = CURRENT_DATE AND o.status != 'CANCELLED'")
    BigDecimal getTodayRevenue();

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE LOWER(o.comment) LIKE LOWER(CONCAT('%', :comment, '%'))")
    List<Order> searchByComment(@Param("comment") String comment);
}