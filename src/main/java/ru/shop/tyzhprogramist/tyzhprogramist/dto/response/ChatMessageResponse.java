package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatMessage;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SenderType;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessageResponse(
        Long id,
        SenderType senderType,
        String message,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime timestamp
) {
    public static ChatMessageResponse from(ChatMessage message) {
        if (message == null) return null;

        return ChatMessageResponse.builder()
                .id(message.getId())
                .senderType(message.getSenderType())
                .message(message.getMessage())
                .timestamp(message.getTimestamp())
                .build();
    }
}