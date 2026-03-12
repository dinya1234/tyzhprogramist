package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComponentTypeResponse(
        Long id,
        String name,
        Integer orderStep
) {
    public static ComponentTypeResponse from(ComponentType type) {
        if (type == null) return null;

        return ComponentTypeResponse.builder()
                .id(type.getId())
                .name(type.getName())
                .orderStep(type.getOrder_step())
                .build();
    }
}