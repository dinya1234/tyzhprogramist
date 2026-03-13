package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FileAttachment;
import java.time.LocalDateTime;


@Data
public class FileAttachmentResponse {
    private Long id;
    private String filePath;
    private String originalFilename;
    private Long fileSize;
    private String mimeType;
    private Boolean isMain;
    private Integer sortOrder;
    private LocalDateTime uploadedAt;

    public static FileAttachmentResponse from(FileAttachment file) {
        if (file == null) return null;

        FileAttachmentResponse response = new FileAttachmentResponse();
        response.setId(file.getId());
        response.setFilePath(file.getFilePath());
        response.setOriginalFilename(file.getOriginalFilename());
        response.setFileSize(file.getFileSize());
        response.setMimeType(file.getMimeType());
        response.setIsMain(file.getIsMain());
        response.setSortOrder(file.getSortOrder());
        response.setUploadedAt(file.getUploadedAt());
        return response;
    }
}