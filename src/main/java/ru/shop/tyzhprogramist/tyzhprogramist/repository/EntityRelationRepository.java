package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RelationType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface EntityRelationRepository extends JpaRepository<EntityRelation, Long> {

    List<EntityRelation> findByRelationType(RelationType relationType);
    Page<EntityRelation> findByRelationType(RelationType relationType, Pageable pageable);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :contentType " +
            "AND er.fromObjectId = :objectId")
    List<EntityRelation> findCompatibilityRulesFrom(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.toContentType = :contentType " +
            "AND er.toObjectId = :objectId")
    List<EntityRelation> findCompatibilityRulesTo(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND ((er.fromContentType = :contentType AND er.fromObjectId = :objectId) " +
            "OR (er.toContentType = :contentType AND er.toObjectId = :objectId))")
    List<EntityRelation> findAllCompatibilityRules(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT er.isCompatible FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :fromType AND er.fromObjectId = :fromId " +
            "AND er.toContentType = :toType AND er.toObjectId = :toId")
    Optional<Boolean> checkCompatibility(
            @Param("fromType") String fromType,
            @Param("fromId") Long fromId,
            @Param("toType") String toType,
            @Param("toId") Long toId);

    @Query("SELECT er.toContentType, er.toObjectId FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :contentType " +
            "AND er.fromObjectId = :objectId " +
            "AND er.isCompatible = true")
    List<Object[]> findCompatibleComponents(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT er.toContentType, er.toObjectId FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :contentType " +
            "AND er.fromObjectId = :objectId " +
            "AND er.isCompatible = false")
    List<Object[]> findIncompatibleComponents(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT COUNT(er) > 0 FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :fromType AND er.fromObjectId = :fromId " +
            "AND er.toContentType = :toType AND er.toObjectId = :toId")
    boolean existsCompatibilityRule(
            @Param("fromType") String fromType,
            @Param("fromId") Long fromId,
            @Param("toType") String toType,
            @Param("toId") Long toId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :fromType AND er.fromObjectId = :fromId " +
            "AND er.toContentType = :toType AND er.toObjectId = :toId")
    int deleteCompatibilityRule(
            @Param("fromType") String fromType,
            @Param("fromId") Long fromId,
            @Param("toType") String toType,
            @Param("toId") Long toId);

    List<EntityRelation> findByUserAndRelationType(User user, RelationType relationType);
    Page<EntityRelation> findByUserAndRelationType(User user, RelationType relationType, Pageable pageable);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.user.id = :userId AND er.relationType = 'COMPARISON'")
    List<EntityRelation> findUserComparisons(@Param("userId") Long userId);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.user.id = :userId AND er.relationType = 'COMPARISON'")
    Page<EntityRelation> findUserComparisons(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.user.id = :userId AND er.name = :name AND er.relationType = 'COMPARISON'")
    List<EntityRelation> findComparisonByName(
            @Param("userId") Long userId,
            @Param("name") String name);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' " +
            "AND er.name = :comparisonName " +
            "AND er.user.id = :userId")
    List<EntityRelation> findComparisonItems(
            @Param("userId") Long userId,
            @Param("comparisonName") String comparisonName);

    @Query("SELECT DISTINCT er.name FROM EntityRelation er " +
            "WHERE er.user.id = :userId AND er.relationType = 'COMPARISON'")
    List<String> findUserComparisonNames(@Param("userId") Long userId);

    @Query("SELECT COUNT(er) > 0 FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' " +
            "AND er.user.id = :userId " +
            "AND er.name = :comparisonName " +
            "AND er.fromContentType = :contentType " +
            "AND er.fromObjectId = :objectId")
    boolean existsInComparison(
            @Param("userId") Long userId,
            @Param("comparisonName") String comparisonName,
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' " +
            "AND er.user.id = :userId " +
            "AND er.name = :comparisonName " +
            "AND er.fromContentType = :contentType " +
            "AND er.fromObjectId = :objectId")
    int removeFromComparison(
            @Param("userId") Long userId,
            @Param("comparisonName") String comparisonName,
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' " +
            "AND er.user.id = :userId " +
            "AND er.name = :comparisonName")
    int deleteComparison(
            @Param("userId") Long userId,
            @Param("comparisonName") String comparisonName);

    @Query("SELECT COUNT(er) FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' " +
            "AND er.user.id = :userId " +
            "AND er.name = :comparisonName")
    int countComparisonItems(
            @Param("userId") Long userId,
            @Param("comparisonName") String comparisonName);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.fromContentType = :contentType AND er.fromObjectId = :objectId")
    List<EntityRelation> findByFromEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.toContentType = :contentType AND er.toObjectId = :objectId")
    List<EntityRelation> findByToEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE (er.fromContentType = :contentType AND er.fromObjectId = :objectId) " +
            "OR (er.toContentType = :contentType AND er.toObjectId = :objectId)")
    List<EntityRelation> findAllByEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    List<EntityRelation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT er FROM EntityRelation er " +
            "WHERE er.user.id = :userId " +
            "ORDER BY er.createdAt DESC")
    List<EntityRelation> findRecentUserRelations(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er WHERE er.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er " +
            "WHERE (er.fromContentType = :contentType AND er.fromObjectId = :objectId) " +
            "OR (er.toContentType = :contentType AND er.toObjectId = :objectId)")
    int deleteAllByEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND ((er.fromContentType = :contentType AND er.fromObjectId = :objectId) " +
            "OR (er.toContentType = :contentType AND er.toObjectId = :objectId))")
    int deleteAllCompatibilityRules(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' " +
            "AND er.createdAt < :date")
    int deleteOldComparisons(@Param("date") LocalDateTime date);

    @Query("SELECT er.relationType, COUNT(er) FROM EntityRelation er GROUP BY er.relationType")
    List<Object[]> getRelationTypeStatistics();

    @Query("SELECT er.fromContentType, COUNT(er) FROM EntityRelation er GROUP BY er.fromContentType")
    List<Object[]> getFromEntityStatistics();

    @Query("SELECT er.fromObjectId, COUNT(er) as compareCount " +
            "FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPARISON' AND er.fromContentType = 'Product' " +
            "GROUP BY er.fromObjectId " +
            "ORDER BY compareCount DESC")
    List<Object[]> getMostComparedProducts(Pageable pageable);

    @Query("SELECT " +
            "SUM(CASE WHEN er.isCompatible = true THEN 1 ELSE 0 END) as compatible, " +
            "SUM(CASE WHEN er.isCompatible = false THEN 1 ELSE 0 END) as incompatible " +
            "FROM EntityRelation er " +
            "WHERE er.relationType = 'COMPATIBILITY' " +
            "AND er.fromContentType = :contentType AND er.fromObjectId = :objectId")
    Object[] getCompatibilityStatistics(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    boolean existsByRelationTypeAndFromContentTypeAndFromObjectIdAndToContentTypeAndToObjectId(
            RelationType relationType,
            String fromContentType, Long fromObjectId,
            String toContentType, Long toObjectId);

    default List<EntityRelation> findProductCompatibilityRules(Long productId) {
        return findAllCompatibilityRules("Product", productId);
    }

    default List<EntityRelation> findComponentTypeCompatibilityRules(Long componentTypeId) {
        return findAllCompatibilityRules("ComponentType", componentTypeId);
    }

    default Optional<Boolean> areProductsCompatible(Long productId1, Long productId2) {
        return checkCompatibility("Product", productId1, "Product", productId2);
    }

    default Optional<Boolean> isProductCompatibleWithComponentType(Long productId, Long componentTypeId) {
        return checkCompatibility("Product", productId, "ComponentType", componentTypeId);
    }

    default List<EntityRelation> findUserProductComparisons(Long userId) {
        return findUserComparisons(userId).stream()
                .filter(er -> "Product".equals(er.getFromContentType()))
                .collect(Collectors.toList());
    }

    default List<EntityRelation> findUserPcBuildComparisons(Long userId) {
        return findUserComparisons(userId).stream()
                .filter(er -> "PcBuild".equals(er.getFromContentType()))
                .collect(Collectors.toList());
    }
}