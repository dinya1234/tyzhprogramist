package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Cart;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

    Optional<Cart> findBySessionKey(String sessionKey);

    boolean existsByUser(User user);

    boolean existsBySessionKey(String sessionKey);

    @Query("SELECT c FROM Cart c WHERE c.user IS NOT NULL")
    List<Cart> findAllAuthorizedCarts();

    @Query("SELECT c FROM Cart c WHERE c.sessionKey IS NOT NULL")
    List<Cart> findAllGuestCarts();

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.createdAt < :date AND c.user IS NULL")
    int deleteOldGuestCarts(@Param("date") LocalDateTime date);

    @Query("SELECT c FROM Cart c WHERE c.createdAt < :date AND c.sessionKey IS NOT NULL")
    List<Cart> findOldGuestCarts(@Param("date") LocalDateTime date);


    @Modifying
    @Transactional
    void deleteBySessionKeyIn(List<String> sessionKeys);

    @Query("SELECT c FROM Cart c WHERE c.sessionKey = :sessionKey AND c.user IS NULL")
    Optional<Cart> findGuestCartBySessionKey(@Param("sessionKey") String sessionKey);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.user = :user, c.sessionKey = NULL WHERE c.id = :cartId")
    int assignCartToUser(@Param("cartId") Long cartId, @Param("user") User user);

    @Query("SELECT c FROM Cart c WHERE c.user = :user OR c.sessionKey = :sessionKey")
    List<Cart> findUserAndGuestCarts(@Param("user") User user, @Param("sessionKey") String sessionKey);


    @Query("SELECT COUNT(c) FROM Cart c WHERE c.createdAt > :since")
    long countActiveCarts(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user IS NOT NULL")
    long countAuthorizedCarts();

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.sessionKey IS NOT NULL")
    long countGuestCarts();

    @Query("SELECT DATE(c.createdAt), COUNT(c) FROM Cart c " + "GROUP BY DATE(c.createdAt) " + "ORDER BY DATE(c.createdAt) DESC")
    List<Object[]> getCartCreationStatistics();

    @Query("SELECT c FROM Cart c " + "LEFT JOIN FETCH c.user " + "ORDER BY c.createdAt DESC")
    List<Cart> findAllWithUsers();

    @Query("SELECT c FROM Cart c " + "WHERE c.user IS NOT NULL AND LOWER(c.user.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Cart> findByUserEmailContaining(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.user = NULL WHERE c.user.id = :userId")
    int detachUserFromCart(@Param("userId") Long userId);
}