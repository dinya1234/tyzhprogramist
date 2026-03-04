package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "chat_sessions")
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  //хз почему может быть нулл надо пересмотреть это

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private User consultant;  //хз почему может быть нулл надо пересмотреть это

    @Column(name = "status", nullable = false, length = 20)
    private String status;  // В ожидании, Активен, Завершен. добавить енум скорее всего

    @Column(name = "source_url")
    private String sourceUrl;  // откуда пришел пользователь хз зачем к вове вопросы

    @Column(name = "context_content_type", length = 50)
    private String contextContentType;  // "Product" или "PcBuild" хз как реализовать но пока так будет

    @Column(name = "context_object_id")
    private Long contextObjectId;  // ID товара или сборки

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public ChatSession() {
        this.startedAt = LocalDateTime.now();
        this.status = "В ожидании";
    }

    public ChatSession(User user, String sourceUrl) {
        this.user = user;
        this.sourceUrl = sourceUrl;
        this.startedAt = LocalDateTime.now();
        this.status = "В ожидании";
    }

    public ChatSession(User user, String sourceUrl,
                       String contextContentType, Long contextObjectId) {
        this.user = user;
        this.sourceUrl = sourceUrl;
        this.contextContentType = contextContentType;
        this.contextObjectId = contextObjectId;
        this.startedAt = LocalDateTime.now();
        this.status = "В ожидании";
    }
}