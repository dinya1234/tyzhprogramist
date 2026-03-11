package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {

    Optional<ComponentType> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT ct FROM ComponentType ct ORDER BY ct.order_step ASC")
    List<ComponentType> findAllOrdered();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step = :orderStep")
    Optional<ComponentType> findByOrderStep(@Param("orderStep") Integer orderStep);

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step <= :orderStep")
    List<ComponentType> findByOrderStepLessThanEqual(@Param("orderStep") Integer orderStep);

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step > :orderStep")
    List<ComponentType> findByOrderStepGreaterThan(@Param("orderStep") Integer orderStep);

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step > :currentOrder ORDER BY ct.order_step ASC")
    List<ComponentType> findNextTypes(@Param("currentOrder") Integer currentOrder);

    @Query("SELECT COALESCE(MAX(ct.order_step), 0) FROM ComponentType ct")
    Integer getMaxOrderStep();

    @Query("SELECT COALESCE(MIN(ct.order_step), 0) FROM ComponentType ct")
    Integer getMinOrderStep();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step = (SELECT MIN(ct2.order_step) FROM ComponentType ct2)")
    Optional<ComponentType> findFirstStep();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step = (SELECT MAX(ct2.order_step) FROM ComponentType ct2)")
    Optional<ComponentType> findLastStep();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step > :currentOrder ORDER BY ct.order_step ASC")
    List<ComponentType> findNextStep(@Param("currentOrder") Integer currentOrder, Pageable pageable);

    @Query("SELECT ct FROM ComponentType ct WHERE ct.order_step < :currentOrder ORDER BY ct.order_step DESC")
    List<ComponentType> findPreviousStep(@Param("currentOrder") Integer currentOrder, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ComponentType ct SET ct.order_step = :newOrder WHERE ct.id = :id")
    int updateOrderStep(@Param("id") Long id, @Param("newOrder") Integer newOrder);

    @Modifying
    @Transactional
    @Query("UPDATE ComponentType ct SET ct.order_step = ct.order_step + :delta WHERE ct.order_step >= :startFrom")
    int shiftOrderSteps(@Param("startFrom") Integer startFrom, @Param("delta") Integer delta);

    @Modifying
    @Transactional
    @Query(value = "WITH ordered AS (" +
            "    SELECT id, ROW_NUMBER() OVER (ORDER BY order_step) AS new_order " +
            "    FROM component_types" +
            ") " +
            "UPDATE component_types ct " +
            "SET order_step = ordered.new_order - 1 " +
            "FROM ordered " +
            "WHERE ct.id = ordered.id", nativeQuery = true)
    int reorderSequential();

    @Query("SELECT CASE WHEN COUNT(ct) > 0 THEN true ELSE false END FROM ComponentType ct " +
            "WHERE ct.id = :id AND ct.name LIKE '%multiple%'")
    boolean isMultipleAllowed(@Param("id") Long id);

    @Query("SELECT ct.name, COUNT(p) FROM ComponentType ct " +
            "LEFT JOIN Product p ON p.category.id IN " +
            "(SELECT c.id FROM Category c WHERE c.name LIKE CONCAT('%', ct.name, '%')) " +
            "GROUP BY ct.id, ct.name " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> getComponentUsageStatistics();

    @Query("SELECT COUNT(ct) FROM ComponentType ct")
    long getTotalSteps();

    @Query("SELECT MIN(ct.order_step), MAX(ct.order_step) FROM ComponentType ct")
    Object[] getOrderStepRange();

    @Query("SELECT CASE WHEN ct.name IN ('Процессор', 'Материнская плата', 'Оперативная память') " +
            "THEN true ELSE false END FROM ComponentType ct WHERE ct.id = :id")
    boolean isRequired(@Param("id") Long id);

    default List<ComponentType> findProcessorTypes() {
        return findAllOrdered().stream()
                .filter(ct -> ct.getName().toLowerCase().contains("процессор") ||
                        ct.getName().toLowerCase().contains("cpu"))
                .collect(Collectors.toList());
    }

    default List<ComponentType> findMemoryTypes() {
        return findAllOrdered().stream()
                .filter(ct -> ct.getName().toLowerCase().contains("озу") ||
                        ct.getName().toLowerCase().contains("ram") ||
                        ct.getName().toLowerCase().contains("память"))
                .collect(Collectors.toList());
    }

    default List<ComponentType> findStorageTypes() {
        return findAllOrdered().stream()
                .filter(ct -> ct.getName().toLowerCase().contains("ssd") ||
                        ct.getName().toLowerCase().contains("hdd") ||
                        ct.getName().toLowerCase().contains("накопитель"))
                .collect(Collectors.toList());
    }

    @Query("SELECT DISTINCT ct FROM ComponentType ct " +
            "JOIN EntityRelation er ON (er.fromContentType = 'ComponentType' AND er.fromObjectId = ct.id) " +
            "WHERE er.relationType = 'compatibility' " +
            "AND er.toContentType = 'ComponentType' " +
            "AND er.toObjectId = :componentTypeId " +
            "AND er.isCompatible = true")
    List<ComponentType> findCompatibleTypes(@Param("componentTypeId") Long componentTypeId);

    @Query("SELECT DISTINCT ct FROM ComponentType ct " +
            "JOIN EntityRelation er ON (er.fromContentType = 'ComponentType' AND er.fromObjectId = ct.id) " +
            "WHERE er.relationType = 'compatibility' " +
            "AND er.toContentType = 'ComponentType' " +
            "AND er.toObjectId = :componentTypeId " +
            "AND er.isCompatible = false")
    List<ComponentType> findIncompatibleTypes(@Param("componentTypeId") Long componentTypeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EntityRelation er WHERE " +
            "(er.fromContentType = 'ComponentType' AND er.fromObjectId = :componentTypeId) OR " +
            "(er.toContentType = 'ComponentType' AND er.toObjectId = :componentTypeId)")
    int deleteCompatibilityRules(@Param("componentTypeId") Long componentTypeId);
}