package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RelationType;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EntityRelationResponse(
        Long id,
        RelationType relationType,
        String fromContentType,
        Long fromObjectId,
        String toContentType,
        Long toObjectId,
        Boolean isCompatible,
        String name,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime createdAt,

        String userName
) {
    public static EntityRelationResponse from(EntityRelation relation) {
        if (relation == null) return null;

        return EntityRelationResponse.builder()
                .id(relation.getId())
                .relationType(relation.getRelationType())
                .fromContentType(relation.getFromContentType())
                .fromObjectId(relation.getFromObjectId())
                .toContentType(relation.getToContentType())
                .toObjectId(relation.getToObjectId())
                .isCompatible(relation.getIsCompatible())
                .name(relation.getName())
                .createdAt(relation.getCreatedAt())
                .userName(relation.getUser() != null ? relation.getUser().getUsername() : null)
                .build();
    }
}