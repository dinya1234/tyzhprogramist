package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductFeedback;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType;
import java.time.LocalDateTime;


@Data
public class ProductFeedbackResponse {
    private Long id;
    private FeedbackType feedbackType;
    private Integer rating;
    private String text;
    private String answer;
    private Boolean isPublished;
    private LocalDateTime createdAt;

    private String userName;

    private Long productId;
    private String productName;

    public static ProductFeedbackResponse from(ProductFeedback feedback) {
        if (feedback == null) return null;

        ProductFeedbackResponse response = new ProductFeedbackResponse();
        response.setId(feedback.getId());
        response.setFeedbackType(feedback.getFeedbackType());
        response.setRating(feedback.getRating());
        response.setText(feedback.getText());
        response.setAnswer(feedback.getAnswer());
        response.setIsPublished(feedback.getIsPublished());
        response.setCreatedAt(feedback.getCreatedAt());

        if (feedback.getUser() != null) {
            response.setUserName(feedback.getUser().getUsername());
        }

        if (feedback.getProduct() != null) {
            response.setProductId(feedback.getProduct().getId());
            response.setProductName(feedback.getProduct().getName());
        }

        return response;
    }
}