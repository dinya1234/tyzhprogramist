package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.ChatMessageRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ChatMessageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ChatSessionResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.*;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ChatMessageRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ChatSessionRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public ChatSession createSession(User user, String sourceUrl) {
        if (chatSessionRepository.hasActiveSession(user.getId())) {
            ChatSession activeSession = chatSessionRepository.findActiveUserSession(user.getId())
                    .orElse(null);
            if (activeSession != null) {
                throw new BadRequestException("У вас уже есть активная сессия чата");
            }
        }

        ChatSession session = new ChatSession(user, sourceUrl);
        ChatSession savedSession = chatSessionRepository.save(session);

        sendSystemMessage(savedSession.getId(), "Чат начат. Ожидайте ответа консультанта.");

        log.info("Создана новая сессия чата для пользователя: {}", user.getUsername());

        return savedSession;
    }

    @Transactional
    public ChatSession createGuestSession(String sourceUrl) {
        ChatSession session = new ChatSession();
        session.setSourceUrl(sourceUrl);
        session.setStatus(ChatStatus.PENDING);
        session.setStartedAt(LocalDateTime.now());

        ChatSession savedSession = chatSessionRepository.save(session);

        sendSystemMessage(savedSession.getId(), "Чат начат. Ожидайте ответа консультанта.");

        log.info("Создана новая гостевая сессия чата с id: {}", savedSession.getId());

        return savedSession;
    }
    @Transactional(readOnly = true)
    public Page<ChatSession> getAllSessions(Pageable pageable) {
        return chatSessionRepository.findAll(pageable);
    }

    @Transactional
    public ChatSession createSessionWithContext(User user, String sourceUrl,
                                                String contextContentType, Long contextObjectId) {
        ChatSession session = new ChatSession(user, sourceUrl, contextContentType, contextObjectId);
        ChatSession savedSession = chatSessionRepository.save(session);

        String contextMessage = String.format(
                "Чат начат. Контекст: %s ID: %d. Ожидайте ответа консультанта.",
                contextContentType, contextObjectId
        );
        sendSystemMessage(savedSession.getId(), contextMessage);

        log.info("Создана контекстная сессия чата для пользователя: {}, контекст: {}-{}",
                user.getUsername(), contextContentType, contextObjectId);

        return savedSession;
    }

    @Transactional(readOnly = true)
    public ChatSession getSessionById(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Сессия чата не найдена с id: " + sessionId));
    }

    @Transactional(readOnly = true)
    public ChatSessionResponse getSessionResponseById(Long sessionId) {
        ChatSession session = getSessionById(sessionId);
        return buildSessionResponse(session);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getUserSessions(User user) {
        return chatSessionRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getUserSessions(User user, Pageable pageable) {
        return chatSessionRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getUserSessionsByUserId(Long userId, Pageable pageable) {
        return chatSessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<ChatSession> getActiveUserSession(Long userId) {
        return chatSessionRepository.findActiveUserSession(userId);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveSession(Long userId) {
        return chatSessionRepository.hasActiveSession(userId);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getSessionsByStatus(ChatStatus status) {
        return chatSessionRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getSessionsByStatus(ChatStatus status, Pageable pageable) {
        return chatSessionRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getActiveSessions() {
        return chatSessionRepository.findActiveSessions();
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getPendingSessions() {
        return chatSessionRepository.findPendingSessions();
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getClosedSessions(Pageable pageable) {
        return chatSessionRepository.findClosedSessions(pageable);
    }

    @Transactional
    public ChatSession assignConsultant(Long sessionId, User consultant) {
        int updated = chatSessionRepository.assignConsultant(sessionId, consultant);

        if (updated == 0) {
            throw new BadRequestException("Не удалось назначить консультанта. Сессия не найдена или уже активна.");
        }

        ChatSession session = getSessionById(sessionId);

        String message = String.format("Консультант %s присоединился к чату.", consultant.getUsername());
        sendSystemMessage(sessionId, message);

        log.info("Консультант {} назначен на сессию {}", consultant.getUsername(), sessionId);

        return session;
    }
    @Transactional
    public ChatSession assignConsultantById(Long sessionId, Long consultantId) {
        User consultant = userService.getById(consultantId);
        return assignConsultant(sessionId, consultant);
    }

    @Transactional
    public ChatSession closeSession(Long sessionId) {
        ChatSession session = getSessionById(sessionId);

        if (session.getStatus() == ChatStatus.CLOSED) {
            throw new BadRequestException("Сессия уже закрыта");
        }

        LocalDateTime now = LocalDateTime.now();
        chatSessionRepository.closeSession(sessionId, now);

        sendSystemMessage(sessionId, "Чат закрыт.");

        log.info("Сессия {} закрыта", sessionId);

        return getSessionById(sessionId);
    }

    @Transactional
    public int closeAllConsultantSessions(Long consultantId) {
        LocalDateTime now = LocalDateTime.now();
        int count = chatSessionRepository.closeAllConsultantSessions(consultantId, now);

        log.info("Закрыто {} сессий консультанта {}", count, consultantId);

        return count;
    }

    @Transactional
    public int closeInactiveSessions(int timeoutMinutes) {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(timeoutMinutes);
        LocalDateTime now = LocalDateTime.now();

        int count = chatSessionRepository.closeInactiveSessions(timeout, now);

        log.info("Закрыто {} неактивных сессий (таймаут {} минут)", count, timeoutMinutes);

        return count;
    }

    @Transactional(readOnly = true)
    public Optional<ChatSession> getNextPendingSession() {
        List<ChatSession> sessions = chatSessionRepository.getNextPendingSession(PageRequest.of(0, 1));
        return sessions.isEmpty() ? Optional.empty() : Optional.of(sessions.get(0));
    }

    @Transactional
    public ChatMessage sendUserMessage(Long sessionId, String messageText) {
        ChatSession session = getSessionById(sessionId);

        if (session.getStatus() == ChatStatus.CLOSED) {
            throw new BadRequestException("Нельзя отправить сообщение в закрытый чат");
        }

        ChatMessage message = new ChatMessage(session, SenderType.USER, messageText);
        ChatMessage savedMessage = chatMessageRepository.save(message);

        log.info("Сообщение от пользователя в сессии {}: {}", sessionId, messageText.substring(0, Math.min(20, messageText.length())));

        return savedMessage;
    }

    @Transactional
    public ChatMessage sendConsultantMessage(Long sessionId, String messageText) {
        ChatSession session = getSessionById(sessionId);

        if (session.getStatus() != ChatStatus.ACTIVE) {
            throw new BadRequestException("Нельзя отправить сообщение в неактивный чат");
        }

        ChatMessage message = new ChatMessage(session, SenderType.CONSULTANT, messageText);
        ChatMessage savedMessage = chatMessageRepository.save(message);

        log.info("Сообщение от консультанта в сессии {}: {}", sessionId, messageText.substring(0, Math.min(20, messageText.length())));

        return savedMessage;
    }

    @Transactional
    public ChatMessage sendSystemMessage(Long sessionId, String messageText) {
        ChatSession session = getSessionById(sessionId);

        ChatMessage message = new ChatMessage(session, SenderType.SYSTEM, messageText);
        ChatMessage savedMessage = chatMessageRepository.save(message);

        return savedMessage;
    }

    @Transactional
    public ChatMessage sendMessage(ChatMessageRequest request, SenderType senderType) {
        return sendUserMessage(request.getSessionId(), request.getMessage());
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getSessionMessages(Long sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getSessionMessageResponses(Long sessionId) {
        return getSessionMessages(sessionId).stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ChatMessage> getSessionMessages(Long sessionId, Pageable pageable) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId, pageable);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getLastMessages(Long sessionId, int limit) {
        return chatMessageRepository.findLastMessages(sessionId, PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Optional<ChatMessage> getFirstMessage(Long sessionId) {
        List<ChatMessage> messages = chatMessageRepository.findFirstMessage(sessionId, PageRequest.of(0, 1));
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.get(0));
    }

    @Transactional(readOnly = true)
    public Optional<ChatMessage> getLastMessage(Long sessionId) {
        List<ChatMessage> messages = chatMessageRepository.findLastMessage(sessionId, PageRequest.of(0, 1));
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.get(0));
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getUserMessages(Long sessionId) {
        return chatMessageRepository.findUserMessages(sessionId);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getConsultantMessages(Long sessionId) {
        return chatMessageRepository.findConsultantMessages(sessionId);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getSystemMessages(Long sessionId) {
        return chatMessageRepository.findSystemMessages(sessionId);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessagesBetween(Long sessionId, LocalDateTime start, LocalDateTime end) {
        return chatMessageRepository.findBySessionIdAndTimestampBetween(sessionId, start, end);
    }

    @Transactional(readOnly = true)
    public long countMessagesInSession(Long sessionId) {
        return chatMessageRepository.countBySessionId(sessionId);
    }

    @Transactional(readOnly = true)
    public Map<SenderType, Long> countMessagesBySender(Long sessionId) {
        List<Object[]> results = chatMessageRepository.countMessagesBySender(sessionId);

        Map<SenderType, Long> counts = new EnumMap<>(SenderType.class);
        for (Object[] result : results) {
            counts.put((SenderType) result[0], (Long) result[1]);
        }

        return counts;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> searchInSession(Long sessionId, String searchTerm) {
        return chatMessageRepository.searchInSession(sessionId, searchTerm);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> searchInSessionBySender(Long sessionId, SenderType senderType, String searchTerm) {
        return chatMessageRepository.searchInSessionBySender(sessionId, senderType, searchTerm);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> searchUserMessages(Long userId, String searchTerm, int limit) {
        return chatMessageRepository.searchUserMessages(userId, searchTerm, PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findMessagesWithKeyword(String keyword, int limit) {
        return chatMessageRepository.findMessagesWithKeyword(keyword, PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getConsultantSessions(User consultant) {
        return chatSessionRepository.findByConsultant(consultant);
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getConsultantSessions(User consultant, Pageable pageable) {
        return chatSessionRepository.findByConsultant(consultant, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getConsultantSessionsByUserId(Long consultantId, Pageable pageable) {
        return chatSessionRepository.findByConsultantIdOrderByStartedAtDesc(consultantId, pageable);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getActiveConsultantSessions(Long consultantId) {
        return chatSessionRepository.findActiveConsultantSessions(consultantId);
    }

    @Transactional(readOnly = true)
    public long countActiveConsultantSessions(Long consultantId) {
        return chatSessionRepository.countActiveConsultantSessions(consultantId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getConsultantsByLoad(int limit) {
        return chatSessionRepository.findConsultantsByLoad(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getUnansweredMessages(int limit) {
        return chatMessageRepository.findUnansweredMessages(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getStaleUnansweredMessages(int minutes) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(minutes);
        return chatMessageRepository.findStaleUnansweredMessages(threshold);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getSessionsByContext(String contentType, Long objectId) {
        return chatSessionRepository.findByContext(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public Page<ChatSession> getSessionsByContext(String contentType, Long objectId, Pageable pageable) {
        return chatSessionRepository.findByContextContentTypeAndContextObjectId(contentType, objectId, pageable);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getSessionsBySourceUrl(String url) {
        return chatSessionRepository.findBySourceUrlContaining(url);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getStatusStatistics() {
        return chatSessionRepository.getStatusStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return chatSessionRepository.getDailyStatistics(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getConsultantStatistics() {
        return chatSessionRepository.getConsultantStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getSourceStatistics(int limit) {
        return chatSessionRepository.getSourceStatistics(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<Object[]> getContextStatistics() {
        return chatSessionRepository.getContextStatistics();
    }

    @Transactional(readOnly = true)
    public Object[] getSessionStatistics(Long sessionId) {
        return chatMessageRepository.getSessionStatistics(sessionId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMessageLengthStatistics() {
        return chatMessageRepository.getMessageLengthStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getHourlyActivity() {
        return chatMessageRepository.getHourlyActivity();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMostActiveUsers(int limit) {
        return chatMessageRepository.getMostActiveUsers(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Double getAverageResponseTime() {
        return chatMessageRepository.getAverageResponseTime();
    }

    @Transactional(readOnly = true)
    public Double getAverageResponseTimeMinutes() {
        Double seconds = getAverageResponseTime();
        return seconds != null ? seconds / 60.0 : null;
    }

    @Transactional(readOnly = true)
    public Double getAverageMessagesPerSession() {
        return chatMessageRepository.getAverageMessagesPerSession();
    }

    @Transactional(readOnly = true)
    public Double getAverageSessionDuration() {
        return chatSessionRepository.getAverageSessionDuration();
    }

    @Transactional(readOnly = true)
    public Double getMaxSessionDuration() {
        return chatSessionRepository.getMaxSessionDuration();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCurrentLoad() {
        Object[] load = chatSessionRepository.getCurrentLoad();

        return Map.of(
                "activeSessions", load[0],
                "pendingSessions", load[1],
                "activeConsultants", load[2]
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getChatStatistics() {
        long totalSessions = chatSessionRepository.count();
        long totalMessages = chatMessageRepository.count();
        long activeSessions = chatSessionRepository.getActiveSessionsCount();
        long pendingSessions = chatSessionRepository.getPendingSessionsCount();

        return Map.of(
                "totalSessions", totalSessions,
                "totalMessages", totalMessages,
                "activeSessions", activeSessions,
                "pendingSessions", pendingSessions,
                "avgMessagesPerSession", getAverageMessagesPerSession(),
                "avgResponseTimeMinutes", getAverageResponseTimeMinutes(),
                "avgSessionDuration", getAverageSessionDuration(),
                "statusStats", getStatusStatistics(),
                "hourlyActivity", getHourlyActivity()
        );
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getGuestSessions() {
        return chatSessionRepository.findGuestSessions();
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getGuestSessionsBetween(LocalDateTime start, LocalDateTime end) {
        return chatSessionRepository.findGuestSessionsBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getChatTranscript(Long sessionId) {
        return chatMessageRepository.getChatTranscript(sessionId);
    }

    @Transactional(readOnly = true)
    public List<String> getChatTranscriptFormatted(Long sessionId) {
        return chatMessageRepository.getChatTranscriptFormatted(sessionId);
    }

    @Transactional(readOnly = true)
    public String exportChatAsText(Long sessionId) {
        ChatSession session = getSessionById(sessionId);
        List<ChatMessage> messages = getChatTranscript(sessionId);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Чат #").append(sessionId).append(" ===\n");
        sb.append("Дата: ").append(session.getStartedAt()).append("\n");

        if (session.getUser() != null) {
            sb.append("Пользователь: ").append(session.getUser().getUsername()).append("\n");
        } else {
            sb.append("Пользователь: Гость\n");
        }

        if (session.getConsultant() != null) {
            sb.append("Консультант: ").append(session.getConsultant().getUsername()).append("\n");
        }

        sb.append("Статус: ").append(session.getStatus()).append("\n");
        sb.append("=".repeat(50)).append("\n\n");

        for (ChatMessage msg : messages) {
            String sender = switch (msg.getSenderType()) {
                case USER -> "Клиент";
                case CONSULTANT -> "Консультант";
                case SYSTEM -> "Система";
            };
            sb.append(String.format("[%s] %s: %s\n",
                    msg.getTimestamp().toLocalTime(),
                    sender,
                    msg.getMessage()));
        }

        return sb.toString();
    }

    @Transactional
    public int deleteOldClosedSessions(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = chatSessionRepository.deleteOldClosedSessions(threshold);
        log.info("Удалено {} старых закрытых сессий", count);
        return count;
    }

    @Transactional
    public int deleteOldMessages(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = chatMessageRepository.deleteOldMessages(threshold);
        log.info("Удалено {} старых сообщений", count);
        return count;
    }

    @Transactional
    public int deleteOldSystemMessages(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = chatMessageRepository.deleteOldSystemMessages(threshold);
        log.info("Удалено {} старых системных сообщений", count);
        return count;
    }

    @Transactional
    public int deleteSessionMessages(Long sessionId) {
        int count = chatMessageRepository.deleteBySessionId(sessionId);
        log.info("Удалено {} сообщений сессии {}", count, sessionId);
        return count;
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        deleteSessionMessages(sessionId);
        chatSessionRepository.deleteById(sessionId);
        log.info("Удалена сессия {}", sessionId);
    }

    @Transactional
    public void deleteUserSessions(User user) {
        chatSessionRepository.deleteByUser(user);
        log.info("Удалены все сессии пользователя: {}", user.getUsername());
    }

    @Transactional(readOnly = true)
    public boolean sessionExists(Long sessionId) {
        return chatSessionRepository.existsById(sessionId);
    }

    @Transactional(readOnly = true)
    public boolean hasConsultantReplied(Long sessionId) {
        return chatMessageRepository.hasConsultantReplied(sessionId);
    }

    @Transactional(readOnly = true)
    public boolean canUserAccessSession(Long userId, Long sessionId) {
        ChatSession session = getSessionById(sessionId);
        return session.getUser() != null && session.getUser().getId().equals(userId);
    }

    @Transactional(readOnly = true)
    public boolean canConsultantAccessSession(Long consultantId, Long sessionId) {
        ChatSession session = getSessionById(sessionId);
        return session.getConsultant() != null && session.getConsultant().getId().equals(consultantId);
    }

    private ChatSessionResponse buildSessionResponse(ChatSession session) {
        ChatSessionResponse response = ChatSessionResponse.from(session);

        List<ChatMessage> messages = getLastMessages(session.getId(), 20);
        response.setMessages(messages.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList()));

        return response;
    }

    public List<ChatSessionResponse> toResponseList(List<ChatSession> sessions) {
        return sessions.stream()
                .map(this::buildSessionResponse)
                .collect(Collectors.toList());
    }

    public Page<ChatSessionResponse> toResponsePage(Page<ChatSession> sessions) {
        return sessions.map(this::buildSessionResponse);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getActiveWithUserMessages() {
        return chatSessionRepository.findActiveWithUserMessages();
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getEmptySessions() {
        return chatSessionRepository.findEmptySessions();
    }

    @Transactional(readOnly = true)
    public List<ChatSession> getLongRunningSessions(int minutes) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(minutes);
        return chatSessionRepository.findLongRunningSessions(threshold);
    }
    @Transactional(readOnly = true)
    public ChatSessionResponse getSessionWithMessages(Long sessionId, int limit) {
        ChatSession session = getSessionById(sessionId);
        ChatSessionResponse response = ChatSessionResponse.from(session);

        List<ChatMessage> messages = getLastMessages(sessionId, limit);
        response.setMessages(messages.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList()));

        return response;
    }
}