package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);
    Optional<Product> findBySku(String sku);

    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.quantity > 0")
    Page<Product> findAllAvailable(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isNew = true AND p.isActive = true")
    List<Product> findNewProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isBestseller = true AND p.isActive = true")
    List<Product> findBestsellers(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.slug = :categorySlug AND p.isActive = true")
    Page<Product> findByCategorySlug(@Param("categorySlug") String categorySlug, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.price ASC")
    Page<Product> findAllSortedByPriceAsc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.price DESC")
    Page<Product> findAllSortedByPriceDesc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.rating DESC")
    Page<Product> findAllSortedByRating(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.createdAt DESC")
    Page<Product> findAllNewest(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    @Query("SELECT p FROM Product p WHERE p.quantity = 0 AND p.isActive = true")
    List<Product> findOutOfStockProducts();

    @Query(value = "SELECT p.* FROM products p " +
            "WHERE p.category_id IN (" +
            "    WITH RECURSIVE category_tree AS (" +
            "        SELECT id FROM categories WHERE id = :categoryId " +
            "        UNION ALL " +
            "        SELECT c.id FROM categories c " +
            "        INNER JOIN category_tree ct ON c.parent_id = ct.id" +
            "    ) SELECT id FROM category_tree" +
            ") ORDER BY p.created_at DESC",
            nativeQuery = true)
    List<Product> findByCategoryWithDescendants(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT p.* FROM products p " +
            "WHERE p.category_id IN (" +
            "    WITH RECURSIVE category_tree AS (" +
            "        SELECT id FROM categories WHERE id = :categoryId " +
            "        UNION ALL " +
            "        SELECT c.id FROM categories c " +
            "        INNER JOIN category_tree ct ON c.parent_id = ct.id" +
            "    ) SELECT id FROM category_tree" +
            ") ORDER BY p.created_at DESC",
            countQuery = "SELECT COUNT(*) FROM products p " +
                    "WHERE p.category_id IN (" +
                    "    WITH RECURSIVE category_tree AS (" +
                    "        SELECT id FROM categories WHERE id = :categoryId " +
                    "        UNION ALL " +
                    "        SELECT c.id FROM categories c " +
                    "        INNER JOIN category_tree ct ON c.parent_id = ct.id" +
                    "    ) SELECT id FROM category_tree" +
                    ")",
            nativeQuery = true)
    Page<Product> findByCategoryWithDescendants(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.relatedProducts r WHERE r.id = :productId")
    List<Product> findRelatedProducts(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p JOIN p.frequentlyBoughtWith f WHERE f.id = :productId")
    List<Product> findFrequentlyBoughtTogether(@Param("productId") Long productId);

    @Query(value = "SELECT p.* FROM products p " +
            "JOIN frequently_bought_together fbt ON p.id = fbt.related_product_id " +
            "WHERE fbt.product_id = :productId",
            nativeQuery = true)
    List<Product> findFrequentlyBoughtTogetherNative(@Param("productId") Long productId);

    @Query("SELECT AVG(p.rating) FROM Product p WHERE p.category.id = :categoryId")
    Double getAverageRatingByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT p.category.name, COUNT(p), AVG(p.price) FROM Product p GROUP BY p.category.name")
    List<Object[]> getProductStatisticsByCategory();

    @Query("SELECT p FROM Product p WHERE p.price > (SELECT AVG(p2.price) FROM Product p2)")
    List<Product> findProductsAboveAveragePrice();

    @Query("SELECT p FROM Product p WHERE p.viewsCount > 0 ORDER BY p.viewsCount DESC")
    List<Product> findMostViewed(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.purchaseCount > 0 ORDER BY p.purchaseCount DESC")
    List<Product> findMostPurchased(Pageable pageable);
}