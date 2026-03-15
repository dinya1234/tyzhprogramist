package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FileAttachment;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.FileAttachmentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileAttachmentService {

    private final FileAttachmentRepository fileAttachmentRepository;

    @Value("${app.upload.path:uploads}")
    private String uploadPath;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/svg+xml"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/plain"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Transactional
    public FileAttachment saveFile(MultipartFile file, String contentType, Long objectId) {
        validateFile(file);

        try {
            Path uploadDir = Paths.get(uploadPath, contentType.toLowerCase());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            FileAttachment attachment = new FileAttachment(
                    contentType,
                    objectId,
                    filePath.toString(),
                    originalFilename,
                    file.getSize(),
                    file.getContentType()
            );

            int maxOrder = fileAttachmentRepository.getMaxSortOrder(contentType, objectId);
            attachment.setSortOrder(maxOrder + 1);

            if (contentType.equals("Product") && file.getContentType().startsWith("image/")) {
                boolean hasMainImage = fileAttachmentRepository
                        .existsByContentTypeAndObjectIdAndIsMainTrue(contentType, objectId);
                attachment.setIsMain(!hasMainImage);
            }

            return fileAttachmentRepository.save(attachment);

        } catch (IOException e) {
            log.error("Ошибка при сохранении файла: {}", e.getMessage(), e);
            throw new BadRequestException("Не удалось сохранить файл: " + e.getMessage());
        }
    }

    @Transactional
    public List<FileAttachment> saveFiles(List<MultipartFile> files, String contentType, Long objectId) {
        return files.stream()
                .map(file -> saveFile(file, contentType, objectId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileAttachment getFileById(Long id) {
        return fileAttachmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Файл не найден с id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getFilesForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc(
                contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getImagesForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.findImagesByEntity(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<String> getImageUrlsForEntity(String contentType, Long objectId) {
        return getImagesForEntity(contentType, objectId).stream()
                .map(this::getFileUrl)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileAttachment getMainImageForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.findMainImage(contentType, objectId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public String getMainImageUrlForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.findMainImage(contentType, objectId)
                .map(this::getFileUrl)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getDocumentsForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.findDocumentsByEntity(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getVideosForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.findVideosByEntity(contentType, objectId);
    }

    @Transactional
    public FileAttachment setAsMain(Long fileId, String contentType, Long objectId) {
        FileAttachment file = getFileById(fileId);

        if (!file.getContentType().equals(contentType) || !file.getObjectId().equals(objectId)) {
            throw new BadRequestException("Файл не принадлежит указанной сущности");
        }

        if (!file.getMimeType().startsWith("image/")) {
            throw new BadRequestException("Только изображения могут быть главными");
        }

        fileAttachmentRepository.resetMainFlagForEntity(contentType, objectId);

        file.setIsMain(true);
        return fileAttachmentRepository.save(file);
    }

    @Transactional
    public void updateSortOrder(List<Long> fileIds, String contentType, Long objectId) {
        List<FileAttachment> files = getFilesForEntity(contentType, objectId);

        Map<Long, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < fileIds.size(); i++) {
            orderMap.put(fileIds.get(i), i);
        }

        files.forEach(file -> {
            Integer newOrder = orderMap.get(file.getId());
            if (newOrder != null && !newOrder.equals(file.getSortOrder())) {
                file.setSortOrder(newOrder);
            }
        });

        fileAttachmentRepository.saveAll(files);
    }

    @Transactional
    public void deleteFile(Long id) {
        FileAttachment file = getFileById(id);

        try {
            Path filePath = Paths.get(file.getFilePath());
            Files.deleteIfExists(filePath);

            if (file.getIsMain() && file.getMimeType().startsWith("image/")) {
                List<FileAttachment> images = getImagesForEntity(file.getContentType(), file.getObjectId());
                if (!images.isEmpty() && images.size() > 1) {
                    FileAttachment newMain = images.stream()
                            .filter(f -> !f.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                    if (newMain != null) {
                        newMain.setIsMain(true);
                        fileAttachmentRepository.save(newMain);
                    }
                }
            }

            fileAttachmentRepository.delete(file);

            log.info("Файл удален: {}", file.getFilePath());

        } catch (IOException e) {
            log.error("Ошибка при удалении файла: {}", e.getMessage(), e);
            throw new BadRequestException("Не удалось удалить файл: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteAllFilesForEntity(String contentType, Long objectId) {
        List<FileAttachment> files = getFilesForEntity(contentType, objectId);

        for (FileAttachment file : files) {
            try {
                Files.deleteIfExists(Paths.get(file.getFilePath()));
            } catch (IOException e) {
                log.warn("Не удалось удалить файл: {}", file.getFilePath(), e);
            }
        }

        fileAttachmentRepository.deleteByContentTypeAndObjectId(contentType, objectId);
        log.info("Все файлы для {}-{} удалены", contentType, objectId);
    }

    @Transactional
    public List<FileAttachment> copyFiles(String sourceContentType, Long sourceObjectId,
                                          String targetContentType, Long targetObjectId) {
        List<FileAttachment> sourceFiles = getFilesForEntity(sourceContentType, sourceObjectId);

        return sourceFiles.stream()
                .map(sourceFile -> {
                    try {
                        Path sourcePath = Paths.get(sourceFile.getFilePath());
                        Path targetDir = Paths.get(uploadPath, targetContentType.toLowerCase());

                        if (!Files.exists(targetDir)) {
                            Files.createDirectories(targetDir);
                        }

                        String filename = sourcePath.getFileName().toString();
                        Path targetPath = targetDir.resolve(filename);

                        if (Files.exists(targetPath)) {
                            String extension = getFileExtension(sourceFile.getOriginalFilename());
                            filename = UUID.randomUUID().toString() + extension;
                            targetPath = targetDir.resolve(filename);
                        }

                        Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES);

                        FileAttachment newFile = new FileAttachment(
                                targetContentType,
                                targetObjectId,
                                targetPath.toString(),
                                sourceFile.getOriginalFilename(),
                                sourceFile.getFileSize(),
                                sourceFile.getMimeType()
                        );
                        newFile.setSortOrder(sourceFile.getSortOrder());
                        newFile.setIsMain(sourceFile.getIsMain() && targetContentType.equals(sourceContentType));

                        return fileAttachmentRepository.save(newFile);

                    } catch (IOException e) {
                        log.error("Ошибка при копировании файла: {}", e.getMessage(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public String getFileUrl(FileAttachment file) {
        String relativePath = file.getFilePath()
                .replace(uploadPath, "")
                .replace("\\", "/");
        return baseUrl + "/api/files" + relativePath;
    }

    public Path getFilePath(FileAttachment file) {
        return Paths.get(file.getFilePath());
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Файл пустой");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Размер файла превышает максимальный (10 MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new BadRequestException("Не удалось определить тип файла");
        }

        boolean isAllowed = ALLOWED_IMAGE_TYPES.contains(contentType) ||
                ALLOWED_DOCUMENT_TYPES.contains(contentType);

        if (!isAllowed) {
            throw new BadRequestException("Недопустимый тип файла: " + contentType);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    @Transactional(readOnly = true)
    public Object getFileStatistics() {
        return Map.of(
                "totalSize", fileAttachmentRepository.getTotalFileSize(),
                "typeStatistics", fileAttachmentRepository.getFileTypeStatistics(),
                "orphanedFiles", findOrphanedFiles(LocalDateTime.now().minusDays(30)).size()
        );
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> searchByFilename(String filename) {
        return fileAttachmentRepository.findByOriginalFilenameContainingIgnoreCase(filename);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findByMimeType(String mimeType) {
        return fileAttachmentRepository.findByMimeType(mimeType);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findByMimeTypeStartingWith(String prefix) {
        return fileAttachmentRepository.findByMimeTypeStartingWith(prefix);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findBySizeGreaterThan(Long size) {
        return fileAttachmentRepository.findByFileSizeGreaterThan(size);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findRecentlyUploaded(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return fileAttachmentRepository.findRecentlyUploaded(since);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findByUploadedAtBetween(LocalDateTime start, LocalDateTime end) {
        return fileAttachmentRepository.findByUploadedAtBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findOldFiles(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        return fileAttachmentRepository.findOldFiles(threshold);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> findOrphanedFiles(LocalDateTime threshold) {
        return fileAttachmentRepository.findOrphanedFiles(threshold);
    }

    @Transactional
    public int deleteOrphanedFiles(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        List<FileAttachment> orphans = fileAttachmentRepository.findOrphanedFiles(threshold);

        for (FileAttachment file : orphans) {
            try {
                Files.deleteIfExists(Paths.get(file.getFilePath()));
            } catch (IOException e) {
                log.warn("Не удалось удалить файл-сироту: {}", file.getFilePath(), e);
            }
            fileAttachmentRepository.delete(file);
        }

        return orphans.size();
    }

    @Transactional(readOnly = true)
    public boolean existsByFilePath(String filePath) {
        return fileAttachmentRepository.existsByFilePath(filePath);
    }

    @Transactional(readOnly = true)
    public long countFilesForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.countByContentTypeAndObjectId(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public long countImagesForEntity(String contentType, Long objectId) {
        return fileAttachmentRepository.countImagesByEntity(contentType, objectId);
    }


    @Transactional(readOnly = true)
    public List<FileAttachment> getProductImages(Long productId) {
        return fileAttachmentRepository.findProductImages(productId);
    }

    @Transactional(readOnly = true)
    public FileAttachment getProductMainImage(Long productId) {
        return fileAttachmentRepository.findProductMainImage(productId).orElse(null);
    }

    @Transactional(readOnly = true)
    public String getProductMainImageUrl(Long productId) {
        return getMainImageUrlForEntity("Product", productId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getReviewImages(Long reviewId) {
        return fileAttachmentRepository.findReviewImages(reviewId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getRepairRequestAttachments(Long repairRequestId) {
        return fileAttachmentRepository.findRepairRequestAttachments(repairRequestId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getChatMessageAttachments(Long chatMessageId) {
        return fileAttachmentRepository.findChatMessageAttachments(chatMessageId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getPcBuildImages(Long pcBuildId) {
        return fileAttachmentRepository.findPcBuildImages(pcBuildId);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getMainImagesForProducts(List<Long> productIds) {
        return fileAttachmentRepository.findMainImagesForEntities("Product", productIds);
    }

    @Transactional(readOnly = true)
    public java.util.Map<Long, String> getProductMainImageUrls(List<Long> productIds) {
        List<FileAttachment> mainImages = getMainImagesForProducts(productIds);

        return mainImages.stream()
                .collect(Collectors.toMap(
                        FileAttachment::getObjectId,
                        this::getFileUrl,
                        (url1, url2) -> url1
                ));
    }

    public boolean isImage(FileAttachment file) {
        return file.getMimeType() != null && file.getMimeType().startsWith("image/");
    }

    public boolean isDocument(FileAttachment file) {
        return file.getMimeType() != null && ALLOWED_DOCUMENT_TYPES.contains(file.getMimeType());
    }

    public boolean isVideo(FileAttachment file) {
        return file.getMimeType() != null && file.getMimeType().startsWith("video/");
    }
}