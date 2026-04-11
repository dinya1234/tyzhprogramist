package ru.shop.tyzhprogramist.tyzhprogramist.controller;

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
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.EntityRelationResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RelationType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.EntityRelationService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/relations")
@RequiredArgsConstructor
public class EntityRelationController {

    private final EntityRelationService entityRelationService;
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

    @GetMapping("/compatibility/check")
    public ResponseEntity<Boolean> checkCompatibility(
            @RequestParam String fromType,
            @RequestParam Long fromId,
            @RequestParam String toType,
            @RequestParam Long toId) {
        Optional<Boolean> result = entityRelationService.checkCompatibility(fromType, fromId, toType, toId);
        return ResponseEntity.ok(result.orElse(true));
    }

    @GetMapping("/compatibility/products/{productId1}/{productId2}")
    public ResponseEntity<Boolean> checkProductsCompatibility(
            @PathVariable Long productId1,
            @PathVariable Long productId2) {
        Optional<Boolean> result = entityRelationService.areProductsCompatible(productId1, productId2);
        return ResponseEntity.ok(result.orElse(true));
    }

    @GetMapping("/compatibility/product/{productId}/component-type/{componentTypeId}")
    public ResponseEntity<Boolean> checkProductWithComponentType(
            @PathVariable Long productId,
            @PathVariable Long componentTypeId) {
        Optional<Boolean> result = entityRelationService.isProductCompatibleWithComponentType(productId, componentTypeId);
        return ResponseEntity.ok(result.orElse(true));
    }

    @GetMapping("/compatibility/{contentType}/{objectId}/compatible")
    public ResponseEntity<List<Object[]>> getCompatibleEntities(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        return ResponseEntity.ok(entityRelationService.getCompatibleEntities(contentType, objectId));
    }

    @GetMapping("/compatibility/{contentType}/{objectId}/incompatible")
    public ResponseEntity<List<Object[]>> getIncompatibleEntities(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        return ResponseEntity.ok(entityRelationService.getIncompatibleEntities(contentType, objectId));
    }

    @GetMapping("/comparisons")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getMyComparisonNames() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(entityRelationService.getUserComparisonNames(userId));
    }

    @GetMapping("/comparisons/{comparisonName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EntityRelationResponse>> getComparison(@PathVariable String comparisonName) {
        Long userId = getCurrentUserId();
        List<EntityRelation> items = entityRelationService.getComparisonItems(userId, comparisonName);
        return ResponseEntity.ok(items.stream()
                .map(EntityRelationResponse::from)
                .toList());
    }

    @GetMapping("/comparisons/{comparisonName}/grouped")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, List<EntityRelationResponse>>> getComparisonGrouped(
            @PathVariable String comparisonName) {
        Long userId = getCurrentUserId();
        Map<String, List<EntityRelation>> grouped = entityRelationService.getComparisonItemsGrouped(userId, comparisonName);
        Map<String, List<EntityRelationResponse>> response = grouped.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(EntityRelationResponse::from).toList()
                ));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comparisons/{comparisonName}/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityRelationResponse> addProductToComparison(
            @PathVariable String comparisonName,
            @PathVariable Long productId) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        EntityRelation relation = entityRelationService.addProductToComparison(user, comparisonName, productId);
        log.info("Пользователь {} добавил товар {} в сравнение {}", user.getUsername(), productId, comparisonName);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityRelationResponse.from(relation));
    }

    @PostMapping("/comparisons/{comparisonName}/pc-builds/{pcBuildId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EntityRelationResponse> addPcBuildToComparison(
            @PathVariable String comparisonName,
            @PathVariable Long pcBuildId) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        EntityRelation relation = entityRelationService.addPcBuildToComparison(user, comparisonName, pcBuildId);
        log.info("Пользователь {} добавил сборку {} в сравнение {}", user.getUsername(), pcBuildId, comparisonName);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityRelationResponse.from(relation));
    }

    @DeleteMapping("/comparisons/{comparisonName}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeProductFromComparison(
            @PathVariable String comparisonName,
            @PathVariable Long productId) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        entityRelationService.removeFromComparison(user, comparisonName,
                EntityRelationService.TYPE_PRODUCT, productId);
        log.info("Пользователь {} удалил товар {} из сравнения {}", user.getUsername(), productId, comparisonName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comparisons/{comparisonName}/pc-builds/{pcBuildId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removePcBuildFromComparison(
            @PathVariable String comparisonName,
            @PathVariable Long pcBuildId) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        entityRelationService.removeFromComparison(user, comparisonName,
                EntityRelationService.TYPE_PC_BUILD, pcBuildId);
        log.info("Пользователь {} удалил сборку {} из сравнения {}", user.getUsername(), pcBuildId, comparisonName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comparisons/{comparisonName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComparison(@PathVariable String comparisonName) {
        Long userId = getCurrentUserId();
        User user = userService.getById(userId);
        entityRelationService.deleteComparison(user, comparisonName);
        log.info("Пользователь {} удалил сравнение {}", user.getUsername(), comparisonName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comparisons/{comparisonName}/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> getComparisonCount(@PathVariable String comparisonName) {
        Long userId = getCurrentUserId();
        int count = entityRelationService.countComparisonItems(userId, comparisonName);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/comparisons/exists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> existsInComparison(
            @RequestParam String comparisonName,
            @RequestParam String contentType,
            @RequestParam Long objectId) {
        Long userId = getCurrentUserId();
        boolean exists = entityRelationService.existsInComparison(userId, comparisonName, contentType, objectId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/compatibility/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityRelationResponse> createProductCompatibilityRule(
            @RequestParam Long productId1,
            @RequestParam Long productId2,
            @RequestParam boolean compatible) {
        EntityRelation relation = entityRelationService.createProductCompatibilityRule(productId1, productId2, compatible);
        log.info("Админ создал правило совместимости между товарами {} и {}", productId1, productId2);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityRelationResponse.from(relation));
    }

    @PostMapping("/compatibility/products/symmetric")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityRelationResponse>> createSymmetricProductCompatibilityRule(
            @RequestParam Long productId1,
            @RequestParam Long productId2,
            @RequestParam boolean compatible) {
        List<EntityRelation> relations = entityRelationService.createSymmetricProductCompatibilityRule(
                productId1, productId2, compatible);
        log.info("Админ создал симметричное правило совместимости между товарами {} и {}", productId1, productId2);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(relations.stream().map(EntityRelationResponse::from).toList());
    }

    @PutMapping("/compatibility/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityRelationResponse> updateCompatibilityRule(
            @PathVariable Long ruleId,
            @RequestParam boolean compatible) {
        EntityRelation relation = entityRelationService.updateCompatibilityRule(ruleId, compatible);
        log.info("Админ обновил правило совместимости {}", ruleId);
        return ResponseEntity.ok(EntityRelationResponse.from(relation));
    }

    @DeleteMapping("/compatibility/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductCompatibilityRule(
            @RequestParam Long productId1,
            @RequestParam Long productId2) {
        entityRelationService.deleteCompatibilityRule(
                EntityRelationService.TYPE_PRODUCT, productId1,
                EntityRelationService.TYPE_PRODUCT, productId2);
        log.info("Админ удалил правило совместимости между товарами {} и {}", productId1, productId2);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/compatibility/{contentType}/{objectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteAllCompatibilityRules(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        int count = entityRelationService.deleteAllCompatibilityRules(contentType, objectId);
        log.info("Админ удалил {} правил совместимости для {}-{}", count, contentType, objectId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/compatibility/rules/{contentType}/{objectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityRelationResponse>> getCompatibilityRules(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        List<EntityRelation> rules = entityRelationService.getAllCompatibilityRules(contentType, objectId);
        return ResponseEntity.ok(rules.stream().map(EntityRelationResponse::from).toList());
    }

    @PostMapping("/compatibility/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityRelationResponse>> bulkCreateCompatibilityRules(
            @RequestParam String fromType,
            @RequestParam Long fromId,
            @RequestParam List<String> toTypes,
            @RequestParam List<Long> toIds,
            @RequestParam boolean compatible) {
        List<EntityRelation> relations = entityRelationService.bulkCreateCompatibilityRules(
                fromType, fromId, toTypes, toIds, compatible);
        log.info("Админ массово создал {} правил совместимости для {}-{}", relations.size(), fromType, fromId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(relations.stream().map(EntityRelationResponse::from).toList());
    }

    @PostMapping("/compatibility/copy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityRelationResponse>> copyCompatibilityRules(
            @RequestParam String sourceType,
            @RequestParam Long sourceId,
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        List<EntityRelation> copied = entityRelationService.copyCompatibilityRules(
                sourceType, sourceId, targetType, targetId);
        log.info("Админ скопировал {} правил совместимости от {}-{} к {}-{}",
                copied.size(), sourceType, sourceId, targetType, targetId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(copied.stream().map(EntityRelationResponse::from).toList());
    }

    @GetMapping("/admin/compatibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<EntityRelationResponse>> getAllCompatibilityRules(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EntityRelation> page = entityRelationService.getByRelationType(RelationType.COMPATIBILITY, pageable);
        Page<EntityRelationResponse> responsePage = page.map(EntityRelationResponse::from);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/admin/comparisons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<EntityRelationResponse>> getAllComparisons(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EntityRelation> page = entityRelationService.getByRelationType(RelationType.COMPARISON, pageable);
        Page<EntityRelationResponse> responsePage = page.map(EntityRelationResponse::from);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(entityRelationService.getEntityRelationStatistics());
    }

    @GetMapping("/compatibility/statistics/{contentType}/{objectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object[]> getCompatibilityStatistics(
            @PathVariable String contentType,
            @PathVariable Long objectId) {
        return ResponseEntity.ok(entityRelationService.getCompatibilityStatistics(contentType, objectId));
    }

    @GetMapping("/most-compared-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getMostComparedProducts(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(entityRelationService.getMostComparedProducts(limit));
    }

    @DeleteMapping("/admin/old-comparisons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteOldComparisons(@RequestParam(defaultValue = "30") int daysOld) {
        int count = entityRelationService.deleteOldComparisons(daysOld);
        log.info("Админ удалил {} старых сравнений", count);
        return ResponseEntity.ok(count);
    }
}