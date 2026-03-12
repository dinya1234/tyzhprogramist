package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CategoryResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String slug,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) String image,
            @RequestParam(required = false) Integer order) {
        log.info("POST /api/categories - создание категории: {}", name);
        CategoryResponse response = categoryService.createCategory(name, slug, parentId, image, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/categories/{} - получение категории", id);
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        log.info("GET /api/categories/slug/{} - получение категории по slug", slug);
        CategoryResponse response = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryResponse>> getRootCategories() {
        log.info("GET /api/categories/root - получение корневых категорий");
        List<CategoryResponse> categories = categoryService.getAllRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<CategoryResponse>> getCategoryChildren(@PathVariable Long id) {
        log.info("GET /api/categories/{}/children - получение дочерних категорий", id);
        List<CategoryResponse> children = categoryService.getCategoryChildren(id);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryResponse>> getCategoryTree() {
        log.info("GET /api/categories/tree - получение полного дерева категорий");
        List<CategoryResponse> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{id}/path")
    public ResponseEntity<List<CategoryResponse>> getCategoryPath(@PathVariable Long id) {
        log.info("GET /api/categories/{}/path - получение пути к категории", id);
        List<CategoryResponse> path = categoryService.getCategoryPath(id);
        return ResponseEntity.ok(path);
    }

    @GetMapping("/{id}/breadcrumbs")
    public ResponseEntity<List<CategoryResponse>> getBreadcrumbs(@PathVariable Long id) {
        log.info("GET /api/categories/{}/breadcrumbs - получение хлебных крошек", id);
        List<CategoryResponse> breadcrumbs = categoryService.getBreadcrumbs(id);
        return ResponseEntity.ok(breadcrumbs);
    }

    @GetMapping("/with-products")
    public ResponseEntity<List<CategoryResponse>> getCategoriesWithProducts() {
        log.info("GET /api/categories/with-products - получение категорий с товарами");
        List<CategoryResponse> categories = categoryService.getCategoriesWithProducts();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/parent")
    public ResponseEntity<List<CategoryResponse>> getParentCategories() {
        log.info("GET /api/categories/parent - получение родительских категорий");
        List<CategoryResponse> parents = categoryService.getParentCategories();
        return ResponseEntity.ok(parents);
    }

    @GetMapping("/leaf")
    public ResponseEntity<List<CategoryResponse>> getLeafCategories() {
        log.info("GET /api/categories/leaf - получение конечных категорий");
        List<CategoryResponse> leaves = categoryService.getLeafCategories();
        return ResponseEntity.ok(leaves);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @PageableDefault(size = 20, sort = "order", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("GET /api/categories - получение всех категорий, страница: {}", pageable.getPageNumber());
        Page<CategoryResponse> categories = categoryService.getAllCategoriesPaginated(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponse>> searchCategories(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/categories/search - поиск категорий по запросу: {}", q);
        Page<CategoryResponse> categories = categoryService.searchCategories(q, pageable);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String slug,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) String image,
            @RequestParam(required = false) Integer order) {
        log.info("PUT /api/categories/{} - обновление категории", id);
        CategoryResponse response = categoryService.updateCategory(id, name, slug, parentId, image, order);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/categories/{} - удаление категории", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/move")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> moveCategory(
            @PathVariable Long id,
            @RequestParam(required = false) Long newParentId,
            @RequestParam(required = false) Integer newOrder) {
        log.info("PATCH /api/categories/{}/move - перемещение категории", id);
        categoryService.moveCategory(id, newParentId, newOrder);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> changeOrder(
            @PathVariable Long id,
            @RequestParam Integer newOrder) {
        log.info("PATCH /api/categories/{}/order - изменение порядка на {}", id, newOrder);
        CategoryResponse response = categoryService.changeOrder(id, newOrder);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reorderCategories(
            @RequestParam(required = false) Long parentId,
            @RequestBody List<Long> categoryIds) {
        log.info("POST /api/categories/reorder - переупорядочивание категорий для родителя: {}", parentId);
        categoryService.reorderCategories(parentId, categoryIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/is-descendant/{descendantId}")
    public ResponseEntity<Boolean> isDescendant(
            @PathVariable Long id,
            @PathVariable Long descendantId) {
        log.info("GET /api/categories/{}/is-descendant/{} - проверка является ли потомком", id, descendantId);
        boolean isDescendant = categoryService.isDescendant(id, descendantId);
        return ResponseEntity.ok(isDescendant);
    }

    @GetMapping("/max-depth")
    public ResponseEntity<Integer> getMaxDepth() {
        log.info("GET /api/categories/max-depth - получение максимальной глубины");
        Integer maxDepth = categoryService.getMaxDepth();
        return ResponseEntity.ok(maxDepth);
    }

    @GetMapping("/empty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponse>> getEmptyCategories() {
        log.info("GET /api/categories/empty - получение пустых категорий");
        List<CategoryResponse> empty = categoryService.getEmptyCategories();
        return ResponseEntity.ok(empty);
    }

    @DeleteMapping("/empty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deleteEmptyCategories() {
        log.info("DELETE /api/categories/empty - удаление пустых категорий");
        long deleted = categoryService.deleteEmptyCategories();
        return ResponseEntity.ok(deleted);
    }

    @PatchMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> setCategoryImage(
            @PathVariable Long id,
            @RequestParam String imageUrl) {
        log.info("PATCH /api/categories/{}/image - установка изображения", id);
        CategoryResponse response = categoryService.setCategoryImage(id, imageUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getCategoryStatistics() {
        log.info("GET /api/categories/statistics - получение статистики по категориям");
        List<Object[]> statistics = categoryService.getCategoryStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCategoriesCount() {
        log.info("GET /api/categories/count - получение общего количества категорий");
        long count = categoryService.getTotalCategoriesCount();
        return ResponseEntity.ok(count);
    }
}