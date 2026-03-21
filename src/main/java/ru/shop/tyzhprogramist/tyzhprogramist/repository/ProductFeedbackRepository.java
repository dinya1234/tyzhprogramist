package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductFeedback;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductFeedbackRepository extends JpaRepository<ProductFeedback, Long> {

    Page<ProductFeedback> findByProduct(Product product, Pageable pageable);

    Page<ProductFeedback> findByProductId(Long productId, Pageable pageable);

    Page<ProductFeedback> findByUser(User user, Pageable pageable);

    Page<ProductFeedback> findByUserId(Long userId, Pageable pageable);

    List<ProductFeedback> findByProductAndIsPublishedTrue(Product product);

    List<ProductFeedback> findByProductIdAndIsPublishedTrue(Long productId);

    Page<ProductFeedback> findByProductAndFeedbackType(Product product, FeedbackType feedbackType, Pageable pageable);

    default Page<ProductFeedback> findQuestionsByProduct(Product product, Pageable pageable) {
        return findByProductAndFeedbackType(product, FeedbackType.QUESTION, pageable);
    }

    default Page<ProductFeedback> findReviewsByProduct(Product product, Pageable pageable) {
        return findByProductAndFeedbackType(product, FeedbackType.REVIEW, pageable);
    }

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = 'QUESTION' " +
            "AND (pf.answer IS NULL OR pf.answer = '') " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findUnansweredQuestions(@Param("productId") Long productId);

    Page<ProductFeedback> findByUserAndFeedbackType(User user, FeedbackType feedbackType, Pageable pageable);

    @Query("SELECT COALESCE(AVG(pf.rating), 0) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = 'REVIEW' " +
            "AND pf.isPublished = true " +
            "AND pf.rating IS NOT NULL")
    Double getAverageRatingByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(pf) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = 'REVIEW' " +
            "AND pf.isPublished = true " +
            "AND pf.rating IS NOT NULL")
    Integer getReviewsCountByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(pf) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = 'QUESTION' " +
            "AND pf.isPublished = true")
    Integer getQuestionsCountByProductId(@Param("productId") Long productId);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = 'REVIEW' " +
            "AND pf.isPublished = true " +
            "AND pf.rating = :rating " +
            "ORDER BY pf.createdAt DESC")
    Page<ProductFeedback> findByProductIdAndRating(@Param("productId") Long productId,
                                                   @Param("rating") Integer rating,
                                                   Pageable pageable);

    @Query("SELECT pf.rating, COUNT(pf) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = 'REVIEW' " +
            "AND pf.isPublished = true " +
            "AND pf.rating IS NOT NULL " +
            "GROUP BY pf.rating " +
            "ORDER BY pf.rating DESC")
    List<Object[]> getRatingStatistics(@Param("productId") Long productId);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.feedbackType = 'REVIEW' " +
            "AND pf.isPublished = true " +
            "AND pf.rating >= 4 " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findTopRecentReviews(Pageable pageable);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.isPublished = false " +
            "ORDER BY pf.createdAt ASC")
    List<ProductFeedback> findPendingModeration();

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.isPublished = false " +
            "ORDER BY pf.createdAt ASC")
    Page<ProductFeedback> findPendingModeration(Pageable pageable);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE (pf.answer IS NULL OR pf.answer = '') " +
            "AND pf.isPublished = true " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findUnanswered();

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE (pf.answer IS NULL OR pf.answer = '') " +
            "AND pf.isPublished = true " +
            "ORDER BY pf.createdAt DESC")
    Page<ProductFeedback> findUnanswered(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ProductFeedback pf SET pf.isPublished = true WHERE pf.id = :feedbackId")
    int publish(@Param("feedbackId") Long feedbackId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductFeedback pf WHERE pf.id = :feedbackId AND pf.isPublished = false")
    int reject(@Param("feedbackId") Long feedbackId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductFeedback pf SET pf.answer = :answer WHERE pf.id = :feedbackId")
    int addAnswer(@Param("feedbackId") Long feedbackId, @Param("answer") String answer);

    @Query("SELECT pf FROM ProductFeedback pf WHERE pf.answer IS NOT NULL AND pf.answer != ''")
    List<ProductFeedback> findAnsweredFeedbacks();

    @Query("SELECT COUNT(pf) > 0 FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.user.id = :userId " +
            "AND pf.feedbackType = 'REVIEW'")
    boolean hasUserReviewedProduct(@Param("productId") Long productId,
                                   @Param("userId") Long userId);

    @Query("SELECT COUNT(pf) > 0 FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.user.id = :userId " +
            "AND pf.feedbackType = 'QUESTION'")
    boolean hasUserAskedQuestion(@Param("productId") Long productId,
                                 @Param("userId") Long userId);

    List<ProductFeedback> findByProductAndUser(Product product, User user);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.createdAt >= :since " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findRecent(@Param("since") LocalDateTime since);

    List<ProductFeedback> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE (pf.answer IS NULL OR pf.answer = '') " +
            "AND pf.createdAt < :date " +
            "ORDER BY pf.createdAt ASC")
    List<ProductFeedback> findStaleUnanswered(@Param("date") LocalDateTime date);

    @Query("SELECT " +
            "COUNT(pf) as total, " +
            "SUM(CASE WHEN pf.feedbackType = 'REVIEW' THEN 1 ELSE 0 END) as totalReviews, " +
            "SUM(CASE WHEN pf.feedbackType = 'QUESTION' THEN 1 ELSE 0 END) as totalQuestions, " +
            "SUM(CASE WHEN pf.isPublished = false THEN 1 ELSE 0 END) as pendingModeration, " +
            "SUM(CASE WHEN pf.answer IS NULL OR pf.answer = '' THEN 1 ELSE 0 END) as unanswered " +
            "FROM ProductFeedback pf")
    Object[] getOverallStatistics();

    @Query("SELECT " +
            "COUNT(pf) as total, " +
            "SUM(CASE WHEN pf.feedbackType = 'REVIEW' THEN 1 ELSE 0 END) as reviews, " +
            "SUM(CASE WHEN pf.feedbackType = 'QUESTION' THEN 1 ELSE 0 END) as questions, " +
            "AVG(CASE WHEN pf.feedbackType = 'REVIEW' AND pf.rating IS NOT NULL THEN pf.rating ELSE null END) as avgRating " +
            "FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId AND pf.isPublished = true")
    Object[] getProductStatistics(@Param("productId") Long productId);

    @Query("SELECT DATE(pf.createdAt), " +
            "COUNT(pf), " +
            "SUM(CASE WHEN pf.feedbackType = 'REVIEW' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN pf.feedbackType = 'QUESTION' THEN 1 ELSE 0 END) " +
            "FROM ProductFeedback pf " +
            "WHERE pf.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(pf.createdAt) " +
            "ORDER BY DATE(pf.createdAt) DESC")
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE LOWER(pf.text) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY pf.createdAt DESC")
    Page<ProductFeedback> searchByText(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE LOWER(pf.answer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY pf.createdAt DESC")
    Page<ProductFeedback> searchByAnswer(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    void deleteByProduct(Product product);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductFeedback pf " +
            "WHERE pf.isPublished = false AND pf.createdAt < :date")
    int deleteOldPending(@Param("date") LocalDateTime date);

    Page<ProductFeedback> findByProductIdAndFeedbackTypeAndIsPublishedTrueOrderByCreatedAtDesc(
            Long productId, FeedbackType feedbackType, Pageable pageable);

    Page<ProductFeedback> findByUserIdAndFeedbackTypeOrderByCreatedAtDesc(
            Long userId, FeedbackType feedbackType, Pageable pageable);

    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findForExport(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}