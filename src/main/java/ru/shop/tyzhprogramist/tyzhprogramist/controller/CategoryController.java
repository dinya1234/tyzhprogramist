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
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.service.CategoryService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    public ResponseEntity<List<Map<String, Object>>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/roots")
    public ResponseEntity<List<CategoryResponse>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @GetMapping
    public ResponseEntity<PageResponse<Category>> getAllCategories(
            @PageableDefault(size = 50, sort = "order", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Category> page = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getBySlug(slug));
    }

    @GetMapping("/{id}/breadcrumbs")
    public ResponseEntity<List<Map<String, Object>>> getBreadcrumbs(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getBreadcrumbs(id));
    }

    @GetMapping("/with-products")
    public ResponseEntity<List<Map<String, Object>>> getCategoriesWithProductCount() {
        return ResponseEntity.ok(categoryService.getCategoriesWithProductCount());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String slug,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) String image,
            @RequestParam(required = false) Integer order) {
        Category category = categoryService.createCategory(name, slug, parentId, image, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String slug,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) String image,
            @RequestParam(required = false) Integer order) {
        Category category = categoryService.updateCategory(id, name, slug, parentId, image, order);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getCategoryStatistics() {
        return ResponseEntity.ok(categoryService.getCategoryStatistics());
    }
}