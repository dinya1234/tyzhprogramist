package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.ChatMessageRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ChatMessageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ChatSessionResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatMessage;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatSession;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ChatService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    private Long getCurrentUserId() {
        try {
            SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return principal != null ? principal.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getGuestId(HttpServletRequest request) {
        String guestId = request.getHeader("X-Guest-Id");
        if (guestId == null || guestId.isEmpty()) {
            guestId = request.getSession(true).getId();
        }
        return guestId;
    }

    @PostMapping("/session")
    public ResponseEntity<ChatSessionResponse> createSession(
            @RequestAttribute(required = false) Long userId,
            HttpServletRequest request,
            @RequestParam(required = false) String sourceUrl,
            @RequestParam(required = false) String contextType,
            @RequestParam(required = false) Long contextId) {

        ChatSession session;

        if (userId != null) {
            User user = userService.getById(userId);
            if (contextType != null && contextId != null) {
                session = chatService.createSessionWithContext(user, sourceUrl, contextType, contextId);
            } else {
                session = chatService.createSession(user, sourceUrl);
            }
        } else {
            session = chatService.createGuestSession(sourceUrl);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.getSessionResponseById(session.getId()));
    }

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ChatMessageResponse> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        ChatMessage message = chatService.sendUserMessage(request.getSessionId(), request.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(ChatMessageResponse.from(message));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ChatSessionResponse> getSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.getSessionResponseById(sessionId));
    }

    @GetMapping("/session/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.getSessionMessageResponses(sessionId));
    }

    @GetMapping("/session/{sessionId}/messages/latest")
    public ResponseEntity<List<ChatMessageResponse>> getLatestMessages(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "20") int limit) {
        List<ChatMessage> messages = chatService.getLastMessages(sessionId, limit);
        return ResponseEntity.ok(messages.stream()
                .map(ChatMessageResponse::from)
                .toList());
    }

    @GetMapping("/me/sessions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ChatSessionResponse>> getMySessions(
            @RequestAttribute Long userId,
            @PageableDefault(size = 10, sort = "startedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ChatSession> page = chatService.getUserSessionsByUserId(userId, pageable);
        return ResponseEntity.ok(PageResponse.from(chatService.toResponsePage(page)));
    }

    @PostMapping("/me/session/{sessionId}/close")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatSessionResponse> closeMySession(
            @RequestAttribute Long userId,
            @PathVariable Long sessionId) {
        if (!chatService.canUserAccessSession(userId, sessionId)) {
            return ResponseEntity.notFound().build();
        }
        ChatSession session = chatService.closeSession(sessionId);
        return ResponseEntity.ok(chatService.getSessionResponseById(session.getId()));
    }

    @GetMapping("/consultant/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ChatSessionResponse>> getPendingSessions() {
        List<ChatSession> sessions = chatService.getPendingSessions();
        return ResponseEntity.ok(chatService.toResponseList(sessions));
    }

    @PostMapping("/consultant/session/{sessionId}/take")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<ChatSessionResponse> takeSession(
            @RequestAttribute Long userId,
            @PathVariable Long sessionId) {
        User consultant = userService.getById(userId);
        ChatSession session = chatService.assignConsultant(sessionId, consultant);
        return ResponseEntity.ok(chatService.getSessionResponseById(session.getId()));
    }

    @PostMapping("/consultant/session/{sessionId}/message")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<ChatMessageResponse> sendConsultantMessage(
            @PathVariable Long sessionId,
            @RequestParam String message) {
        ChatMessage chatMessage = chatService.sendConsultantMessage(sessionId, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(ChatMessageResponse.from(chatMessage));
    }

    @GetMapping("/consultant/sessions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<PageResponse<ChatSessionResponse>> getConsultantSessions(
            @RequestAttribute Long userId,
            @PageableDefault(size = 20, sort = "startedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ChatSession> page = chatService.getConsultantSessionsByUserId(userId, pageable);
        return ResponseEntity.ok(PageResponse.from(chatService.toResponsePage(page)));
    }

    @GetMapping("/consultant/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ChatSessionResponse>> getActiveConsultantSessions(@RequestAttribute Long userId) {
        List<ChatSession> sessions = chatService.getActiveConsultantSessions(userId);
        return ResponseEntity.ok(chatService.toResponseList(sessions));
    }

    @PostMapping("/consultant/session/{sessionId}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<ChatSessionResponse> closeSessionAsConsultant(@PathVariable Long sessionId) {
        ChatSession session = chatService.closeSession(sessionId);
        return ResponseEntity.ok(chatService.getSessionResponseById(session.getId()));
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<ChatSessionResponse>> getAllSessions(
            @PageableDefault(size = 20, sort = "startedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ChatSession> page = chatService.getAllSessions(pageable);
        return ResponseEntity.ok(PageResponse.from(chatService.toResponsePage(page)));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getChatStatistics() {
        return ResponseEntity.ok(chatService.getChatStatistics());
    }

    @GetMapping("/load")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCurrentLoad() {
        return ResponseEntity.ok(chatService.getCurrentLoad());
    }

    @GetMapping("/consultants/load")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getConsultantsByLoad(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(chatService.getConsultantsByLoad(limit));
    }
}