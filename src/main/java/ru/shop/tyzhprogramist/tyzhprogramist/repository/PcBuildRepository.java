package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.PcBuild;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PcBuildRepository extends JpaRepository<PcBuild, Long> {

    List<PcBuild> findByUser(User user);
    Page<PcBuild> findByUser(User user, Pageable pageable);
    Page<PcBuild> findByUserId(Long userId, Pageable pageable);
    Page<PcBuild> findByIsPublicTrue(Pageable pageable);
    List<PcBuild> findByUserAndIsPublicFalse(User user);

    @Query("SELECT p FROM PcBuild p WHERE p.user.id = :userId AND p.name = :name")
    List<PcBuild> findByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true ORDER BY p.createdAt DESC")
    List<PcBuild> findRecentPublicBuilds(Pageable pageable);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true ORDER BY p.viewsCount DESC")
    Page<PcBuild> findMostViewedPublicBuilds(Pageable pageable);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true AND p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<PcBuild> findRecentPublicBuildsSince(@Param("since") LocalDateTime since);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true ORDER BY p.createdAt DESC")
    Page<PcBuild> findPublicBuilds(Pageable pageable);
    List<PcBuild> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM PcBuild p WHERE p.user.id = :userId AND p.createdAt BETWEEN :startDate AND :endDate")
    List<PcBuild> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE PcBuild p SET p.viewsCount = p.viewsCount + 1 WHERE p.id = :buildId")
    int incrementViews(@Param("buildId") Long buildId);

    @Query("SELECT p.viewsCount FROM PcBuild p WHERE p.id = :buildId")
    Integer getViewsCount(@Param("buildId") Long buildId);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true ORDER BY p.viewsCount DESC")
    List<PcBuild> findTopByViews(Pageable pageable);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true AND LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PcBuild> searchPublicBuildsByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT p FROM PcBuild p WHERE p.user.id = :userId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PcBuild> searchUserBuildsByName(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

    long countByUser(User user);
    long countByIsPublicTrue();
    long countByUserAndIsPublicTrue(User user);

    @Query("SELECT COUNT(p) FROM PcBuild p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT " +
            "COUNT(p) as totalBuilds, " +
            "SUM(CASE WHEN p.isPublic = true THEN 1 ELSE 0 END) as publicBuilds, " +
            "SUM(p.viewsCount) as totalViews " +
            "FROM PcBuild p WHERE p.user.id = :userId")
    Object[] getUserBuildStatistics(@Param("userId") Long userId);

    @Query("SELECT " +
            "COUNT(p) as totalBuilds, " +
            "SUM(CASE WHEN p.isPublic = true THEN 1 ELSE 0 END) as publicBuilds, " +
            "SUM(p.viewsCount) as totalViews, " +
            "AVG(p.viewsCount) as avgViews " +
            "FROM PcBuild p")
    Object[] getOverallStatistics();

    @Query("SELECT DATE(p.createdAt), COUNT(p), SUM(CASE WHEN p.isPublic = true THEN 1 ELSE 0 END) " +
            "FROM PcBuild p WHERE p.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(p.createdAt) ORDER BY DATE(p.createdAt)")
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.user.id, p.user.username, COUNT(p) as buildCount " +
            "FROM PcBuild p " +
            "GROUP BY p.user.id, p.user.username " +
            "ORDER BY buildCount DESC")
    List<Object[]> getTopUsersByBuilds(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE PcBuild p SET p.isPublic = true WHERE p.id = :buildId")
    int makePublic(@Param("buildId") Long buildId);

    @Modifying
    @Transactional
    @Query("UPDATE PcBuild p SET p.isPublic = false WHERE p.id = :buildId")
    int makePrivate(@Param("buildId") Long buildId);

    @Modifying
    @Transactional
    @Query("UPDATE PcBuild p SET p.isPublic = false WHERE p.user.id = :userId")
    int hideAllUserBuilds(@Param("userId") Long userId);

    @Query("SELECT DISTINCT p FROM PcBuild p " +
            "JOIN ProductItem pi ON pi.parentType = 'PC_BUILD' AND pi.parentId = p.id " +
            "WHERE pi.product.id = :productId")
    List<PcBuild> findBuildsContainingComponent(@Param("productId") Long productId);

    @Query("SELECT DISTINCT p FROM PcBuild p " +
            "JOIN ProductItem pi ON pi.parentType = 'PC_BUILD' AND pi.parentId = p.id " +
            "WHERE p.isPublic = true AND pi.product.id = :productId")
    List<PcBuild> findPublicBuildsContainingComponent(@Param("productId") Long productId);

    @Query("SELECT DISTINCT p FROM PcBuild p " +
            "JOIN ProductItem pi ON pi.parentType = 'PC_BUILD' AND pi.parentId = p.id " +
            "JOIN pi.product prod " +
            "WHERE LOWER(prod.name) LIKE LOWER(CONCAT('%', :cpuName, '%'))")
    List<PcBuild> findBuildsWithCPU(@Param("cpuName") String cpuName);

    @Query(value = "INSERT INTO pc_builds (user_id, name, is_public, views_count, created_at) " +
            "SELECT :newUserId, CONCAT(name, ' (копия)'), false, 0, NOW() " +
            "FROM pc_builds WHERE id = :sourceBuildId", nativeQuery = true)
    @Modifying
    @Transactional
    int cloneBuild(@Param("sourceBuildId") Long sourceBuildId, @Param("newUserId") Long newUserId);

    @Query("SELECT p, COUNT(pi) as commonComponents FROM PcBuild p " +
            "JOIN ProductItem pi ON pi.parentType = 'PC_BUILD' AND pi.parentId = p.id " +
            "WHERE p.id != :buildId AND p.isPublic = true " +
            "AND pi.product.id IN (SELECT pi2.product.id FROM ProductItem pi2 " +
            "                     WHERE pi2.parentType = 'PC_BUILD' AND pi2.parentId = :buildId) " +
            "GROUP BY p.id " +
            "ORDER BY commonComponents DESC")
    List<Object[]> findSimilarBuilds(@Param("buildId") Long buildId, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM PcBuild p WHERE p.isPublic = false AND p.createdAt < :date")
    int deleteOldPrivateBuilds(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query("DELETE FROM PcBuild p WHERE p.viewsCount = 0 AND p.createdAt < :date")
    int deleteUnpopularBuilds(@Param("date") LocalDateTime date);

    Page<PcBuild> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<PcBuild> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<PcBuild> findByIsPublicTrueOrderByViewsCountDesc(Pageable pageable);
    boolean existsByUserIdAndName(Long userId, String name);

    @Query("SELECT p.isPublic FROM PcBuild p WHERE p.id = :buildId")
    boolean isBuildPublic(@Param("buildId") Long buildId);

    @Query("SELECT p, SUM(pi.price * pi.quantity) as totalPrice FROM PcBuild p " +
            "JOIN ProductItem pi ON pi.parentType = 'PC_BUILD' AND pi.parentId = p.id " +
            "WHERE p.isPublic = true " +
            "GROUP BY p.id " +
            "HAVING SUM(pi.price * pi.quantity) BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY totalPrice")
    List<Object[]> findBuildsByPriceRange(@Param("minPrice") Double minPrice,
                                          @Param("maxPrice") Double maxPrice,
                                          Pageable pageable);

    @Query("SELECT p FROM PcBuild p WHERE p.isPublic = true ORDER BY p.createdAt DESC")
    List<PcBuild> findRecentlyUpdated(Pageable pageable);
}