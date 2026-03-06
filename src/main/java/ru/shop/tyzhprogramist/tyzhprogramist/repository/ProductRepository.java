package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);
    Optional<Product> findBySlug(String slug);
    Optional<Product> findByName(String name);

    boolean existsBySku(String sku);
    boolean existsBySlug(String slug);

    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByCategoryAndIsActiveTrue(Category category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN (" +
            "WITH RECURSIVE category_tree AS (" +
            "    SELECT id FROM categories WHERE id = :categoryId " +
            "    UNION ALL " +
            "    SELECT c.id FROM categories c " +
            "    INNER JOIN category_tree ct ON c.parent_id = ct.id" +
            ") SELECT id FROM category_tree)")
    Page<Product> findByCategoryWithDescendants(@Param("categoryId") Long categoryId, Pageable pageable);

    long countByCategory(Category category);
    long countByCategoryAndIsActiveTrue(Category category);

    Page<Product> findByIsActiveTrue(Pageable pageable);
    List<Product> findByIsActiveFalse();
    Page<Product> findByIsNewTrue(Pageable pageable);
    Page<Product> findByIsBestsellerTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.oldPrice IS NOT NULL AND p.oldPrice > p.price")
    Page<Product> findDiscountedProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    @Query("SELECT p FROM Product p WHERE p.quantity = 0")
    List<Product> findOutOfStockProducts();
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByPriceLessThanEqual(BigDecimal price, Pageable pageable);
    Page<Product> findByPriceGreaterThanEqual(BigDecimal price, Pageable pageable);
    Page<Product> findByRatingGreaterThanEqual(Double minRating, Pageable pageable);
    Page<Product> findByRatingBetween(Double minRating, Double maxRating, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE SIZE(p.feedbacks) >= :minReviews ORDER BY p.rating DESC")
    List<Product> findTopRated(@Param("minReviews") int minReviews, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.createdAt >= :since")
    Page<Product> findRecentlyAdded(@Param("since") LocalDateTime since, Pageable pageable);
    List<Product> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<Product> findByOrderByViewsCountDesc(Pageable pageable);
    Page<Product> findByOrderByPurchaseCountDesc(Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.purchaseCount DESC")
    List<Product> findPopularInCategory(@Param("category") Category category, Pageable pageable);

    @Query("SELECT p.relatedProducts FROM Product p WHERE p.id = :productId")
    Set<Product> findRelatedProducts(@Param("productId") Long productId);

    @Query("SELECT p.frequentlyBoughtWith FROM Product p WHERE p.id = :productId")
    Set<Product> findFrequentlyBoughtWith(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p WHERE p.category.id IN (" +
            "    SELECT p2.category.id FROM Product p2 WHERE p2.id IN :productIds" +
            ") AND p.id NOT IN :productIds ORDER BY p.purchaseCount DESC")
    List<Product> findRecommendations(@Param("productIds") List<Long> productIds, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.fullDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    List<Product> findByWarrantyMonthsGreaterThanEqual(Integer warrantyMonths);
    Page<Product> findByWeightLessThanEqual(BigDecimal maxWeight, Pageable pageable);

    @Query("SELECT MIN(p.price), MAX(p.price) FROM Product p WHERE p.category.id = :categoryId")
    Object[] getPriceRangeByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT AVG(p.price) FROM Product p WHERE p.category.id = :categoryId")
    BigDecimal getAveragePriceByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT " +
            "COUNT(p) as totalProducts, " +
            "SUM(CASE WHEN p.isActive = true THEN 1 ELSE 0 END) as activeProducts, " +
            "SUM(CASE WHEN p.quantity = 0 THEN 1 ELSE 0 END) as outOfStock, " +
            "SUM(CASE WHEN p.oldPrice IS NOT NULL AND p.oldPrice > p.price THEN 1 ELSE 0 END) as discounted, " +
            "AVG(p.rating) as averageRating " +
            "FROM Product p")
    Object[] getProductStatistics();

    @Query("SELECT DATE(o.createdAt), SUM(pi.quantity), SUM(pi.price * pi.quantity) " +
            "FROM Order o " +
            "JOIN ProductItem pi ON pi.parentType = 'Order' AND pi.parentId = o.id " +
            "WHERE pi.product.id = :productId " +
            "AND o.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.createdAt)")
    List<Object[]> getSalesStatistics(@Param("productId") Long productId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = :newPrice WHERE p.id = :productId")
    int updatePrice(@Param("productId") Long productId, @Param("newPrice") BigDecimal newPrice);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :productId AND p.quantity >= :quantity")
    int decreaseQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity + :quantity WHERE p.id = :productId")
    int increaseQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.rating = (" +
            "    SELECT AVG(pf.rating) FROM ProductFeedback pf " +
            "    WHERE pf.product.id = :productId AND pf.rating IS NOT NULL" +
            ") WHERE p.id = :productId")
    int updateRating(@Param("productId") Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isNew = false WHERE p.createdAt < :date")
    int resetNewStatus(@Param("date") LocalDateTime date);

    List<Product> findByIdIn(List<Long> ids);
    List<Product> findBySkuIn(List<String> skus);

    @Query("SELECT DISTINCT pf.product.id FROM ProductFeedback pf " +
            "WHERE pf.createdAt > :since")
    List<Long> findProductsWithNewFeedback(@Param("since") LocalDateTime since);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.isActive = false AND p.purchaseCount = 0 AND p.createdAt < :date")
    int deleteOldInactiveProducts(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isActive = false WHERE p.quantity = 0 AND p.isActive = true")
    int deactivateOutOfStockProducts();
}