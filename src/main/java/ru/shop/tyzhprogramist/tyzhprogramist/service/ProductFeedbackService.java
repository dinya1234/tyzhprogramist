package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreateFeedbackRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFeedbackResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.FeedbackType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductFeedback;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.UserRole;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductFeedbackRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFeedbackService {

    private final ProductFeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public ProductFeedback getById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Отзыв/вопрос не найден с id: " + id));
    }

    @Transactional(readOnly = true)
    public ProductFeedbackResponse getResponseById(Long id) {
        return ProductFeedbackResponse.from(getById(id));
    }

    @Transactional
    public ProductFeedback createReview(User user, Long productId, String text, Integer rating) {
        Product product = productService.getById(productId);

        if (feedbackRepository.hasUserReviewedProduct(productId, user.getId())) {
            throw new BadRequestException("Вы уже оставили отзыв на этот товар");
        }

        if (rating == null || rating < 1 || rating > 5) {
            throw new BadRequestException("Рейтинг должен быть от 1 до 5");
        }

        ProductFeedback feedback = new ProductFeedback(
                product,
                user,
                FeedbackType.REVIEW,
                text,
                rating
        );

        ProductFeedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Пользователь {} оставил отзыв на товар {} с рейтингом {}",
                user.getUsername(), productId, rating);

        return savedFeedback;
    }

    @Transactional
    public ProductFeedback createQuestion(User user, Long productId, String text) {
        Product product = productService.getById(productId);

        ProductFeedback feedback = new ProductFeedback(
                product,
                user,
                FeedbackType.QUESTION,
                text,
                null
        );

        ProductFeedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Пользователь {} задал вопрос о товаре {}", user.getUsername(), productId);

        return savedFeedback;
    }

    @Transactional
    public ProductFeedback createFeedback(User user, CreateFeedbackRequest request) {
        if (request.getIsQuestion() != null && request.getIsQuestion()) {
            return createQuestion(user, request.getProductId(), request.getText());
        } else {
            return createReview(user, request.getProductId(), request.getText(), request.getRating());
        }
    }

    @Transactional
    public ProductFeedback answerQuestion(Long questionId, String answer) {
        ProductFeedback question = getById(questionId);

        if (question.getFeedbackType() != FeedbackType.QUESTION) {
            throw new BadRequestException("Это не вопрос");
        }

        if (answer == null || answer.trim().isEmpty()) {
            throw new BadRequestException("Ответ не может быть пустым");
        }

        feedbackRepository.addAnswer(questionId, answer);

        ProductFeedback updatedQuestion = getById(questionId);
        log.info("Дан ответ на вопрос {}", questionId);

        return updatedQuestion;
    }

    @Transactional
    public ProductFeedback publish(Long feedbackId) {
        ProductFeedback feedback = getById(feedbackId);

        if (feedback.getIsPublished()) {
            throw new BadRequestException("Отзыв/вопрос уже опубликован");
        }

        int updated = feedbackRepository.publish(feedbackId);
        if (updated == 0) {
            throw new BadRequestException("Не удалось опубликовать отзыв/вопрос");
        }

        ProductFeedback publishedFeedback = getById(feedbackId);
        log.info("Опубликован отзыв/вопрос {}", feedbackId);

        return publishedFeedback;
    }

    @Transactional
    public void reject(Long feedbackId) {
        ProductFeedback feedback = getById(feedbackId);

        if (feedback.getIsPublished()) {
            throw new BadRequestException("Нельзя отклонить опубликованный отзыв/вопрос");
        }

        int deleted = feedbackRepository.reject(feedbackId);
        if (deleted == 0) {
            throw new BadRequestException("Не удалось отклонить отзыв/вопрос");
        }

        log.info("Отклонен (удален) отзыв/вопрос {}", feedbackId);
    }

    @Transactional
    public List<ProductFeedback> bulkPublish(List<Long> feedbackIds) {
        return feedbackIds.stream()
                .map(this::publish)
                .collect(Collectors.toList());
    }

    @Transactional
    public void bulkReject(List<Long> feedbackIds) {
        feedbackIds.forEach(this::reject);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getProductReviews(Long productId, Pageable pageable) {
        return feedbackRepository.findByProductIdAndFeedbackTypeAndIsPublishedTrueOrderByCreatedAtDesc(
                productId, FeedbackType.REVIEW, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getProductQuestions(Long productId, Pageable pageable) {
        return feedbackRepository.findByProductIdAndFeedbackTypeAndIsPublishedTrueOrderByCreatedAtDesc(
                productId, FeedbackType.QUESTION, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getUserFeedbacks(Long userId, Pageable pageable) {
        return feedbackRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getUserReviews(Long userId, Pageable pageable) {
        return feedbackRepository.findByUserIdAndFeedbackTypeOrderByCreatedAtDesc(
                userId, FeedbackType.REVIEW, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getUserQuestions(Long userId, Pageable pageable) {
        return feedbackRepository.findByUserIdAndFeedbackTypeOrderByCreatedAtDesc(
                userId, FeedbackType.QUESTION, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getPendingModeration(Pageable pageable) {
        return feedbackRepository.findPendingModeration(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> getUnansweredQuestions(Pageable pageable) {
        return feedbackRepository.findUnanswered(pageable);
    }

    @Transactional(readOnly = true)
    public List<ProductFeedback> getUnansweredQuestionsForProduct(Long productId) {
        return feedbackRepository.findUnansweredQuestions(productId);
    }

    @Transactional(readOnly = true)
    public Double getAverageRating(Long productId) {
        return feedbackRepository.getAverageRatingByProductId(productId);
    }

    @Transactional(readOnly = true)
    public Integer getReviewsCount(Long productId) {
        return feedbackRepository.getReviewsCountByProductId(productId);
    }

    @Transactional(readOnly = true)
    public Integer getQuestionsCount(Long productId) {
        return feedbackRepository.getQuestionsCountByProductId(productId);
    }

    @Transactional(readOnly = true)
    public Map<Integer, Long> getRatingStatistics(Long productId) {
        List<Object[]> stats = feedbackRepository.getRatingStatistics(productId);
        return stats.stream()
                .collect(Collectors.toMap(
                        arr -> (Integer) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getOverallStatistics() {
        Object[] stats = feedbackRepository.getOverallStatistics();

        return Map.of(
                "total", stats[0],
                "totalReviews", stats[1],
                "totalQuestions", stats[2],
                "pendingModeration", stats[3],
                "unanswered", stats[4]
        );
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return feedbackRepository.getDailyStatistics(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<ProductFeedback> getTopRecentReviews(int limit) {
        return feedbackRepository.findTopRecentReviews(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> searchByText(String searchTerm, Pageable pageable) {
        return feedbackRepository.searchByText(searchTerm, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductFeedback> searchByAnswer(String searchTerm, Pageable pageable) {
        return feedbackRepository.searchByAnswer(searchTerm, pageable);
    }

    @Transactional
    public void updateProductAverageRating(Long productId) {
        Double averageRating = getAverageRating(productId);
        productService.updateProductRating(productId, averageRating != null ? averageRating : 0.0);
        log.debug("Обновлен средний рейтинг товара {}: {}", productId, averageRating);
    }

    @Transactional(readOnly = true)
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return feedbackRepository.hasUserReviewedProduct(productId, userId);
    }

    @Transactional(readOnly = true)
    public boolean hasUserAskedQuestion(Long userId, Long productId) {
        return feedbackRepository.hasUserAskedQuestion(productId, userId);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return feedbackRepository.existsById(id);
    }

    @Transactional
    public void deleteFeedback(Long feedbackId, User requester) {
        ProductFeedback feedback = getById(feedbackId);

        if (!requester.getRole().equals(UserRole.ADMIN) &&
                !requester.getRole().equals(UserRole.MODERATOR) &&
                !feedback.getUser().getId().equals(requester.getId())) {
            throw new BadRequestException("Нет прав на удаление этого отзыва/вопроса");
        }

        feedbackRepository.delete(feedback);
        log.info("Удален отзыв/вопрос {} пользователем {}", feedbackId, requester.getUsername());
    }

    @Transactional
    public void deleteUserFeedbacks(User user) {
        feedbackRepository.deleteByUser(user);
        log.info("Удалены все отзывы пользователя {}", user.getUsername());
    }

    @Transactional
    public void deleteProductFeedbacks(Long productId) {
        Product product = productService.getById(productId);
        feedbackRepository.deleteByProduct(product);
        log.info("Удалены все отзывы о товаре {}", productId);
    }

    @Transactional
    public int deleteOldPendingFeedbacks(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = feedbackRepository.deleteOldPending(threshold);
        log.info("Удалено {} старых неопубликованных отзывов", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<ProductFeedback> exportFeedbacks(LocalDateTime startDate, LocalDateTime endDate) {
        return feedbackRepository.findForExport(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public String getProductFeedbackReport(Long productId) {
        Product product = productService.getById(productId);
        Double avgRating = getAverageRating(productId);
        Integer reviewsCount = getReviewsCount(productId);
        Integer questionsCount = getQuestionsCount(productId);
        Map<Integer, Long> ratingStats = getRatingStatistics(productId);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Отчет по отзывам на товар: ").append(product.getName()).append(" ===\n");
        sb.append("Средний рейтинг: ").append(String.format("%.2f", avgRating != null ? avgRating : 0.0)).append("/5\n");
        sb.append("Всего отзывов: ").append(reviewsCount).append("\n");
        sb.append("Всего вопросов: ").append(questionsCount).append("\n");
        sb.append("\nРаспределение по рейтингам:\n");

        for (int i = 5; i >= 1; i--) {
            Long count = ratingStats.getOrDefault(i, 0L);
            sb.append("  ").append(i).append(" звезд: ").append(count).append("\n");
        }

        return sb.toString();
    }

    public Page<ProductFeedbackResponse> toResponsePage(Page<ProductFeedback> feedbacks) {
        return feedbacks.map(ProductFeedbackResponse::from);
    }

    public List<ProductFeedbackResponse> toResponseList(List<ProductFeedback> feedbacks) {
        return feedbacks.stream()
                .map(ProductFeedbackResponse::from)
                .collect(Collectors.toList());
    }
}