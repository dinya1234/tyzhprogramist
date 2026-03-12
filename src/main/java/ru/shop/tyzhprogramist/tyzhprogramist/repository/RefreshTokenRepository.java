package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RefreshToken;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    int deleteAllExpiredTokens(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(rt) > 0 FROM RefreshToken rt WHERE rt.user.id = :userId AND rt.revoked = false")
    boolean hasValidToken(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    int revokeAllUserTokens(@Param("userId") Long userId);

    boolean existsByToken(String token);
}