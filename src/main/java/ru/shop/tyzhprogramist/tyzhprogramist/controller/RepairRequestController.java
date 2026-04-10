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
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreateRepairRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.RepairRequestResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RepairRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.RepairRequestService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/repair-requests")
@RequiredArgsConstructor
public class RepairRequestController {

    private final RepairRequestService repairRequestService;
    private final UserService userService;

    private Long getCurrentUserId() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return principal.getId();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepairRequestResponse> createRequest(@Valid @RequestBody CreateRepairRequest request) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        RepairRequest repairRequest = repairRequestService.create(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repairRequestService.getResponseById(repairRequest.getId()));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<RepairRequestResponse>> getMyRequests(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = getCurrentUserId();
        Page<RepairRequestResponse> page = repairRequestService.findResponsesByUserId(userId, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/me/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepairRequestResponse> getMyRequest(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        RepairRequestResponse response = repairRequestService.getResponseById(id);
        if (!response.getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RepairRequestResponse> cancelMyRequest(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        RepairRequest cancelled = repairRequestService.cancelByUser(id, userId);
        return ResponseEntity.ok(repairRequestService.getResponseById(cancelled.getId()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<PageResponse<RepairRequestResponse>> getAllRequests(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RepairRequestResponse> page = repairRequestService.findAllResponses(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(repairRequestService.getResponseById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        RepairRequest request = repairRequestService.updateStatus(id, status);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @PutMapping("/{id}/diagnostics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> setDiagnostics(@PathVariable Long id) {
        RepairRequest request = repairRequestService.setDiagnostics(id);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @PutMapping("/{id}/repair")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> startRepair(@PathVariable Long id) {
        RepairRequest request = repairRequestService.setRepairInProgress(id);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> completeRepair(
            @PathVariable Long id,
            @RequestParam BigDecimal finalPrice) {
        RepairRequest request = repairRequestService.completeRepair(id, finalPrice);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @PutMapping("/{id}/issue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> markAsIssued(@PathVariable Long id) {
        RepairRequest request = repairRequestService.markAsIssued(id);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @PutMapping("/{id}/comment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> addComment(
            @PathVariable Long id,
            @RequestParam String comment) {
        RepairRequest request = repairRequestService.updateMasterComment(id, comment);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @PutMapping("/{id}/estimated-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<RepairRequestResponse> updateEstimatedPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price) {
        RepairRequest request = repairRequestService.updateEstimatedPrice(id, price);
        return ResponseEntity.ok(repairRequestService.getResponseById(request.getId()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<PageResponse<RepairRequestResponse>> getByStatus(
            @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<RepairRequest> page = repairRequestService.findByStatus(status, pageable);
        return ResponseEntity.ok(PageResponse.from(repairRequestService.toResponsePage(page)));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> getStatistics() {
        return ResponseEntity.ok(repairRequestService.getOverallStatistics());
    }
}