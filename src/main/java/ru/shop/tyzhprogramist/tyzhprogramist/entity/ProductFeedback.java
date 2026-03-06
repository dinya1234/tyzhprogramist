package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "product_feedback")
public class ProductFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type", nullable = false)
    private FeedbackType feedbackType;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "answer")
    private String answer;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ProductFeedback() {
        this.createdAt = LocalDateTime.now();
    }

    public ProductFeedback(Product product, User user, FeedbackType feedbackType,
                           String text, Integer rating) {
        this.product = product;
        this.user = user;
        this.feedbackType = feedbackType;
        this.text = text;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
        this.isPublished = false;
    }
}