package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatSession;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatSessionResponse {
    private Long id;
    private ChatStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private String userName;
    private String userEmail;

    private String consultantName;

    private List<ChatMessageResponse> messages = new ArrayList<>();

    public static ChatSessionResponse from(ChatSession session) {
        if (session == null) return null;

        ChatSessionResponse response = new ChatSessionResponse();
        response.setId(session.getId());
        response.setStatus(session.getStatus());
        response.setStartedAt(session.getStartedAt());
        response.setEndedAt(session.getEndedAt());

        if (session.getUser() != null) {
            response.setUserName(session.getUser().getUsername());
            response.setUserEmail(session.getUser().getEmail());
        }

        if (session.getConsultant() != null) {
            response.setConsultantName(session.getConsultant().getUsername());
        }

        return response;
    }
}