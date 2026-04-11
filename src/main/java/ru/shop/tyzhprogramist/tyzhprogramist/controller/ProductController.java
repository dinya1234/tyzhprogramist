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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Product> page = productService.getAllAvailableProducts(pageable);
        Page<ProductResponse> responsePage = page.map(ProductResponse::from);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductFullResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductFullResponse(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
        Product product = productService.getBySlug(slug);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.getProductsByCategory(categoryId, pageable);
        Page<ProductResponse> responsePage = page.map(ProductResponse::from);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Product> page = productService.searchProducts(q, pageable);
        Page<ProductResponse> responsePage = page.map(ProductResponse::from);
        return ResponseEntity.ok(PageResponse.from(responsePage));
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponse>> getNewProducts(@RequestParam(defaultValue = "12") int limit) {
        return ResponseEntity.ok(productService.getNewProducts(limit));
    }

    @GetMapping("/bestsellers")
    public ResponseEntity<List<ProductResponse>> getBestsellers(@RequestParam(defaultValue = "12") int limit) {
        return ResponseEntity.ok(productService.getBestsellers(limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody Product product,
                                                         @RequestParam Long categoryId) {
        Product created = productService.createProduct(product, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product,
            @RequestParam(required = false) Long categoryId) {
        Product updated = productService.updateProduct(id, product, categoryId);
        return ResponseEntity.ok(ProductResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}