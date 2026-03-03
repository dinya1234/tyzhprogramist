package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findBySlug(String slug);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByCategoryAndIsActiveTrue(Category category);

    List<Product> findAllByIsActiveTrueAndIsBestsellerTrue();

    List<Product> findAllByIsActiveTrueAndIsNewTrue();
}