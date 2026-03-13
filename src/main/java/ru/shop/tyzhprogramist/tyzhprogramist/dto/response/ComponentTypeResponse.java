package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;

@Data
public class ComponentTypeResponse {
    private Long id;
    private String name;
    private Integer orderStep;

    public static ComponentTypeResponse from(ComponentType type) {
        if (type == null) return null;

        ComponentTypeResponse response = new ComponentTypeResponse();
        response.setId(type.getId());
        response.setName(type.getName());
        response.setOrderStep(type.getOrder_step());
        return response;
    }
}