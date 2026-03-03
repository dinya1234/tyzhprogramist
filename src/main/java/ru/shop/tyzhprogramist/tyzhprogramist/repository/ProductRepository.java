package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<User> findByProductname(String username);


}