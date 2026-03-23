package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreateRepairRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.RepairRequestResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RepairRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.RepairRequestRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final UserService userService;
    private final FileAttachmentService fileAttachmentService;

    public static final String STATUS_ACCEPTED = "Принята";
    public static final String STATUS_DIAGNOSTICS = "Диагностика";
    public static final String STATUS_REPAIR = "Ремонт";
    public static final String STATUS_READY = "Готов к выдаче";
    public static final String STATUS_ISSUED = "Выдан";
    public static final String STATUS_CANCELLED = "Отменена";

    private static final Set<String> TERMINAL_STATUSES = Set.of(STATUS_ISSUED, STATUS_CANCELLED);

    @Transactional(readOnly = true)
    public RepairRequest getById(Long id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка на ремонт не найдена с id: " + id));
    }

    @Transactional(readOnly = true)
    public RepairRequestResponse getResponseById(Long id) {
        return RepairRequestResponse.from(getById(id));
    }

    @Transactional
    public RepairRequest create(User user, CreateRepairRequest request) {
        validateCreate(request);

        RepairRequest entity = new RepairRequest(
                user,
                request.getDeviceType().trim(),
                request.getProblemDescription().trim(),
                request.getEstimatedPrice()
        );

        RepairRequest saved = repairRequestRepository.save(entity);
        log.info("Создана заявка на ремонт {} пользователем {}", saved.getId(), user.getUsername());
        return saved;
    }

    @Transactional
    public RepairRequest create(Long userId, CreateRepairRequest request) {
        User user = userService.getById(userId);
        return create(user, request);
    }

    private void validateCreate(CreateRepairRequest request) {
        if (request.getDeviceType() == null || request.getDeviceType().trim().isEmpty()) {
            throw new BadRequestException("Укажите тип устройства");
        }
        if (request.getProblemDescription() == null || request.getProblemDescription().trim().isEmpty()) {
            throw new BadRequestException("Опишите проблему");
        }
    }

    @Transactional(readOnly = true)
    public Page<RepairRequest> findAll(Pageable pageable) {
        return repairRequestRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<RepairRequestResponse> findAllResponses(Pageable pageable) {
        return findAll(pageable).map(RepairRequestResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<RepairRequest> findByUserId(Long userId, Pageable pageable) {
        return repairRequestRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<RepairRequestResponse> findResponsesByUserId(Long userId, Pageable pageable) {
        return findByUserId(userId, pageable).map(RepairRequestResponse::from);
    }

    @Transactional(readOnly = true)
    public List<RepairRequest> findUserRecent(Long userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return repairRequestRepository.findUserRecentRequests(userId, since);
    }

    @Transactional(readOnly = true)
    public Page<RepairRequest> findByStatus(String status, Pageable pageable) {
        return repairRequestRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Transactional(readOnly = true)
    public List<RepairRequest> findNewRequests() {
        return repairRequestRepository.findNewRequests();
    }

    @Transactional(readOnly = true)
    public List<RepairRequest> findInProgressRequests() {
        return repairRequestRepository.findInProgressRequests();
    }

    @Transactional(readOnly = true)
    public List<RepairRequest> findCompletedRequests() {
        return repairRequestRepository.findCompletedRequests();
    }

    @Transactional(readOnly = true)
    public boolean hasActiveRequests(Long userId) {
        return repairRequestRepository.hasActiveRequests(userId);
    }

    @Transactional
    public RepairRequest updateStatus(Long requestId, String newStatus) {
        RepairRequest request = getById(requestId);
        if (TERMINAL_STATUSES.contains(request.getStatus())) {
            throw new BadRequestException("Нельзя изменить статус завершённой заявки");
        }

        int updated = repairRequestRepository.updateStatus(requestId, newStatus);
        if (updated == 0) {
            throw new BadRequestException("Не удалось обновить статус заявки");
        }
        log.info("Заявка {}: статус -> {}", requestId, newStatus);
        return getById(requestId);
    }

    @Transactional
    public RepairRequest setDiagnostics(Long requestId) {
        return updateStatus(requestId, STATUS_DIAGNOSTICS);
    }

    @Transactional
    public RepairRequest setRepairInProgress(Long requestId) {
        return updateStatus(requestId, STATUS_REPAIR);
    }

    @Transactional
    public RepairRequest updateMasterComment(Long requestId, String comment) {
        getById(requestId);
        if (comment == null || comment.trim().isEmpty()) {
            throw new BadRequestException("Комментарий мастера не может быть пустым");
        }
        int updated = repairRequestRepository.updateMasterComment(requestId, comment.trim());
        if (updated == 0) {
            throw new BadRequestException("Не удалось сохранить комментарий");
        }
        return getById(requestId);
    }

    @Transactional
    public RepairRequest updateEstimatedPrice(Long requestId, BigDecimal price) {
        getById(requestId);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Некорректная ориентировочная стоимость");
        }
        int updated = repairRequestRepository.updateEstimatedPrice(requestId, price);
        if (updated == 0) {
            throw new BadRequestException("Не удалось обновить ориентировочную стоимость");
        }
        return getById(requestId);
    }

    @Transactional
    public RepairRequest updateFinalPrice(Long requestId, BigDecimal price) {
        getById(requestId);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Некорректная итоговая стоимость");
        }
        int updated = repairRequestRepository.updateFinalPrice(requestId, price);
        if (updated == 0) {
            throw new BadRequestException("Не удалось обновить итоговую стоимость");
        }
        return getById(requestId);
    }

    @Transactional
    public RepairRequest completeRepair(Long requestId, BigDecimal finalPrice) {
        getById(requestId);
        if (finalPrice == null || finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Укажите итоговую стоимость");
        }
        int updated = repairRequestRepository.completeRepair(requestId, finalPrice);
        if (updated == 0) {
            throw new BadRequestException("Не удалось завершить ремонт");
        }
        log.info("Ремонт по заявке {} завершён, итог: {}", requestId, finalPrice);
        return getById(requestId);
    }

    @Transactional
    public RepairRequest markAsIssued(Long requestId) {
        getById(requestId);
        int updated = repairRequestRepository.markAsIssued(requestId);
        if (updated == 0) {
            throw new BadRequestException("Не удалось отметить выдачу");
        }
        log.info("Заявка {} выдана клиенту", requestId);
        return getById(requestId);
    }

    @Transactional
    public RepairRequest cancelByUser(Long requestId, Long userId) {
        RepairRequest request = getById(requestId);
        if (!request.getUser().getId().equals(userId)) {
            throw new BadRequestException("Это не ваша заявка");
        }
        if (TERMINAL_STATUSES.contains(request.getStatus())) {
            throw new BadRequestException("Заявку нельзя отменить");
        }
        return updateStatus(requestId, STATUS_CANCELLED);
    }

    @Transactional
    public RepairRequest cancelByAdmin(Long requestId) {
        return updateStatus(requestId, STATUS_CANCELLED);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getOverallStatistics() {
        List<Object[]> byStatus = repairRequestRepository.getStatusStatistics();
        BigDecimal revenue = repairRequestRepository.sumTotalRevenue(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now()
        );

        return Map.of(
                "byStatus", byStatus,
                "totalRevenueIssued", revenue != null ? revenue : BigDecimal.ZERO
        );
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyStatistics(LocalDateTime start, LocalDateTime end) {
        return repairRequestRepository.getDailyStatistics(start, end);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getRepairReport(LocalDateTime start, LocalDateTime end) {
        return repairRequestRepository.getRepairReport(start, end);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDeviceTypeStatistics() {
        return repairRequestRepository.getDeviceTypeStatistics();
    }

    @Transactional
    public void deleteByUser(User user) {
        for (RepairRequest r : repairRequestRepository.findByUser(user)) {
            fileAttachmentService.deleteAllFilesForEntity("RepairRequest", r.getId());
        }
        repairRequestRepository.deleteByUser(user);
        log.info("Удалены заявки на ремонт пользователя {}", user.getUsername());
    }

    @Transactional
    public void deleteRepairRequest(Long id) {
        RepairRequest request = getById(id);
        fileAttachmentService.deleteAllFilesForEntity("RepairRequest", id);
        repairRequestRepository.delete(request);
        log.info("Удалена заявка на ремонт {}", id);
    }

    @Transactional
    public int deleteOldCompletedRequests(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = repairRequestRepository.deleteOldCompletedRequests(threshold);
        log.info("Удалено {} старых завершённых заявок на ремонт", count);
        return count;
    }

    @Transactional
    public int deleteOldCancelledRequests(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = repairRequestRepository.deleteOldCancelledRequests(threshold);
        log.info("Удалено {} старых отменённых заявок на ремонт", count);
        return count;
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return repairRequestRepository.existsById(id);
    }

    public Page<RepairRequestResponse> toResponsePage(Page<RepairRequest> page) {
        return page.map(RepairRequestResponse::from);
    }

    public List<RepairRequestResponse> toResponseList(List<RepairRequest> list) {
        return list.stream()
                .map(RepairRequestResponse::from)
                .collect(Collectors.toList());
    }
}
