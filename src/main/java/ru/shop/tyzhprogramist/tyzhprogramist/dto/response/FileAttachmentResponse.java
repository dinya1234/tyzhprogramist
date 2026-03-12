package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FileAttachment;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileAttachmentResponse(
        Long id,
        String filePath,
        String originalFilename,
        Long fileSize,
        String mimeType,
        Boolean isMain,
        Integer sortOrder,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime uploadedAt
) {
    public static FileAttachmentResponse from(FileAttachment file) {
        if (file == null) return null;

        return FileAttachmentResponse.builder()
                .id(file.getId())
                .filePath(file.getFilePath())
                .originalFilename(file.getOriginalFilename())
                .fileSize(file.getFileSize())
                .mimeType(file.getMimeType())
                .isMain(file.getIsMain())
                .sortOrder(file.getSortOrder())
                .uploadedAt(file.getUploadedAt())
                .build();
    }
}