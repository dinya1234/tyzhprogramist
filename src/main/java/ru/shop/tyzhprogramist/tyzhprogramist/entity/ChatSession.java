package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "chat_sessions")
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    private User consultant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ChatStatus status;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "context_content_type", length = 50)
    private String contextContentType;

    @Column(name = "context_object_id")
    private Long contextObjectId;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;


    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatSession() {
        this.startedAt = LocalDateTime.now();
        this.status = ChatStatus.PENDING;
    }

    public ChatSession(User user, String sourceUrl) {
        this.user = user;
        this.sourceUrl = sourceUrl;
        this.startedAt = LocalDateTime.now();
        this.status = ChatStatus.PENDING;
    }

    public ChatSession(User user, String sourceUrl,
                       String contextContentType, Long contextObjectId) {
        this.user = user;
        this.sourceUrl = sourceUrl;
        this.contextContentType = contextContentType;
        this.contextObjectId = contextObjectId;
        this.startedAt = LocalDateTime.now();
        this.status = ChatStatus.PENDING;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setSession(this);
    }

    public void removeMessage(ChatMessage message) {
        messages.remove(message);
        message.setSession(null);
    }
}