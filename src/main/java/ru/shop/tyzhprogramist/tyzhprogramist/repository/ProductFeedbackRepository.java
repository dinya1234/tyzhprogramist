package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductFeedback;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductFeedbackRepository extends JpaRepository<ProductFeedback, Long> {

    // ===== Базовые методы поиска =====

    /**
     * Найти все отзывы/вопросы для конкретного товара с пагинацией
     */
    Page<ProductFeedback> findByProduct(Product product, Pageable pageable);

    /**
     * Найти все отзывы/вопросы пользователя с пагинацией
     */
    Page<ProductFeedback> findByUser(User user, Pageable pageable);

    /**
     * Найти все опубликованные отзывы/вопросы для товара
     */
    List<ProductFeedback> findByProductAndIsPublishedTrue(Product product);

    /**
     * Найти все отзывы/вопросы для товара по типу (REVIEW или QUESTION)
     */
    Page<ProductFeedback> findByProductAndFeedbackType(Product product, FeedbackType feedbackType, Pageable pageable);

    /**
     * Найти все отзывы/вопросы по типу (REVIEW или QUESTION)
     */
    List<ProductFeedback> findByFeedbackType(FeedbackType feedbackType);

    // ===== Методы с Query =====

    /**
     * Найти средний рейтинг товара (только для REVIEW)
     */
    @Query("SELECT COALESCE(AVG(pf.rating), 0) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.REVIEW " +
            "AND pf.isPublished = true " +
            "AND pf.rating IS NOT NULL")
    Double getAverageRatingByProductId(@Param("productId") Long productId);

    /**
     * Подсчитать количество отзывов с рейтингом для товара (только для REVIEW)
     */
    @Query("SELECT COUNT(pf) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.REVIEW " +
            "AND pf.isPublished = true " +
            "AND pf.rating IS NOT NULL")
    Integer getReviewsCountByProductId(@Param("productId") Long productId);

    /**
     * Подсчитать количество вопросов для товара (только для QUESTION)
     */
    @Query("SELECT COUNT(pf) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.QUESTION " +
            "AND pf.isPublished = true")
    Integer getQuestionsCountByProductId(@Param("productId") Long productId);

    /**
     * Найти все неподтвержденные (неопубликованные) отзывы/вопросы для модерации
     */
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.isPublished = false " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findPendingModeration();

    /**
     * Найти все отзывы/вопросы, на которые еще не дан ответ
     */
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.answer IS NULL OR pf.answer = ''")
    List<ProductFeedback> findUnanswered();

    /**
     * Проверить, оставлял ли пользователь уже отзыв на этот товар (только для REVIEW)
     */
    @Query("SELECT COUNT(pf) > 0 FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.user.id = :userId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.REVIEW")
    boolean hasUserReviewedProduct(@Param("productId") Long productId,
                                   @Param("userId") Long userId);

    /**
     * Проверить, задавал ли пользователь уже вопрос по этому товару (только для QUESTION)
     */
    @Query("SELECT COUNT(pf) > 0 FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.user.id = :userId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.QUESTION")
    boolean hasUserAskedQuestion(@Param("productId") Long productId,
                                 @Param("userId") Long userId);

    /**
     * Найти последние отзывы с высоким рейтингом (только для REVIEW)
     */
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.REVIEW " +
            "AND pf.isPublished = true " +
            "AND pf.rating >= 4 " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findTopRecentReviews(Pageable pageable);

    /**
     * Найти последние вопросы (только для QUESTION)
     */
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.QUESTION " +
            "AND pf.isPublished = true " +
            "ORDER BY pf.createdAt DESC")
    List<ProductFeedback> findRecentQuestions(Pageable pageable);

    /**
     * Найти отзывы для товара с определенным рейтингом (только для REVIEW)
     */
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.REVIEW " +
            "AND pf.isPublished = true " +
            "AND pf.rating = :rating " +
            "ORDER BY pf.createdAt DESC")
    Page<ProductFeedback> findByProductIdAndRating(@Param("productId") Long productId,
                                                   @Param("rating") Integer rating,
                                                   Pageable pageable);

    /**
     * Статистика по рейтингам для товара (сколько отзывов с каждой оценкой)
     */
    @Query("SELECT pf.rating, COUNT(pf) FROM ProductFeedback pf " +
            "WHERE pf.product.id = :productId " +
            "AND pf.feedbackType = ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType.REVIEW " +
            "AND pf.isPublished = true " +
            "AND pf.rating IS NOT NULL " +
            "GROUP BY pf.rating " +
            "ORDER BY pf.rating DESC")
    List<Object[]> getRatingStatistics(@Param("productId") Long productId);

    // ===== Методы удаления =====

    /**
     * Удалить все отзывы/вопросы пользователя
     */
    void deleteByUser(User user);

    /**
     * Удалить все отзывы/вопросы для товара
     */
    void deleteByProduct(Product product);

    // ===== Методы с пагинацией для админки =====

    /**
     * Найти все неподтвержденные отзывы с пагинацией (для админки)
     */
    @Query("SELECT pf FROM ProductFeedback pf " +
            "WHERE pf.isPublished = false " +
            "ORDER BY pf.createdAt DESC")
    Page<ProductFeedback> findPendingModeration(Pageable pageable);

    /**
     * Найти все отзывы по типу с пагинацией (для админки)
     */
    Page<ProductFeedback> findByFeedbackType(FeedbackType feedbackType, Pageable pageable);
}