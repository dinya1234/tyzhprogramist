package ru.shop.tyzhprogramist.tyzhprogramist.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Banner;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByIsActiveTrueOrderByDisplayOrderAsc();
    List<Banner> findAllByOrderByDisplayOrderAsc();
}