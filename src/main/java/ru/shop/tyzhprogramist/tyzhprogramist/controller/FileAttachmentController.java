package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.FileAttachmentResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FileAttachment;
import ru.shop.tyzhprogramist.tyzhprogramist.service.FileAttachmentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileAttachmentController {

    private final FileAttachmentService fileAttachmentService;

    @PostMapping("/upload/{contentType}/{objectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<FileAttachmentResponse>> uploadFiles(
            @RequestParam List<MultipartFile> files,
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        List<FileAttachment> attachments = fileAttachmentService.saveFiles(files, contentType, objectId);
        log.info("Загружено {} файлов для {}-{}", attachments.size(), contentType, objectId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachments.stream()
                        .map(FileAttachmentResponse::from)
                        .toList());
    }

    @PostMapping("/upload/single/{contentType}/{objectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<FileAttachmentResponse> uploadSingleFile(
            @RequestParam MultipartFile file,
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        FileAttachment attachment = fileAttachmentService.saveFile(file, contentType, objectId);
        log.info("Загружен файл {} для {}-{}", attachment.getOriginalFilename(), contentType, objectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(FileAttachmentResponse.from(attachment));
    }

    @GetMapping("/{contentType}/{objectId}")
    public ResponseEntity<List<FileAttachmentResponse>> getFiles(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        List<FileAttachment> files = fileAttachmentService.getFilesForEntity(contentType, objectId);
        return ResponseEntity.ok(files.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/{contentType}/{objectId}/images")
    public ResponseEntity<List<String>> getImageUrls(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        return ResponseEntity.ok(fileAttachmentService.getImageUrlsForEntity(contentType, objectId));
    }

    @GetMapping("/{contentType}/{objectId}/main-image")
    public ResponseEntity<String> getMainImageUrl(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        String url = fileAttachmentService.getMainImageUrlForEntity(contentType, objectId);
        return url != null ? ResponseEntity.ok(url) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{contentType}/{objectId}/documents")
    public ResponseEntity<List<FileAttachmentResponse>> getDocuments(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        List<FileAttachment> documents = fileAttachmentService.getDocumentsForEntity(contentType, objectId);
        return ResponseEntity.ok(documents.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/{contentType}/{objectId}/videos")
    public ResponseEntity<List<FileAttachmentResponse>> getVideos(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        List<FileAttachment> videos = fileAttachmentService.getVideosForEntity(contentType, objectId);
        return ResponseEntity.ok(videos.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<FileAttachmentResponse> getFileById(@PathVariable Long fileId) {
        FileAttachment file = fileAttachmentService.getFileById(fileId);
        return ResponseEntity.ok(FileAttachmentResponse.from(file));
    }

    @PutMapping("/{fileId}/main")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<FileAttachmentResponse> setAsMain(
            @PathVariable Long fileId,
            @RequestParam String contentType,
            @RequestParam Long objectId) {
        FileAttachment file = fileAttachmentService.setAsMain(fileId, contentType, objectId);
        log.info("Файл {} установлен как главный для {}-{}", fileId, contentType, objectId);
        return ResponseEntity.ok(FileAttachmentResponse.from(file));
    }

    @PutMapping("/reorder")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> reorderFiles(
            @RequestBody List<Long> fileIds,
            @RequestParam String contentType,
            @RequestParam Long objectId) {
        fileAttachmentService.updateSortOrder(fileIds, contentType, objectId);
        log.info("Изменен порядок файлов для {}-{}", contentType, objectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        fileAttachmentService.deleteFile(fileId);
        log.info("Удален файл {}", fileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{contentType}/{objectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteAllFiles(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        fileAttachmentService.deleteAllFilesForEntity(contentType, objectId);
        log.info("Удалены все файлы для {}-{}", contentType, objectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/copy")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<FileAttachmentResponse>> copyFiles(
            @RequestParam String sourceType,
            @RequestParam Long sourceId,
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        List<FileAttachment> copied = fileAttachmentService.copyFiles(sourceType, sourceId, targetType, targetId);
        log.info("Скопировано {} файлов из {}-{} в {}-{}", copied.size(), sourceType, sourceId, targetType, targetId);
        return ResponseEntity.ok(copied.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FileAttachmentResponse>> searchByFilename(@RequestParam String filename) {
        List<FileAttachment> files = fileAttachmentService.searchByFilename(filename);
        return ResponseEntity.ok(files.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/by-mime-type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FileAttachmentResponse>> findByMimeType(@RequestParam String mimeType) {
        List<FileAttachment> files = fileAttachmentService.findByMimeType(mimeType);
        return ResponseEntity.ok(files.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FileAttachmentResponse>> getRecentlyUploaded(@RequestParam(defaultValue = "7") int days) {
        List<FileAttachment> files = fileAttachmentService.findRecentlyUploaded(days);
        return ResponseEntity.ok(files.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/orphaned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FileAttachmentResponse>> getOrphanedFiles(@RequestParam(defaultValue = "30") int daysOld) {
        List<FileAttachment> files = fileAttachmentService.findOrphanedFiles(java.time.LocalDateTime.now().minusDays(daysOld));
        return ResponseEntity.ok(files.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @DeleteMapping("/orphaned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteOrphanedFiles(@RequestParam(defaultValue = "30") int daysOld) {
        int deleted = fileAttachmentService.deleteOrphanedFiles(daysOld);
        log.info("Удалено {} файлов-сирот", deleted);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getFileStatistics() {
        return ResponseEntity.ok(fileAttachmentService.getFileStatistics());
    }

    @GetMapping("/products/{productId}/images")
    public ResponseEntity<List<FileAttachmentResponse>> getProductImages(@PathVariable Long productId) {
        List<FileAttachment> images = fileAttachmentService.getProductImages(productId);
        return ResponseEntity.ok(images.stream()
                .map(FileAttachmentResponse::from)
                .toList());
    }

    @GetMapping("/products/{productId}/main-image")
    public ResponseEntity<String> getProductMainImage(@PathVariable Long productId) {
        String url = fileAttachmentService.getProductMainImageUrl(productId);
        return url != null ? ResponseEntity.ok(url) : ResponseEntity.notFound().build();
    }

    @PostMapping("/products/{productId}/images")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<FileAttachmentResponse>> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam List<MultipartFile> files) {
        List<FileAttachment> attachments = fileAttachmentService.saveFiles(files, "Product", productId);
        log.info("Загружено {} изображений для товара {}", attachments.size(), productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachments.stream()
                        .map(FileAttachmentResponse::from)
                        .toList());
    }
}