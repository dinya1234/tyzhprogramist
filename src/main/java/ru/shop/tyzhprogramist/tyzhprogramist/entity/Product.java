package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean getNew() {
        return isNew;
    }

    public Boolean getBestseller() {
        return isBestseller;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public void setBestseller(Boolean bestseller) {
        isBestseller = bestseller;
    }
}

