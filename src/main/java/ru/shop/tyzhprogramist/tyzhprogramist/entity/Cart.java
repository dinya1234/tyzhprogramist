package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_key", length = 255)//подумать над реализацией, но пока так
    private String sessionKey;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Cart() {
        this.createdAt = LocalDateTime.now();
    }

    public Cart(User user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public Cart(String sessionKey) {
        this.sessionKey = sessionKey;
        this.createdAt = LocalDateTime.now();
    }
}