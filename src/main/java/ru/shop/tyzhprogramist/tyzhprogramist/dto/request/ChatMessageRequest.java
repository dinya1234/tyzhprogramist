package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SenderType;

public record ChatMessageRequest(

        @NotNull
        @Positive(message = "айди сессии не может быть отрицательным")
        Long sessionId,
        @NotBlank
        String message,
        @NotNull
        SenderType senderType
) {
}
