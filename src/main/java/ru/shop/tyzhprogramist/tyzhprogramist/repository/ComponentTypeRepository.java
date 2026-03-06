package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {

    Optional<ComponentType> findByName(String name);

    boolean existsByName(String name);

    List<ComponentType> findAllByOrderByOrderStepAsc();
    Optional<ComponentType> findByOrderStep(Integer orderStep);
    List<ComponentType> findByOrderStepLessThanEqual(Integer orderStep);
    List<ComponentType> findByOrderStepGreaterThan(Integer orderStep);

    @Query("SELECT ct FROM ComponentType ct WHERE ct.orderStep > :currentOrder ORDER BY ct.orderStep ASC")
    List<ComponentType> findNextTypes(@Param("currentOrder") Integer currentOrder, Pageable pageable);


    @Query("SELECT COALESCE(MAX(ct.orderStep), 0) FROM ComponentType ct")
    Integer getMaxOrderStep();

    @Query("SELECT COALESCE(MIN(ct.orderStep), 0) FROM ComponentType ct")
    Integer getMinOrderStep();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.orderStep = (SELECT MIN(ct2.orderStep) FROM ComponentType ct2)")
    Optional<ComponentType> findFirstStep();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.orderStep = (SELECT MAX(ct2.orderStep) FROM ComponentType ct2)")
    Optional<ComponentType> findLastStep();

    @Query("SELECT ct FROM ComponentType ct WHERE ct.orderStep > :currentOrder ORDER BY ct.orderStep ASC")
    List<ComponentType> findNextStep(@Param("currentOrder") Integer currentOrder, Pageable pageable);

    @Query("SELECT ct FROM ComponentType ct WHERE ct.orderStep < :currentOrder ORDER BY ct.orderStep DESC")
    List<ComponentType> findPreviousStep(@Param("currentOrder") Integer currentOrder, DataWebProperties.Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ComponentType ct SET ct.orderStep = :newOrder WHERE ct.id = :id")
    int updateOrderStep(@Param("id") Long id, @Param("newOrder") Integer newOrder);

    @Modifying
    @Transactional
    @Query("UPDATE ComponentType ct SET ct.orderStep = ct.orderStep + :delta WHERE ct.orderStep >= :startFrom")
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

    @Query("SELECT ct.name, COUNT(pbi) FROM ComponentType ct " +
            "LEFT JOIN ProductItem pbi ON pbi.parentType = 'PcBuild' " +
            "LEFT JOIN Product p ON pbi.product.id = p.id " +
            "LEFT JOIN ComponentType ct2 ON ct2.id = :componentTypeId " + // Здесь нужна связь Product с ComponentType
            "GROUP BY ct.id, ct.name " +
            "ORDER BY COUNT(pbi) DESC")
    List<Object[]> getComponentUsageStatistics();

    @Query("SELECT COUNT(ct) FROM ComponentType ct")
    long getTotalSteps();

    @Query("SELECT MIN(ct.orderStep), MAX(ct.orderStep) FROM ComponentType ct")
    Object[] getOrderStepRange();

    @Query("SELECT CASE WHEN ct.name IN ('CPU', 'MOTHERBOARD', 'RAM') THEN true ELSE false END " +
            "FROM ComponentType ct WHERE ct.id = :id")
    boolean isRequired(@Param("id") Long id);

    default List<ComponentType> findProcessorTypes() {
        return findAllByOrderByOrderStepAsc().stream()
                .filter(ct -> ct.getName().toLowerCase().contains("процессор") ||
                        ct.getName().toLowerCase().contains("cpu"))
                .collect(Collectors.toList());
    }

    default List<ComponentType> findMemoryTypes() {
        return findAllByOrderByOrderStepAsc().stream()
                .filter(ct -> ct.getName().toLowerCase().contains("озу") ||
                        ct.getName().toLowerCase().contains("ram") ||
                        ct.getName().toLowerCase().contains("память"))
                .collect(Collectors.toList());
    }

    default List<ComponentType> findStorageTypes() {
        return findAllByOrderByOrderStepAsc().stream()
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