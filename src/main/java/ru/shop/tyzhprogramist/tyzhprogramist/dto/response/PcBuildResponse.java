package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.PcBuild;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PcBuildResponse {
    private Long id;
    private String name;
    private Boolean isPublic;
    private Integer viewsCount;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;

    private Long userId;
    private String userName;

    private List<PcBuildComponentResponse> components = new ArrayList<>();

    public static PcBuildResponse from(PcBuild build) {
        if (build == null) return null;

        PcBuildResponse response = new PcBuildResponse();
        response.setId(build.getId());
        response.setName(build.getName());
        response.setIsPublic(build.getIsPublic());
        response.setViewsCount(build.getViewsCount());
        response.setCreatedAt(build.getCreatedAt());

        if (build.getUser() != null) {
            response.setUserId(build.getUser().getId());
            response.setUserName(build.getUser().getUsername());
        }

        return response;
    }
}