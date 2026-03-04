package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;

import java.util.List;

@Repository
public interface ComponentTypeRepository extends JpaRepository<ComponentType, Long> {

    ComponentType findByName(String name);

    List<ComponentType> findByNameContainingIgnoreCase(String name);

    List<ComponentType> findAllByOrderByOrder_stepAsc();

    List<ComponentType> findAllByOrderByOrder_stepDesc();

    boolean existsByName(String name);
}