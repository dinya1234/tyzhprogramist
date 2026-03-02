package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "file_attachments")
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "is_main")
    private Boolean isMain = false;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public FileAttachment() {
        this.uploadedAt = LocalDateTime.now();
    }

    public FileAttachment(String contentType, Long objectId, String filePath,
                          String originalFilename, Long fileSize, String mimeType) {
        this.contentType = contentType;
        this.objectId = objectId;
        this.filePath = filePath;
        this.originalFilename = originalFilename;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.uploadedAt = LocalDateTime.now();
        this.isMain = false;
    }
}