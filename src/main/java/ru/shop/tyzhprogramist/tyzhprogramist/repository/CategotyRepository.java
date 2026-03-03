package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategotyRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    Optional<Category> findByName(String name);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    List<Category> findAllByParentIsNullOrderByOrderAsc();

    List<Category> findAllByParentIdOrderByOrderAsc(Long parentId);
}

