package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatMessage;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatSession;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.SenderType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySessionOrderByTimestampAsc(ChatSession session);
    Page<ChatMessage> findBySessionIdOrderByTimestampAsc(Long sessionId, Pageable pageable);
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(Long sessionId);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId ORDER BY cm.timestamp DESC")
    List<ChatMessage> findLastMessages(@Param("sessionId") Long sessionId, Pageable pageable);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId ORDER BY cm.timestamp ASC")
    List<ChatMessage> findFirstMessage(@Param("sessionId") Long sessionId, Pageable pageable);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId ORDER BY cm.timestamp DESC")
    List<ChatMessage> findLastMessage(@Param("sessionId") Long sessionId, Pageable pageable);

    List<ChatMessage> findBySessionAndSenderTypeOrderByTimestampAsc(ChatSession session, SenderType senderType);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId AND cm.senderType = :senderType ORDER BY cm.timestamp ASC")
    List<ChatMessage> findBySessionIdAndSenderType(@Param("sessionId") Long sessionId,
                                                   @Param("senderType") SenderType senderType);

    default List<ChatMessage> findConsultantMessages(Long sessionId) {
        return findBySessionIdAndSenderType(sessionId, SenderType.CONSULTANT);
    }

    default List<ChatMessage> findUserMessages(Long sessionId) {
        return findBySessionIdAndSenderType(sessionId, SenderType.USER);
    }

    default List<ChatMessage> findSystemMessages(Long sessionId) {
        return findBySessionIdAndSenderType(sessionId, SenderType.SYSTEM);
    }

    @Query("SELECT cm.senderType, COUNT(cm) FROM ChatMessage cm WHERE cm.session.id = :sessionId GROUP BY cm.senderType")
    List<Object[]> countMessagesBySender(@Param("sessionId") Long sessionId);

    List<ChatMessage> findBySessionAndTimestampBetweenOrderByTimestampAsc(
            ChatSession session, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId " +
            "AND cm.timestamp BETWEEN :startDate AND :endDate ORDER BY cm.timestamp ASC")
    List<ChatMessage> findBySessionIdAndTimestampBetween(@Param("sessionId") Long sessionId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    List<ChatMessage> findBySessionAndTimestampAfterOrderByTimestampAsc(ChatSession session, LocalDateTime date);
    List<ChatMessage> findBySessionAndTimestampBeforeOrderByTimestampAsc(ChatSession session, LocalDateTime date);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId " +
            "AND LOWER(cm.message) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY cm.timestamp ASC")
    List<ChatMessage> searchInSession(@Param("sessionId") Long sessionId,
                                      @Param("searchTerm") String searchTerm);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId " +
            "AND cm.senderType = :senderType " +
            "AND LOWER(cm.message) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY cm.timestamp ASC")
    List<ChatMessage> searchInSessionBySender(@Param("sessionId") Long sessionId,
                                              @Param("senderType") SenderType senderType,
                                              @Param("searchTerm") String searchTerm);

    @Query("SELECT cm FROM ChatMessage cm JOIN cm.session s " +
            "WHERE s.user.id = :userId " +
            "AND LOWER(cm.message) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY cm.timestamp DESC")
    List<ChatMessage> searchUserMessages(@Param("userId") Long userId,
                                         @Param("searchTerm") String searchTerm,
                                         Pageable pageable);

    long countBySession(ChatSession session);
    long countBySessionId(Long sessionId);
    long countBySessionAndSenderType(ChatSession session, SenderType senderType);

    @Query("SELECT " +
            "COUNT(cm) as totalMessages, " +
            "SUM(CASE WHEN cm.senderType = 'USER' THEN 1 ELSE 0 END) as userMessages, " +
            "SUM(CASE WHEN cm.senderType = 'CONSULTANT' THEN 1 ELSE 0 END) as consultantMessages, " +
            "SUM(CASE WHEN cm.senderType = 'SYSTEM' THEN 1 ELSE 0 END) as systemMessages, " +
            "MIN(cm.timestamp) as firstMessageTime, " +
            "MAX(cm.timestamp) as lastMessageTime " +
            "FROM ChatMessage cm WHERE cm.session.id = :sessionId")
    Object[] getSessionStatistics(@Param("sessionId") Long sessionId);

    @Query("SELECT AVG(msgCount) FROM (SELECT COUNT(cm) as msgCount FROM ChatMessage cm GROUP BY cm.session.id)")
    Double getAverageMessagesPerSession();

    @Query("SELECT " +
            "CASE " +
            "  WHEN LENGTH(cm.message) < 50 THEN 'Short' " +
            "  WHEN LENGTH(cm.message) < 200 THEN 'Medium' " +
            "  ELSE 'Long' " +
            "END as messageLength, " +
            "COUNT(cm) " +
            "FROM ChatMessage cm " +
            "GROUP BY messageLength")
    List<Object[]> getMessageLengthStatistics();

    // ИСПРАВЛЕНО: Используем EXTRACT для часа
    @Query("SELECT EXTRACT(HOUR FROM cm.timestamp), COUNT(cm) FROM ChatMessage cm " +
            "GROUP BY EXTRACT(HOUR FROM cm.timestamp) ORDER BY EXTRACT(HOUR FROM cm.timestamp)")
    List<Object[]> getHourlyActivity();

    @Query("SELECT s.user.id, s.user.username, COUNT(cm) as msgCount " +
            "FROM ChatMessage cm JOIN cm.session s " +
            "WHERE s.user IS NOT NULL " +
            "GROUP BY s.user.id, s.user.username " +
            "ORDER BY msgCount DESC")
    List<Object[]> getMostActiveUsers(Pageable pageable);

    // ИСПРАВЛЕНО: Используем nativeQuery для сложного запроса с вычислением времени
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (cm2.timestamp - cm1.timestamp))) " +
            "FROM chat_messages cm1 " +
            "JOIN chat_messages cm2 ON cm1.session_id = cm2.session_id " +
            "WHERE cm1.sender_type = 'USER' " +
            "AND cm2.sender_type = 'CONSULTANT' " +
            "AND cm2.timestamp > cm1.timestamp " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM chat_messages cm3 " +
            "    WHERE cm3.session_id = cm1.session_id " +
            "    AND cm3.sender_type = 'CONSULTANT' " +
            "    AND cm3.timestamp > cm1.timestamp " +
            "    AND cm3.timestamp < cm2.timestamp" +
            ")", nativeQuery = true)
    Double getAverageResponseTime();

    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE cm.senderType = 'USER' " +
            "AND NOT EXISTS (SELECT cm2 FROM ChatMessage cm2 " +
            "                WHERE cm2.session.id = cm.session.id " +
            "                AND cm2.senderType = 'CONSULTANT' " +
            "                AND cm2.timestamp > cm.timestamp) " +
            "ORDER BY cm.timestamp DESC")
    List<ChatMessage> findUnansweredMessages(Pageable pageable);

    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE cm.senderType = 'USER' " +
            "AND cm.timestamp < :threshold " +
            "AND NOT EXISTS (SELECT cm2 FROM ChatMessage cm2 " +
            "                WHERE cm2.session.id = cm.session.id " +
            "                AND cm2.senderType = 'CONSULTANT' " +
            "                AND cm2.timestamp > cm.timestamp) " +
            "ORDER BY cm.timestamp ASC")
    List<ChatMessage> findStaleUnansweredMessages(@Param("threshold") LocalDateTime threshold);

    @Modifying
    @Transactional
    void deleteBySession(ChatSession session);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.session.id = :sessionId")
    int deleteBySessionId(@Param("sessionId") Long sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.timestamp < :date")
    int deleteOldMessages(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.senderType = 'SYSTEM' AND cm.timestamp < :date")
    int deleteOldSystemMessages(@Param("date") LocalDateTime date);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId ORDER BY cm.timestamp ASC")
    List<ChatMessage> getChatTranscript(@Param("sessionId") Long sessionId);

    @Query("SELECT CONCAT(cm.timestamp, ' - ', cm.senderType, ': ', cm.message) " +
            "FROM ChatMessage cm WHERE cm.session.id = :sessionId ORDER BY cm.timestamp ASC")
    List<String> getChatTranscriptFormatted(@Param("sessionId") Long sessionId);

    boolean existsBySessionId(Long sessionId);
    boolean existsBySessionIdAndSenderType(Long sessionId, SenderType senderType);

    @Query("SELECT COUNT(cm) > 0 FROM ChatMessage cm " +
            "WHERE cm.session.id = :sessionId AND cm.senderType = 'CONSULTANT'")
    boolean hasConsultantReplied(@Param("sessionId") Long sessionId);

    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE LOWER(cm.message) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND cm.senderType = 'USER' " +
            "ORDER BY cm.timestamp DESC")
    List<ChatMessage> findMessagesWithKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<ChatMessage> findAllByOrderByTimestampDesc(Pageable pageable);
    Page<ChatMessage> findBySessionIdOrderByTimestampDesc(Long sessionId, Pageable pageable);
}