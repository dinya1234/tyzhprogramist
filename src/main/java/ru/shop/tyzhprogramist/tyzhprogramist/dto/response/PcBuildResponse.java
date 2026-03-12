package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.PcBuild;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PcBuildResponse(
        Long id,
        String name,
        Boolean isPublic,
        Integer viewsCount,

        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDateTime createdAt,

        String userName,
        BigDecimal totalPrice,
        List<PcBuildComponentResponse> components
) {
    public static PcBuildResponse from(PcBuild build, List<PcBuildComponentResponse> components,
                                       BigDecimal totalPrice) {
        if (build == null) return null;

        return PcBuildResponse.builder()
                .id(build.getId())
                .name(build.getName())
                .isPublic(build.getIsPublic())
                .viewsCount(build.getViewsCount())
                .createdAt(build.getCreatedAt())
                .userName(build.getUser() != null ? build.getUser().getUsername() : null)
                .totalPrice(totalPrice)
                .components(components)
                .build();
    }
}