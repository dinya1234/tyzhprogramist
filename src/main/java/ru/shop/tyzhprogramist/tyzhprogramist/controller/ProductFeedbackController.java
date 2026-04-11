package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreateFeedbackRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFeedbackResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductFeedback;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ProductFeedbackService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class ProductFeedbackController {

    private final ProductFeedbackService feedbackService;
    private final UserService userService;

    private Long getCurrentUserId() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return principal.getId();
    }

    // ========== ПУБЛИЧНЫЕ ЭНДПОИНТЫ ==========

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<PageResponse<ProductFeedbackResponse>> getProductReviews(
            @PathVariable Long productId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductFeedback> page = feedbackService.getProductReviews(productId, pageable);
        Page<ProductFeedbackResponse> responsePage = feedbackService.toResponsePage(page);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/products/{productId}/questions")
    public ResponseEntity<PageResponse<ProductFeedbackResponse>> getProductQuestions(
            @PathVariable Long productId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductFeedback> page = feedbackService.getProductQuestions(productId, pageable);
        Page<ProductFeedbackResponse> responsePage = feedbackService.toResponsePage(page);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/products/{productId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        Double rating = feedbackService.getAverageRating(productId);
        return ResponseEntity.ok(rating != null ? rating : 0.0);
    }

    @GetMapping("/products/{productId}/rating-stats")
    public ResponseEntity<Map<Integer, Long>> getRatingStatistics(@PathVariable Long productId) {
        return ResponseEntity.ok(feedbackService.getRatingStatistics(productId));
    }

    // ========== АВТОРИЗОВАННЫЙ ПОЛЬЗОВАТЕЛЬ ==========

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductFeedbackResponse> createFeedback(@Valid @RequestBody CreateFeedbackRequest request) {
        Long userId = getCurrentUserId();
        var user = userService.getById(userId);
        var feedback = feedbackService.createFeedback(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedbackService.getResponseById(feedback.getId()));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ProductFeedbackResponse>> getMyFeedbacks(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = getCurrentUserId();
        Page<ProductFeedback> page = feedbackService.getUserFeedbacks(userId, pageable);
        Page<ProductFeedbackResponse> responsePage = feedbackService.toResponsePage(page);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMyFeedback(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        var user = userService.getById(userId);
        feedbackService.deleteFeedback(id, user);
        return ResponseEntity.noContent().build();
    }

    // ========== МОДЕРАТОР / АДМИН ==========

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<PageResponse<ProductFeedbackResponse>> getPendingModeration(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductFeedback> page = feedbackService.getPendingModeration(pageable);
        Page<ProductFeedbackResponse> responsePage = feedbackService.toResponsePage(page);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/unanswered")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<PageResponse<ProductFeedbackResponse>> getUnansweredQuestions(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductFeedback> page = feedbackService.getUnansweredQuestions(pageable);
        Page<ProductFeedbackResponse> responsePage = feedbackService.toResponsePage(page);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<ProductFeedbackResponse> publish(@PathVariable Long id) {
        var feedback = feedbackService.publish(id);
        return ResponseEntity.ok(feedbackService.getResponseById(feedback.getId()));
    }

    @PostMapping("/{id}/answer")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<ProductFeedbackResponse> answerQuestion(@PathVariable Long id, @RequestParam String answer) {
        var feedback = feedbackService.answerQuestion(id, answer);
        return ResponseEntity.ok(feedbackService.getResponseById(feedback.getId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.reject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ProductFeedbackResponse>> bulkPublish(@RequestBody List<Long> feedbackIds) {
        List<ProductFeedback> feedbacks = feedbackService.bulkPublish(feedbackIds);
        List<ProductFeedbackResponse> responses = new ArrayList<>();
        for (ProductFeedback feedback : feedbacks) {
            responses.add(ProductFeedbackResponse.from(feedback));
        }
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/bulk/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> bulkReject(@RequestBody List<Long> feedbackIds) {
        feedbackService.bulkReject(feedbackIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(feedbackService.getOverallStatistics());
    }
}