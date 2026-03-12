package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatSession;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatStatus;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatSessionResponse(
        Long id,
        ChatStatus status,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime startedAt,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime endedAt,

        String userName,
        String consultantName,
        List<ChatMessageResponse> messages
) {
    public static ChatSessionResponse from(ChatSession session, List<ChatMessageResponse> messages) {
        if (session == null) return null;

        return ChatSessionResponse.builder()
                .id(session.getId())
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .userName(session.getUser() != null ? session.getUser().getUsername() : "Гость")
                .consultantName(session.getConsultant() != null ? session.getConsultant().getUsername() : null)
                .messages(messages)
                .build();
    }
}