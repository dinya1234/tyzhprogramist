package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);
    Optional<Category> findByName(String name);

    boolean existsBySlug(String slug);
    boolean existsByName(String name);

    List<Category> findByParentIsNullOrderByOrderAsc();
    List<Category> findByParentOrderByOrderAsc(Category parent);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId ORDER BY c.order ASC")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL ORDER BY c.order ASC")
    List<Category> findAllRootCategoriesWithChildren();

    @Query(value = "WITH RECURSIVE category_path AS (" +
            "    SELECT id, name, slug, parent_id, 1 as level " +
            "    FROM categories WHERE id = :categoryId " +
            "    UNION ALL " +
            "    SELECT c.id, c.name, c.slug, c.parent_id, cp.level + 1 " +
            "    FROM categories c " +
            "    INNER JOIN category_path cp ON c.id = cp.parent_id" +
            ") " +
            "SELECT * FROM category_path ORDER BY level DESC", nativeQuery = true)
    List<Object[]> findCategoryPath(@Param("categoryId") Long categoryId);

    @Query(value = "WITH RECURSIVE category_tree AS (" +
            "    SELECT id, name, parent_id " +
            "    FROM categories WHERE id = :categoryId " +
            "    UNION ALL " +
            "    SELECT c.id, c.name, c.parent_id " +
            "    FROM categories c " +
            "    INNER JOIN category_tree ct ON c.parent_id = ct.id" +
            ") " +
            "SELECT * FROM category_tree WHERE id != :categoryId", nativeQuery = true)
    List<Category> findAllDescendants(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT c FROM Category c " +
            "JOIN c.products p " +
            "WHERE p.quantity > 0 AND p.isActive = true " +
            "ORDER BY c.order ASC")
    List<Category> findCategoriesWithAvailableProducts();

    @Query("SELECT c FROM Category c WHERE c.children IS NOT EMPTY ORDER BY c.order ASC")
    List<Category> findParentCategories();

    @Query("SELECT c FROM Category c WHERE c.children IS EMPTY ORDER BY c.order ASC")
    List<Category> findLeafCategories();

    List<Category> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    @Query("SELECT c, COUNT(p) as productCount FROM Category c " +
            "LEFT JOIN c.products p " +
            "WHERE p.isActive = true OR p IS NULL " +
            "GROUP BY c " +
            "ORDER BY c.order ASC")
    List<Object[]> findAllCategoriesWithProductCount();

    @Query("SELECT c, COUNT(p) as productCount FROM Category c " +
            "LEFT JOIN c.products p ON p.isActive = true " +
            "GROUP BY c " +
            "ORDER BY c.order ASC")
    List<Object[]> findAllCategoriesWithActiveProductCount();

    @Query("SELECT DISTINCT c FROM Category c " +
            "JOIN c.products p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> findCategoriesByProductName(@Param("searchTerm") String searchTerm);

    @Query(value = "WITH RECURSIVE category_breadcrumbs AS (" +
            "    SELECT id, name, slug, parent_id, 0 as level " +
            "    FROM categories WHERE id = :categoryId " +
            "    UNION ALL " +
            "    SELECT c.id, c.name, c.slug, c.parent_id, cb.level + 1 " +
            "    FROM categories c " +
            "    INNER JOIN category_breadcrumbs cb ON c.id = cb.parent_id" +
            ") " +
            "SELECT id, name, slug FROM category_breadcrumbs ORDER BY level DESC", nativeQuery = true)
    List<Object[]> findBreadcrumbs(@Param("categoryId") Long categoryId);

    @Query("SELECT COALESCE(MAX(c.order), -1) FROM Category c " +
            "WHERE (c.parent.id = :parentId OR (:parentId IS NULL AND c.parent IS NULL))")
    int getMaxOrder(@Param("parentId") Long parentId);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.order = :newOrder WHERE c.id = :categoryId")
    int updateOrder(@Param("categoryId") Long categoryId, @Param("newOrder") Integer newOrder);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.order = c.order + :delta " +
            "WHERE (c.parent.id = :parentId OR (:parentId IS NULL AND c.parent IS NULL)) " +
            "AND c.order >= :startOrder")
    int shiftOrders(@Param("parentId") Long parentId,
                    @Param("startOrder") Integer startOrder,
                    @Param("delta") Integer delta);

    @Query("SELECT c FROM Category c " +
            "WHERE (SELECT COUNT(cat) FROM Category cat WHERE cat.id = c.parent.id) = :level")
    List<Category> findByLevel(@Param("level") int level);

    @Query(value = "WITH RECURSIVE category_depth AS (" +
            "    SELECT id, parent_id, 1 as depth " +
            "    FROM categories WHERE parent_id IS NULL " +
            "    UNION ALL " +
            "    SELECT c.id, c.parent_id, cd.depth + 1 " +
            "    FROM categories c " +
            "    INNER JOIN category_depth cd ON c.parent_id = cd.id" +
            ") " +
            "SELECT MAX(depth) FROM category_depth", nativeQuery = true)
    Integer getMaxDepth();

    @Query("SELECT c1 FROM Category c1, Category c2 " +
            "WHERE c1.id < c2.id AND (c1.name = c2.name OR c1.slug = c2.slug)")
    List<Category> findDuplicates();

    @Query("SELECT c FROM Category c WHERE c.products IS EMPTY")
    List<Category> findEmptyCategories();

    Page<Category> findAllByOrderByOrderAsc(Pageable pageable);

    @Query("SELECT c FROM Category c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.slug) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY c.order ASC")
    Page<Category> searchCategories(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT c.name, " +
            "COUNT(DISTINCT p) as productCount, " +
            "COUNT(DISTINCT ch) as childCount " +
            "FROM Category c " +
            "LEFT JOIN c.products p " +
            "LEFT JOIN c.children ch " +
            "GROUP BY c.id, c.name " +
            "ORDER BY c.order ASC")
    List<Object[]> getCategoryStatistics();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.category.id = :newCategoryId WHERE p.category.id = :oldCategoryId")
    int reassignProductsToCategory(@Param("oldCategoryId") Long oldCategoryId,
                                   @Param("newCategoryId") Long newCategoryId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Category c WHERE c.products IS EMPTY")
    int deleteEmptyCategories();
}