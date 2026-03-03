package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "pc_builds")
public class PcBuild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @Column(name = "views_count", nullable = false)
    private Integer viewsCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public PcBuild() {
        this.createdAt = LocalDateTime.now();
    }

    public PcBuild(User user, String name, Boolean isPublic) {
        this.user = user;
        this.name = name;
        this.isPublic = isPublic;
        this.viewsCount = 0;
        this.createdAt = LocalDateTime.now();
    }
    //метод для увеличения просмотров (пока что так мб изменим)
    public void incrementViews() {
        this.viewsCount++;
    }
}