package ru.shop.tyzhprogramist.tyzhprogramist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatSession;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatStatus;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findByStatus(ChatStatus status);
    Page<ChatSession> findByStatus(ChatStatus status, Pageable pageable);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.status = 'ACTIVE' ORDER BY cs.startedAt DESC")
    List<ChatSession> findActiveSessions();

    @Query("SELECT cs FROM ChatSession cs WHERE cs.status = 'PENDING' ORDER BY cs.startedAt ASC")
    List<ChatSession> findPendingSessions();

    @Query("SELECT cs FROM ChatSession cs WHERE cs.status = 'CLOSED' ORDER BY cs.endedAt DESC")
    Page<ChatSession> findClosedSessions(Pageable pageable);

    List<ChatSession> findByUser(User user);
    Page<ChatSession> findByUser(User user, Pageable pageable);
    Page<ChatSession> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.id = :userId AND cs.status = 'ACTIVE'")
    Optional<ChatSession> findActiveUserSession(@Param("userId") Long userId);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.id = :userId ORDER BY cs.startedAt DESC")
    List<ChatSession> findLastUserSession(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(cs) > 0 FROM ChatSession cs WHERE cs.user.id = :userId AND cs.status = 'ACTIVE'")
    boolean hasActiveSession(@Param("userId") Long userId);

    List<ChatSession> findByConsultant(User consultant);
    Page<ChatSession> findByConsultant(User consultant, Pageable pageable);
    Page<ChatSession> findByConsultantId(Long consultantId, Pageable pageable);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.consultant.id = :consultantId AND cs.status = 'ACTIVE'")
    List<ChatSession> findActiveConsultantSessions(@Param("consultantId") Long consultantId);

    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.consultant.id = :consultantId AND cs.status = 'ACTIVE'")
    long countActiveConsultantSessions(@Param("consultantId") Long consultantId);

    @Query("SELECT cs.consultant.id, COUNT(cs) as sessionCount FROM ChatSession cs " +
            "WHERE cs.status = 'ACTIVE' GROUP BY cs.consultant.id ORDER BY sessionCount ASC")
    List<Object[]> findConsultantsByLoad(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession cs SET cs.consultant = :consultant, cs.status = 'ACTIVE' WHERE cs.id = :sessionId AND cs.status = 'PENDING'")
    int assignConsultant(@Param("sessionId") Long sessionId, @Param("consultant") User consultant);

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession cs SET cs.consultant.id = :consultantId, cs.status = 'ACTIVE' WHERE cs.id = :sessionId AND cs.status = 'PENDING'")
    int assignConsultantById(@Param("sessionId") Long sessionId, @Param("consultantId") Long consultantId);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.status = 'PENDING' ORDER BY cs.startedAt ASC")
    List<ChatSession> getNextPendingSession(Pageable pageable);

    long countByStatus(ChatStatus status);

    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.status = 'PENDING'")
    long countPendingSessions();

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession cs SET cs.status = 'CLOSED', cs.endedAt = :endedAt WHERE cs.id = :sessionId")
    int closeSession(@Param("sessionId") Long sessionId, @Param("endedAt") LocalDateTime endedAt);

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession cs SET cs.status = 'CLOSED', cs.endedAt = :endedAt " +
            "WHERE cs.consultant.id = :consultantId AND cs.status = 'ACTIVE'")
    int closeAllConsultantSessions(@Param("consultantId") Long consultantId, @Param("endedAt") LocalDateTime endedAt);

    @Modifying
    @Transactional
    @Query("UPDATE ChatSession cs SET cs.status = 'CLOSED', cs.endedAt = :endedAt " +
            "WHERE cs.status = 'ACTIVE' AND cs.endedAt IS NULL AND cs.startedAt < :timeout")
    int closeInactiveSessions(@Param("timeout") LocalDateTime timeout, @Param("endedAt") LocalDateTime endedAt);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.contextContentType = :contentType AND cs.contextObjectId = :objectId")
    List<ChatSession> findByContext(@Param("contentType") String contentType, @Param("objectId") Long objectId);

    Page<ChatSession> findByContextContentTypeAndContextObjectId(String contentType, Long objectId, Pageable pageable);

    List<ChatSession> findBySourceUrlContaining(String url);
    List<ChatSession> findByStartedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ChatSession> findByEndedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT cs FROM ChatSession cs WHERE FUNCTION('DATE', cs.startedAt) = CURRENT_DATE")
    List<ChatSession> findTodaySessions();

    @Query("SELECT cs FROM ChatSession cs WHERE cs.status = 'ACTIVE' AND cs.startedAt < :threshold")
    List<ChatSession> findLongRunningSessions(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT cs.status, COUNT(cs) FROM ChatSession cs GROUP BY cs.status")
    List<Object[]> getStatusStatistics();

    @Query(value = "SELECT DATE(cs.started_at) as date, " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN cs.status = 'CLOSED' THEN 1 ELSE 0 END) as completed, " +
            "AVG(CASE WHEN cs.ended_at IS NOT NULL " +
            "THEN EXTRACT(EPOCH FROM (cs.ended_at - cs.started_at)) / 60 " +
            "ELSE NULL END) as avgDuration " +
            "FROM chat_sessions cs " +
            "WHERE cs.started_at BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(cs.started_at) " +
            "ORDER BY DATE(cs.started_at)",
            nativeQuery = true)
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT cs.consultant_id, u.username, COUNT(*), " +
            "AVG(CASE WHEN cs.ended_at IS NOT NULL " +
            "THEN EXTRACT(EPOCH FROM (cs.ended_at - cs.started_at)) / 60 " +
            "ELSE NULL END) as avgDuration " +
            "FROM chat_sessions cs " +
            "JOIN users u ON cs.consultant_id = u.id " +
            "WHERE cs.consultant_id IS NOT NULL AND cs.status = 'CLOSED' " +
            "GROUP BY cs.consultant_id, u.username " +
            "ORDER BY COUNT(*) DESC",
            nativeQuery = true)
    List<Object[]> getConsultantStatistics();

    @Query("SELECT SUBSTRING(cs.sourceUrl, 1, 50) as urlSource, COUNT(cs) " +
            "FROM ChatSession cs WHERE cs.sourceUrl IS NOT NULL " +
            "GROUP BY SUBSTRING(cs.sourceUrl, 1, 50) " +
            "ORDER BY COUNT(cs) DESC")
    List<Object[]> getSourceStatistics(Pageable pageable);

    @Query("SELECT cs.contextContentType, COUNT(cs) FROM ChatSession cs WHERE cs.contextContentType IS NOT NULL GROUP BY cs.contextContentType")
    List<Object[]> getContextStatistics();

    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (ended_at - started_at)) / 60) FROM chat_sessions WHERE ended_at IS NOT NULL",
            nativeQuery = true)
    Double getAverageSessionDuration();

    @Query(value = "SELECT MAX(EXTRACT(EPOCH FROM (ended_at - started_at)) / 60) FROM chat_sessions WHERE ended_at IS NOT NULL",
            nativeQuery = true)
    Double getMaxSessionDuration();

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user IS NULL")
    List<ChatSession> findGuestSessions();

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user IS NULL AND cs.startedAt BETWEEN :startDate AND :endDate")
    List<ChatSession> findGuestSessionsBetween(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatSession cs WHERE cs.status = 'CLOSED' AND cs.endedAt < :date")
    int deleteOldClosedSessions(@Param("date") LocalDateTime date);

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    void deleteByConsultant(User consultant);

    Page<ChatSession> findAllByOrderByStartedAtDesc(Pageable pageable);
    Page<ChatSession> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);
    Page<ChatSession> findByConsultantIdOrderByStartedAtDesc(Long consultantId, Pageable pageable);

    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.status = 'ACTIVE'")
    long getActiveSessionsCount();

    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.status = 'PENDING'")
    long getPendingSessionsCount();

    @Query("SELECT " +
            "COUNT(CASE WHEN cs.status = 'ACTIVE' THEN 1 END) as active, " +
            "COUNT(CASE WHEN cs.status = 'PENDING' THEN 1 END) as pending, " +
            "COUNT(DISTINCT cs.consultant.id) as activeConsultants " +
            "FROM ChatSession cs")
    Object[] getCurrentLoad();

    @Query("SELECT DISTINCT cs FROM ChatSession cs JOIN cs.messages m WHERE m.senderType = 'USER' AND cs.status = 'ACTIVE'")
    List<ChatSession> findActiveWithUserMessages();

    @Query("SELECT cs FROM ChatSession cs WHERE SIZE(cs.messages) = 0")
    List<ChatSession> findEmptySessions();
}