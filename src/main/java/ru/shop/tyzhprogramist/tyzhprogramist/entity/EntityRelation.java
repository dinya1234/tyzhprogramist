package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "entity_relations")
public class EntityRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false, length = 20)
    private RelationType relationType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "from_content_type", nullable = false, length = 50)
    private String fromContentType;

    @Column(name = "from_object_id", nullable = false)
    private Long fromObjectId;

    @Column(name = "to_content_type", nullable = false, length = 50)
    private String toContentType;

    @Column(name = "to_object_id", nullable = false)
    private Long toObjectId;

    @Column(name = "is_compatible")
    private Boolean isCompatible;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public EntityRelation() {
        this.createdAt = LocalDateTime.now();
    }

    // Конструктор для совместимости (compatibility)
    public EntityRelation(String fromContentType, Long fromObjectId,
                          String toContentType, Long toObjectId,
                          Boolean isCompatible) {
        this.relationType = RelationType.COMPATIBILITY;
        this.fromContentType = fromContentType;
        this.fromObjectId = fromObjectId;
        this.toContentType = toContentType;
        this.toObjectId = toObjectId;
        this.isCompatible = isCompatible;
        this.createdAt = LocalDateTime.now();
    }

    public EntityRelation(User user, String name,
                          String fromContentType, Long fromObjectId,
                          String toContentType, Long toObjectId) {
        this.relationType = RelationType.COMPARISON;
        this.user = user;
        this.name = name;
        this.fromContentType = fromContentType;
        this.fromObjectId = fromObjectId;
        this.toContentType = toContentType;
        this.toObjectId = toObjectId;
        this.createdAt = LocalDateTime.now();
    }
}