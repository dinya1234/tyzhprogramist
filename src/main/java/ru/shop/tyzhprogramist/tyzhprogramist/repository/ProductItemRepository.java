package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ParentType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    List<ProductItem> findByParentTypeAndParentId(ParentType parentType, Long parentId);

    @Query("SELECT pi FROM ProductItem pi JOIN FETCH pi.product WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    List<ProductItem> findByParentTypeAndParentIdWithProduct(@Param("parentType") ParentType parentType,
                                                             @Param("parentId") Long parentId);

    List<ProductItem> findByParentTypeAndParentIdIn(ParentType parentType, List<Long> parentIds);

    default List<ProductItem> findCartItems(Long cartId) {
        return findByParentTypeAndParentId(ParentType.CART, cartId);
    }

    default List<ProductItem> findCartItemsWithProduct(Long cartId) {
        return findByParentTypeAndParentIdWithProduct(ParentType.CART, cartId);
    }

    default List<ProductItem> findOrderItems(Long orderId) {
        return findByParentTypeAndParentId(ParentType.ORDER, orderId);
    }

    default List<ProductItem> findOrderItemsWithProduct(Long orderId) {
        return findByParentTypeAndParentIdWithProduct(ParentType.ORDER, orderId);
    }

    default List<ProductItem> findPcBuildItems(Long pcBuildId) {
        return findByParentTypeAndParentId(ParentType.PC_BUILD, pcBuildId);
    }

    default List<ProductItem> findPcBuildItemsWithProduct(Long pcBuildId) {
        return findByParentTypeAndParentIdWithProduct(ParentType.PC_BUILD, pcBuildId);
    }

    Optional<ProductItem> findByParentTypeAndParentIdAndProductId(
            ParentType parentType, Long parentId, Long productId);

    boolean existsByParentTypeAndParentIdAndProductId(
            ParentType parentType, Long parentId, Long productId);

    default Optional<ProductItem> findCartItem(Long cartId, Long productId) {
        return findByParentTypeAndParentIdAndProductId(ParentType.CART, cartId, productId);
    }

    default Optional<ProductItem> findOrderItem(Long orderId, Long productId) {
        return findByParentTypeAndParentIdAndProductId(ParentType.ORDER, orderId, productId);
    }

    default Optional<ProductItem> findPcBuildComponent(Long pcBuildId, Long productId) {
        return findByParentTypeAndParentIdAndProductId(ParentType.PC_BUILD, pcBuildId, productId);
    }

    @Query("SELECT COUNT(pi) FROM ProductItem pi WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    long countByParent(@Param("parentType") ParentType parentType, @Param("parentId") Long parentId);

    @Query("SELECT SUM(pi.quantity) FROM ProductItem pi WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    Integer sumQuantityByParent(@Param("parentType") ParentType parentType, @Param("parentId") Long parentId);

    @Query("SELECT SUM(pi.price * pi.quantity) FROM ProductItem pi WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    BigDecimal sumTotalPriceByParent(@Param("parentType") ParentType parentType, @Param("parentId") Long parentId);

    default long countCartItems(Long cartId) {
        return countByParent(ParentType.CART, cartId);
    }

    default Integer sumCartQuantity(Long cartId) {
        return sumQuantityByParent(ParentType.CART, cartId);
    }

    default BigDecimal sumCartTotal(Long cartId) {
        return sumTotalPriceByParent(ParentType.CART, cartId);
    }

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.quantity = :quantity WHERE pi.id = :itemId")
    int updateQuantity(@Param("itemId") Long itemId, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.quantity = :quantity " +
            "WHERE pi.parentType = :parentType AND pi.parentId = :parentId AND pi.product.id = :productId")
    int updateItemQuantity(@Param("parentType") ParentType parentType,
                           @Param("parentId") Long parentId,
                           @Param("productId") Long productId,
                           @Param("quantity") Integer quantity);

    default int updateCartItemQuantity(Long cartId, Long productId, Integer quantity) {
        return updateItemQuantity(ParentType.CART, cartId, productId, quantity);
    }

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.price = :price WHERE pi.id = :itemId")
    int updatePrice(@Param("itemId") Long itemId, @Param("price") BigDecimal price);

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.price = (" +
            "    SELECT p.price FROM Product p WHERE p.id = pi.product.id" +
            ") WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    int refreshPrices(@Param("parentType") ParentType parentType, @Param("parentId") Long parentId);

    default int refreshOrderPrices(Long orderId) {
        return refreshPrices(ParentType.ORDER, orderId);
    }

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.quantity = pi.quantity + :increment WHERE pi.id = :itemId")
    int incrementQuantity(@Param("itemId") Long itemId, @Param("increment") Integer increment);

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.quantity = pi.quantity - :decrement " +
            "WHERE pi.id = :itemId AND pi.quantity >= :decrement")
    int decrementQuantity(@Param("itemId") Long itemId, @Param("decrement") Integer decrement);

    @Modifying
    @Transactional
    void deleteByParentTypeAndParentId(ParentType parentType, Long parentId);

    @Modifying
    @Transactional
    void deleteByParentTypeAndParentIdAndProductId(ParentType parentType, Long parentId, Long productId);

    default void clearCart(Long cartId) {
        deleteByParentTypeAndParentId(ParentType.CART, cartId);
    }

    default void removeFromCart(Long cartId, Long productId) {
        deleteByParentTypeAndParentIdAndProductId(ParentType.CART, cartId, productId);
    }

    default void clearPcBuild(Long pcBuildId) {
        deleteByParentTypeAndParentId(ParentType.PC_BUILD, pcBuildId);
    }

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.parentType = :newParentType, pi.parentId = :newParentId " +
            "WHERE pi.parentType = :oldParentType AND pi.parentId = :oldParentId")
    int moveItems(@Param("oldParentType") ParentType oldParentType,
                  @Param("oldParentId") Long oldParentId,
                  @Param("newParentType") ParentType newParentType,
                  @Param("newParentId") Long newParentId);

    @Query(value = "INSERT INTO product_items (parent_type, parent_id, product_id, quantity, price) " +
            "SELECT :newParentType, :newParentId, product_id, quantity, price " +
            "FROM product_items WHERE parent_type = :oldParentType AND parent_id = :oldParentId",
            nativeQuery = true)
    @Modifying
    @Transactional
    int copyItems(@Param("oldParentType") String oldParentType,
                  @Param("oldParentId") Long oldParentId,
                  @Param("newParentType") String newParentType,
                  @Param("newParentId") Long newParentId);

    @Query("SELECT pi FROM ProductItem pi " +
            "WHERE pi.parentType = :parentType AND pi.parentId = :parentId " +
            "GROUP BY pi.product.id HAVING COUNT(pi) > 1")
    List<ProductItem> findDuplicates(@Param("parentType") ParentType parentType,
                                     @Param("parentId") Long parentId);

    @Modifying
    @Transactional
    @Query(value = "WITH duplicates AS (" +
            "    SELECT MIN(id) as keep_id, product_id, SUM(quantity) as total_quantity " +
            "    FROM product_items " +
            "    WHERE parent_type = :parentType AND parent_id = :parentId " +
            "    GROUP BY product_id " +
            ") " +
            "UPDATE product_items pi SET quantity = d.total_quantity " +
            "FROM duplicates d " +
            "WHERE pi.id = d.keep_id AND pi.parent_type = :parentType AND pi.parent_id = :parentId",
            nativeQuery = true)
    int mergeDuplicates(@Param("parentType") String parentType,
                        @Param("parentId") Long parentId);

    @Query("SELECT CASE WHEN (p.quantity >= :requestedQuantity) THEN true ELSE false END " +
            "FROM Product p WHERE p.id = :productId")
    boolean isProductAvailable(@Param("productId") Long productId,
                               @Param("requestedQuantity") Integer requestedQuantity);

    @Query("SELECT pi FROM ProductItem pi " +
            "JOIN pi.product p " +
            "WHERE pi.parentType = :parentType AND pi.parentId = :parentId " +
            "AND p.quantity < pi.quantity")
    List<ProductItem> findOutOfStockItems(@Param("parentType") ParentType parentType,
                                          @Param("parentId") Long parentId);

    default List<ProductItem> findOutOfStockItemsInCart(Long cartId) {
        return findOutOfStockItems(ParentType.CART, cartId);
    }

    @Query("SELECT " +
            "COUNT(DISTINCT pi.product.id) as uniqueProducts, " +
            "COALESCE(SUM(pi.quantity), 0) as totalQuantity, " +
            "COALESCE(SUM(pi.price * pi.quantity), 0) as totalPrice " +
            "FROM ProductItem pi " +
            "WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    Object[] getStatistics(@Param("parentType") ParentType parentType,
                           @Param("parentId") Long parentId);

    default Object[] getCartStatistics(Long cartId) {
        return getStatistics(ParentType.CART, cartId);
    }

    default Object[] getOrderStatistics(Long orderId) {
        return getStatistics(ParentType.ORDER, orderId);
    }

    default Object[] getPcBuildStatistics(Long pcBuildId) {
        return getStatistics(ParentType.PC_BUILD, pcBuildId);
    }


    List<ProductItem> findByProductId(Long productId);

    @Query("SELECT pi.parentId FROM ProductItem pi " +
            "WHERE pi.parentType = :parentType AND pi.product.id = :productId")
    List<Long> findParentIdsByProductId(@Param("parentType") ParentType parentType,
                                        @Param("productId") Long productId);

    default List<Long> findCartIdsByProductId(Long productId) {
        return findParentIdsByProductId(ParentType.CART, productId);
    }

    default List<Long> findOrderIdsByProductId(Long productId) {
        return findParentIdsByProductId(ParentType.ORDER, productId);
    }

    default List<Long> findPcBuildIdsByProductId(Long productId) {
        return findParentIdsByProductId(ParentType.PC_BUILD, productId);
    }

    @Modifying
    @Transactional
    void deleteByParentTypeAndParentIdIn(ParentType parentType, List<Long> parentIds);

    @Modifying
    @Transactional
    @Query("UPDATE ProductItem pi SET pi.price = (" +
            "    SELECT p.price FROM Product p WHERE p.id = pi.product.id" +
            ") WHERE pi.parentType = :parentType")
    int refreshAllPrices(@Param("parentType") ParentType parentType);

    default int refreshAllCartPrices() {
        return refreshAllPrices(ParentType.CART);
    }

    default int refreshAllOrderPrices() {
        return refreshAllPrices(ParentType.ORDER);
    }

    @Query("SELECT CASE WHEN COUNT(pi) = 0 THEN true ELSE " +
            "MIN(CASE WHEN p.quantity >= pi.quantity THEN 1 ELSE 0 END) END " +
            "FROM ProductItem pi JOIN pi.product p " +
            "WHERE pi.parentType = :parentType AND pi.parentId = :parentId")
    boolean areAllItemsAvailable(@Param("parentType") ParentType parentType,
                                 @Param("parentId") Long parentId);

    default boolean areAllCartItemsAvailable(Long cartId) {
        return areAllItemsAvailable(ParentType.CART, cartId);
    }
}