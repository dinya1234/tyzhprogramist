package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

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

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "purchase_count")
    private Integer purchaseCount = 0;

    @Column(name = "warranty_months")
    private Integer warrantyMonths;

    @Column(name = "weight_kg")
    private BigDecimal weight;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductFeedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductItem> productItems = new HashSet<>();

    @OneToMany(mappedBy = "contentType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FileAttachment> images = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "related_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "related_product_id")
    )
    private Set<Product> relatedProducts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "frequently_bought_together",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "related_product_id")
    )
    private Set<Product> frequentlyBoughtWith = new HashSet<>();

    public Product() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.isNew = false;
        this.isBestseller = false;
        this.rating = 0.0;
    }

    public Product(String sku, Category category, String name, String slug,
                   String shortDescription, String fullDescription,
                   BigDecimal price, BigDecimal oldPrice, Integer quantity) {
        this.sku = sku;
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
        this.viewsCount = 0;
        this.purchaseCount = 0;
    }

    public void addFeedback(ProductFeedback feedback) {
        feedbacks.add(feedback);
        feedback.setProduct(this);
    }

    public void removeFeedback(ProductFeedback feedback) {
        feedbacks.remove(feedback);
        feedback.setProduct(null);
    }

    public void addRelatedProduct(Product product) {
        relatedProducts.add(product);
    }

    public void removeRelatedProduct(Product product) {
        relatedProducts.remove(product);
    }

    public void incrementViews() {
        this.viewsCount++;
    }

    public void incrementPurchaseCount() {
        this.purchaseCount++;
    }
}