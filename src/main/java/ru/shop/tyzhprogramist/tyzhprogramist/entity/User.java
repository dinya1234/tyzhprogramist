package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "date_joined", nullable = false)
    private LocalDateTime date_joined;

    @Column(name="avatar")
    private String avatar;

    @Column(name = "notifications", nullable = false)
    private Boolean notifications = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.CLIENT;

    public User(){}

    public User (String username, String email , String password , String first_name , String last_name) {
    this.date_joined= LocalDateTime.now();
    this.email= email;
    this.first_name= first_name;
    this.username=username;
    this.last_name=last_name;
    this.password=password;
    }

}
