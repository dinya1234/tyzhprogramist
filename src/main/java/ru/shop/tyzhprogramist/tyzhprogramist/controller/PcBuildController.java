package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreatePcBuildRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PcBuildComponentResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PcBuildResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.PcBuild;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.PcBuildService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pc-builds")
@RequiredArgsConstructor
public class PcBuildController {

    private final PcBuildService pcBuildService;
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

    @GetMapping("/public")
    public ResponseEntity<PageResponse<PcBuildResponse>> getPublicBuilds(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PcBuildResponse> page = pcBuildService.getPublicBuildResponses(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/public/recent")
    public ResponseEntity<List<PcBuild>> getRecentPublicBuilds(@RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(pcBuildService.getRecentPublicBuilds(limit));
    }

    @GetMapping("/public/top-views")
    public ResponseEntity<PageResponse<PcBuild>> getMostViewedBuilds(
            @PageableDefault(size = 12) Pageable pageable) {
        Page<PcBuild> page = pcBuildService.getMostViewedPublicBuilds(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PcBuildResponse> getPublicBuild(@PathVariable Long id) {
        pcBuildService.incrementViews(id);
        return ResponseEntity.ok(pcBuildService.getPcBuildResponseById(id));
    }

    @GetMapping("/public/search")
    public ResponseEntity<PageResponse<PcBuildResponse>> searchPublicBuilds(
            @RequestParam String q,
            @PageableDefault(size = 12) Pageable pageable) {
        Page<PcBuild> page = pcBuildService.searchPublicBuilds(q, pageable);

        List<PcBuildResponse> responses = new ArrayList<>();
        for (PcBuild build : page.getContent()) {
            responses.add(pcBuildService.getPcBuildResponseById(build.getId()));
        }

        Page<PcBuildResponse> responsePage = new PageImpl<>(responses, pageable, page.getTotalElements());
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<PcBuildResponse>> getMyBuilds(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = getCurrentUserId();
        Page<PcBuildResponse> page = pcBuildService.getUserBuildResponses(userId, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/me/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PcBuildResponse> getMyBuild(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canViewBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        pcBuildService.incrementViews(id);
        return ResponseEntity.ok(pcBuildService.getPcBuildResponseById(id));
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PcBuildResponse> createBuild(@Valid @RequestBody CreatePcBuildRequest request) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        PcBuild build = pcBuildService.createBuild(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pcBuildService.getPcBuildResponseById(build.getId()));
    }

    @PutMapping("/me/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PcBuildResponse> updateBuild(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isPublic) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        PcBuild build = pcBuildService.updateBuild(id, name, isPublic);
        return ResponseEntity.ok(pcBuildService.getPcBuildResponseById(build.getId()));
    }

    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteBuild(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        pcBuildService.deleteBuild(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/{id}/public")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PcBuildResponse> makePublic(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        PcBuild build = pcBuildService.makePublic(id);
        return ResponseEntity.ok(pcBuildService.getPcBuildResponseById(build.getId()));
    }

    @PostMapping("/me/{id}/private")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PcBuildResponse> makePrivate(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        PcBuild build = pcBuildService.makePrivate(id);
        return ResponseEntity.ok(pcBuildService.getPcBuildResponseById(build.getId()));
    }

    @PostMapping("/me/{id}/clone")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PcBuildResponse> cloneBuild(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        PcBuild cloned = pcBuildService.cloneBuild(id, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pcBuildService.getPcBuildResponseById(cloned.getId()));
    }

    @GetMapping("/me/{id}/components")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PcBuildComponentResponse>> getComponents(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canViewBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pcBuildService.getBuildComponentResponses(id));
    }

    @PostMapping("/me/{id}/components/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addComponent(
            @PathVariable Long id,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        pcBuildService.addComponent(id, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/{id}/components/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeComponent(
            @PathVariable Long id,
            @PathVariable Long productId) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        pcBuildService.removeComponent(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/{id}/components/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateComponentQuantity(
            @PathVariable Long id,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        pcBuildService.updateComponentQuantity(id, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/{id}/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearBuild(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canEditBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        pcBuildService.clearBuild(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/{id}/total")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BigDecimal> getTotalPrice(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canViewBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pcBuildService.calculateTotalPrice(id));
    }

    @GetMapping("/me/{id}/export")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> exportBuildAsText(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (!pcBuildService.canViewBuild(id, userId)) {
            return ResponseEntity.notFound().build();
        }
        String export = pcBuildService.exportBuildAsText(id);
        return ResponseEntity.ok(export);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PcBuildResponse>> getAllBuildsAdmin(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PcBuildResponse> page = pcBuildService.getAllBuildResponses(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPcBuildStatistics() {
        return ResponseEntity.ok(pcBuildService.getPcBuildStatistics());
    }
}