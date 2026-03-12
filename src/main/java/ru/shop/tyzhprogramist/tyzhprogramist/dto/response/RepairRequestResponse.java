package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RepairRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RepairRequestResponse(
        Long id,
        String deviceType,
        String problemDescription,
        String status,
        String masterComment,
        BigDecimal estimatedPrice,
        BigDecimal finalPrice,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime createdAt,

        String userName
) {
    public static RepairRequestResponse from(RepairRequest request) {
        if (request == null) return null;

        return RepairRequestResponse.builder()
                .id(request.getId())
                .deviceType(request.getDeviceType())
                .problemDescription(request.getProblemDescription())
                .status(request.getStatus())
                .masterComment(request.getMasterComment())
                .estimatedPrice(request.getEstimatedPrice())
                .finalPrice(request.getFinalPrice())
                .createdAt(request.getCreatedAt())
                .userName(request.getUser() != null ? request.getUser().getUsername() : null)
                .build();
    }
}