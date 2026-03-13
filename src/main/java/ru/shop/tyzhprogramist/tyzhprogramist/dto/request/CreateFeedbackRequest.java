package ru.shop.tyzhprogramist.tyzhprogramist.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateFeedbackRequest {

    @NotNull
    private Long productId;

    @NotBlank
    @Size(max = 2000)
    private String text;

    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    private Boolean isQuestion = false;

    private Long parentFeedbackId;
}