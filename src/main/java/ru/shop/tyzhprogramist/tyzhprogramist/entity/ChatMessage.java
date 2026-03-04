package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    @Column(name = "sender_type", nullable = false, length = 20)
    private String senderType;  // "Пользователь" или "Консультант" добавить енум по идее

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;//время отправки соо

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(ChatSession session, String senderType, String message) {
        this.session = session;
        this.senderType = senderType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}