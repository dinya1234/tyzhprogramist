package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.ChatMessageRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ChatMessageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatMessage;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SenderType;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ChatService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request, Authentication auth) {
        boolean isModerator = false;

        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) auth.getPrincipal();
            isModerator = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR") ||
                            a.getAuthority().equals("ROLE_ADMIN"));
        }

        // Сохраняем сообщение
        ChatMessage message;
        if (isModerator) {
            message = chatService.sendConsultantMessage(request.getSessionId(), request.getMessage());
        } else {
            message = chatService.sendUserMessage(request.getSessionId(), request.getMessage());
        }

        ChatMessageResponse response = ChatMessageResponse.from(message);

        // СПОСОБ 1: Используем MessageBuilder для явного создания сообщения
        org.springframework.messaging.Message<ChatMessageResponse> msg =
                org.springframework.messaging.support.MessageBuilder
                        .withPayload(response)
                        .build();

        messagingTemplate.send("/topic/chat/" + request.getSessionId(), msg);

        // Если сообщение от пользователя - уведомляем модераторов
        if (message.getSenderType() == SenderType.USER) {
            Map<String, Object> notification = new HashMap<>();
            notification.put("sessionId", request.getSessionId());
            notification.put("message", response);
            notification.put("pendingCount", chatService.getPendingSessions().size());

            org.springframework.messaging.Message<Map<String, Object>> notificationMsg =
                    org.springframework.messaging.support.MessageBuilder
                            .withPayload(notification)
                            .build();

            messagingTemplate.send("/topic/new-messages", notificationMsg);
        }
    }
}