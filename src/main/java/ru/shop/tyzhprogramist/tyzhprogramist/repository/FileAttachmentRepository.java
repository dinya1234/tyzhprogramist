package ru.shop.tyzhprogramist.tyzhprogramist.repository;

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

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType " +
            "AND fa.objectId = :objectId " +
            "AND fa.mimeType LIKE :mimeTypePattern")
    List<FileAttachment> findByContentTypeAndObjectIdAndMimeTypeLike(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId,
            @Param("mimeTypePattern") String mimeTypePattern);


    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.contentType = :contentType " +
            "AND fa.objectId = :objectId " +
            "AND fa.mimeType LIKE 'image/%' " +
            "ORDER BY fa.isMain DESC, fa.sortOrder ASC")
    List<FileAttachment> findImagesByEntity(
            @Param("contentType") String contentType,
            @Param("objectId") Long objectId);


    @Query("SELECT COUNT(fa) FROM FileAttachment fa " + "WHERE fa.contentType = :contentType AND fa.objectId = :objectId")
    long countByEntity(@Param("contentType") String contentType,
                       @Param("objectId") Long objectId);

    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.uploadedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY fa.uploadedAt DESC")
    List<FileAttachment> findByUploadedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.isMain = false " + "WHERE fa.contentType = :contentType AND fa.objectId = :objectId")
    void resetMainFlagForEntity(@Param("contentType") String contentType,
                                @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.isMain = true " + "WHERE fa.id = :fileId AND fa.contentType = :contentType AND fa.objectId = :objectId")
    int setAsMain(@Param("fileId") Long fileId,
                  @Param("contentType") String contentType,
                  @Param("objectId") Long objectId);


    @Modifying
    @Transactional
    @Query("UPDATE FileAttachment fa SET fa.sortOrder = :newOrder " +
            "WHERE fa.id = :fileId")
    int updateSortOrder(@Param("fileId") Long fileId, @Param("newOrder") Integer newOrder);

    @Query("SELECT COALESCE(MAX(fa.sortOrder), -1) FROM FileAttachment fa " + "WHERE fa.contentType = :contentType AND fa.objectId = :objectId")
    int getMaxSortOrder(@Param("contentType") String contentType,
                        @Param("objectId") Long objectId);

    @Modifying
    @Transactional
    void deleteByContentTypeAndObjectId(String contentType, Long objectId);

    @Modifying
    @Transactional
    void deleteByContentTypeAndObjectIdIn(String contentType, List<Long> objectIds);

    @Query("SELECT fa FROM FileAttachment fa " + "WHERE fa.filePath = :filePath AND fa.id != :fileId")
    List<FileAttachment> findDuplicatesByPath(
            @Param("filePath") String filePath,
            @Param("fileId") Long fileId);


    @Query("SELECT fa FROM FileAttachment fa " +
            "WHERE fa.uploadedAt < :dateThreshold " +
            "ORDER BY fa.uploadedAt DESC")
    List<FileAttachment> findOldFiles(@Param("dateThreshold") LocalDateTime dateThreshold);

    List<FileAttachment> findByContentType(String contentType);
}
