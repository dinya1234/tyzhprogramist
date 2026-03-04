package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SiteSettings;
import java.util.Optional;

@Repository
public interface SiteSettingsRepository extends JpaRepository<SiteSettings, Long> {


    @Query("SELECT s FROM SiteSettings s WHERE s.id = 1")
    Optional<SiteSettings> findSingleton();

    @Query("SELECT COUNT(s) > 0 FROM SiteSettings s WHERE s.id = 1")
    boolean existsSingleton();

    @Query("DELETE FROM SiteSettings s WHERE s.id = 1")
    void deleteSingleton();
}