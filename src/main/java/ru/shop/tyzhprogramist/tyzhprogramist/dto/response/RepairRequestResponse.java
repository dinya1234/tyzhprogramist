package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RepairRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class RepairRequestResponse {
    private Long id;
    private String deviceType;
    private String problemDescription;
    private String status;
    private String masterComment;
    private BigDecimal estimatedPrice;
    private BigDecimal finalPrice;
    private LocalDateTime createdAt;


    private Long userId;
    private String userName;
    private String userPhone;

    public static RepairRequestResponse from(RepairRequest request) {
        if (request == null) return null;

        RepairRequestResponse response = new RepairRequestResponse();
        response.setId(request.getId());
        response.setDeviceType(request.getDeviceType());
        response.setProblemDescription(request.getProblemDescription());
        response.setStatus(request.getStatus());
        response.setMasterComment(request.getMasterComment());
        response.setEstimatedPrice(request.getEstimatedPrice());
        response.setFinalPrice(request.getFinalPrice());
        response.setCreatedAt(request.getCreatedAt());

        if (request.getUser() != null) {
            response.setUserId(request.getUser().getId());
            response.setUserName(request.getUser().getUsername());
            response.setUserPhone(request.getUser().getPhone());
        }

        return response;
    }
}