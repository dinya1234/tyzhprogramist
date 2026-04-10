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
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PageResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFullResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<Product>> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Product> page = productService.getAllAvailableProducts(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductFullResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductFullResponse(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Product> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getBySlug(slug));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getBySku(sku));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/category/slug/{categorySlug}")
    public ResponseEntity<PageResponse<Product>> getProductsByCategorySlug(
            @PathVariable String categorySlug,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.getProductsByCategorySlug(categorySlug, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/category/{categoryId}/descendants")
    public ResponseEntity<PageResponse<Product>> getProductsByCategoryWithDescendants(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.getProductsByCategoryWithDescendants(categoryId, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<Product>> searchProducts(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.searchProducts(q, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/price-range")
    public ResponseEntity<PageResponse<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.getProductsByPriceRange(min, max, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponse>> getNewProducts(@RequestParam(defaultValue = "12") int limit) {
        return ResponseEntity.ok(productService.getNewProducts(limit));
    }

    @GetMapping("/bestsellers")
    public ResponseEntity<List<ProductResponse>> getBestsellers(@RequestParam(defaultValue = "12") int limit) {
        return ResponseEntity.ok(productService.getBestsellers(limit));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponse>> getMostViewed(@RequestParam(defaultValue = "12") int limit) {
        return ResponseEntity.ok(productService.getMostViewedProducts(limit));
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<ProductResponse>> getTopSelling(@RequestParam(defaultValue = "12") int limit) {
        return ResponseEntity.ok(productService.getMostPurchasedProducts(limit));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "5") int threshold) {
        return ResponseEntity.ok(productService.getLowStockProducts(threshold));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product,
                                                 @RequestParam Long categoryId) {
        Product created = productService.createProduct(product, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product,
            @RequestParam(required = false) Long categoryId) {
        Product updated = productService.updateProduct(id, product, categoryId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable Long id) {
        productService.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/related/{relatedId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addRelatedProduct(@PathVariable Long id, @PathVariable Long relatedId) {
        productService.addRelatedProduct(id, relatedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/related/{relatedId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRelatedProduct(@PathVariable Long id, @PathVariable Long relatedId) {
        productService.removeRelatedProduct(id, relatedId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/increment-view")
    public ResponseEntity<Void> incrementViews(@PathVariable Long id) {
        productService.incrementViews(id);
        return ResponseEntity.ok().build();
    }
}