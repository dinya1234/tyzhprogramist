package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FileAttachment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc(
            String contentType, Long objectId);

    List<FileAttachment> findByContentTypeAndObjectIdOrderByUploadedAtDesc(
            String contentType, Long objectId);

    List<FileAttachment> findByContentTypeAndObjectIdInOrderBySortOrderAsc(
            String contentType, List<Long> objectIds);

    Optional<FileAttachment> findByContentTypeAndObjectIdAndIsMainTrue(
            String contentType, Long objectId);


    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType " +
            "AND fa.objectId IN :objectIds " +
            "AND fa.isMain = true")
    List<FileAttachment> findMainImagesForEntities(
            @Param("contentType") String contentType,
            @Param("objectIds") List<Long> objectIds);

    default List<FileAttachment> findProductImages(Long productId) {
        return findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc("Product", productId);
    }

    default Optional<FileAttachment> findProductMainImage(Long productId) {
        return findByContentTypeAndObjectIdAndIsMainTrue("Product", productId);
    }

    default List<FileAttachment> findReviewImages(Long reviewId) {
        return findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc("Review", reviewId);
    }

    default List<FileAttachment> findRepairRequestAttachments(Long repairRequestId) {
        return findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc("RepairRequest", repairRequestId);
    }

    default List<FileAttachment> findChatMessageAttachments(Long chatMessageId) {
        return findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc("ChatMessage", chatMessageId);
    }

    default List<FileAttachment> findPcBuildImages(Long pcBuildId) {
        return findByContentTypeAndObjectIdOrderBySortOrderAscUploadedAtAsc("PcBuild", pcBuildId);
    }

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType " +
            "AND fa.objectId = :objectId " +
            "AND fa.mimeType LIKE 'image/%' " +
            "ORDER BY fa.isMain DESC, fa.sortOrder ASC")
    List<FileAttachment> findImagesByEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType " +
            "AND fa.objectId = :objectId " +
            "AND fa.mimeType IN ('application/pdf', 'application/msword', " +
            "'application/vnd.openxmlformats-officedocument.wordprocessingml.document')")
    List<FileAttachment> findDocumentsByEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType " +
            "AND fa.objectId = :objectId " +
            "AND fa.mimeType LIKE 'video/%'")
    List<FileAttachment> findVideosByEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);

    List<FileAttachment> findByFileSizeGreaterThan(Long size);

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.uploadedAt >= :since " +
            "ORDER BY fa.uploadedAt DESC")
    List<FileAttachment> findRecentlyUploaded(@Param("since") LocalDateTime since);


    List<FileAttachment> findByUploadedAtBetween(
            LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.uploadedAt < :dateThreshold " +
            "ORDER BY fa.uploadedAt DESC")
    List<FileAttachment> findOldFiles(@Param("dateThreshold") LocalDateTime dateThreshold);


    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.isMain = false " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId")
    int resetMainFlagForEntity(@Param("contentType") String contentType,
                               @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.isMain = true " +
            "WHERE fa.id = :fileId AND fa.contentType = :contentType AND fa.objectId = :objectId")
    int setAsMain(@Param("fileId") Long fileId,
                  @Param("contentType") String contentType,
                  @Param("objectId") Long objectId);

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId AND fa.isMain = true")
    Optional<FileAttachment> findMainImage(@Param("contentType") String contentType,
                                           @Param("objectId") Long objectId);


    @Query("SELECT COALESCE(MAX(fa.sortOrder), -1) FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId")
    int getMaxSortOrder(@Param("contentType") String contentType,
                        @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.sortOrder = :newOrder WHERE fa.id = :fileId")
    int updateSortOrder(@Param("fileId") Long fileId, @Param("newOrder") Integer newOrder);


    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.sortOrder = fa.sortOrder + :delta " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId " +
            "AND fa.sortOrder >= :startFrom")
    int shiftSortOrders(@Param("contentType") String contentType,
                        @Param("objectId") Long objectId,
                        @Param("startFrom") Integer startFrom,
                        @Param("delta") Integer delta);


    long countByContentTypeAndObjectId(String contentType, Long objectId);

    @Query("SELECT COUNT(fa) FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId " +
            "AND fa.mimeType LIKE 'image/%'")
    long countImagesByEntity(@Param("contentType") String contentType,
                             @Param("objectId") Long objectId);

    List<FileAttachment> findByOriginalFilenameContainingIgnoreCase(String filename);
    Optional<FileAttachment> findByFilePath(String filePath);

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.filePath = :filePath AND fa.id != :fileId")
    List<FileAttachment> findDuplicatesByPath(
            @Param("filePath") String filePath,
            @Param("fileId") Long fileId);

    List<FileAttachment> findByMimeType(String mimeType);
    List<FileAttachment> findByMimeTypeStartingWith(String prefix);

    @Modifying
    @Transactional
    void deleteByContentTypeAndObjectId(String contentType, Long objectId);

    @Modifying
    @Transactional
    void deleteByContentTypeAndObjectIdIn(String contentType, List<Long> objectIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId AND fa.isMain = false")
    int deleteNonMainFiles(@Param("contentType") String contentType,
                           @Param("objectId") Long objectId);


    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.uploadedAt < :dateThreshold " +
            "AND (fa.contentType, fa.objectId) NOT IN " +
            "(SELECT 'Product', p.id FROM Product p WHERE p.isActive = true)")
    List<FileAttachment> findOrphanedFiles(@Param("dateThreshold") LocalDateTime dateThreshold);

    @Query("SELECT fa.mimeType, COUNT(fa), SUM(fa.fileSize) FROM FileAttachment fa " +
            "GROUP BY fa.mimeType " +
            "ORDER BY COUNT(fa) DESC")
    List<Object[]> getFileTypeStatistics();

    @Query("SELECT COALESCE(SUM(fa.fileSize), 0) FROM FileAttachment fa")
    long getTotalFileSize();

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType AND fa.objectId = :objectId " +
            "ORDER BY fa.isMain DESC, fa.sortOrder ASC, fa.uploadedAt DESC")
    List<FileAttachment> findByEntityWithPagination(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId,
            Pageable pageable);

    boolean existsByFilePath(String filePath);
    boolean existsByContentTypeAndObjectIdAndIsMainTrue(String contentType, Long objectId);
    boolean existsByContentTypeAndObjectId(String contentType, Long objectId);
}