package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "old_price", precision = 10, scale = 2)
    private BigDecimal oldPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_new", nullable = false)
    private Boolean isNew;

    @Column(name = "is_bestseller", nullable = false)
    private Boolean isBestseller;

    public Product() {
    }

    public Product(String id,
                   Category category,
                   String name,
                   String slug,
                   String shortDescription,
                   String fullDescription,
                   BigDecimal price,
                   BigDecimal oldPrice,
                   Integer quantity) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.price = price;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.isNew = false;
        this.isBestseller = false;
        this.rating = 0.0;
    }


}

