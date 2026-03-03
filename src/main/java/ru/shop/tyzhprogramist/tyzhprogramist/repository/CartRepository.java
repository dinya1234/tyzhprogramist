package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Cart;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    List<Cart> findAllByUser(User user);

    Optional<Cart> findBySessionKey(String sessionKey);

    boolean existsByUser(User user);

    boolean existsBySessionKey(String sessionKey);
}

