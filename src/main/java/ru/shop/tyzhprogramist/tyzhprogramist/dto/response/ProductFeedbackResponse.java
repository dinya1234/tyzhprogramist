package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductFeedback;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductFeedbackResponse(
        Long id,
        FeedbackType feedbackType,
        Integer rating,
        String text,
        String answer,
        Boolean isPublished,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime createdAt,

        String userName,
        Long productId,
        String productName
) {
    public static ProductFeedbackResponse from(ProductFeedback feedback) {
        if (feedback == null) return null;

        return ProductFeedbackResponse.builder()
                .id(feedback.getId())
                .feedbackType(feedback.getFeedbackType())
                .rating(feedback.getRating())
                .text(feedback.getText())
                .answer(feedback.getAnswer())
                .isPublished(feedback.getIsPublished())
                .createdAt(feedback.getCreatedAt())
                .userName(feedback.getUser() != null ? feedback.getUser().getUsername() : null)
                .productId(feedback.getProduct() != null ? feedback.getProduct().getId() : null)
                .productName(feedback.getProduct() != null ? feedback.getProduct().getName() : null)
                .build();
    }
}