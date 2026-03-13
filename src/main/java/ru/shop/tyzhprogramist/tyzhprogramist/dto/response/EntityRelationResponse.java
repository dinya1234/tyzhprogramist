package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RelationType;
import java.time.LocalDateTime;


@Data
public class EntityRelationResponse {
    private Long id;
    private RelationType relationType;
    private String fromContentType;
    private Long fromObjectId;
    private String toContentType;
    private Long toObjectId;
    private Boolean isCompatible;
    private String name;
    private LocalDateTime createdAt;

    private String userName;

    public static EntityRelationResponse from(EntityRelation relation) {
        if (relation == null) return null;

        EntityRelationResponse response = new EntityRelationResponse();
        response.setId(relation.getId());
        response.setRelationType(relation.getRelationType());
        response.setFromContentType(relation.getFromContentType());
        response.setFromObjectId(relation.getFromObjectId());
        response.setToContentType(relation.getToContentType());
        response.setToObjectId(relation.getToObjectId());
        response.setIsCompatible(relation.getIsCompatible());
        response.setName(relation.getName());
        response.setCreatedAt(relation.getCreatedAt());

        if (relation.getUser() != null) {
            response.setUserName(relation.getUser().getUsername());
        }

        return response;
    }
}