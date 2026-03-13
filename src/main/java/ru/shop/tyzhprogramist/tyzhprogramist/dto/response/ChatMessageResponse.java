package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatMessage;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SenderType;
import java.time.LocalDateTime;

@Data
public class ChatMessageResponse {
    private Long id;
    private SenderType senderType;
    private String message;
    private LocalDateTime timestamp;

    public static ChatMessageResponse from(ChatMessage message) {
        if (message == null) return null;

        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setSenderType(message.getSenderType());
        response.setMessage(message.getMessage());
        response.setTimestamp(message.getTimestamp());
        return response;
    }
}