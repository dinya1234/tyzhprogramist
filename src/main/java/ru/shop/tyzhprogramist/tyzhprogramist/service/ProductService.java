package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFullResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final FileAttachmentService fileAttachmentService;

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден с id: " + id));
    }

    @Transactional(readOnly = true)
    public Product getBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Товар не найден: " + slug));
    }

    @Transactional(readOnly = true)
    public Product getBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new NotFoundException("Товар не найден с артикулом: " + sku));
    }

    @Transactional(readOnly = true)
    public ProductFullResponse getProductFullResponse(Long id) {
        Product product = getById(id);

        incrementViews(id);

        ProductFullResponse response = ProductFullResponse.from(product);

        List<String> images = fileAttachmentService.getImageUrlsForEntity("Product", id);
        response.setImages(images);
        response.setMainImage(images.isEmpty() ? null : images.get(0));

        List<ProductResponse> relatedProducts = product.getRelatedProducts().stream()
                .limit(8)
                .map(p -> {
                    ProductResponse pr = ProductResponse.from(p);
                    pr.setMainImage(fileAttachmentService.getMainImageUrlForEntity("Product", p.getId()));
                    return pr;
                })
                .collect(Collectors.toList());
        response.setRelatedProducts(relatedProducts);

        List<ProductResponse> frequentlyBought = product.getFrequentlyBoughtWith().stream()
                .limit(8)
                .map(p -> {
                    ProductResponse pr = ProductResponse.from(p);
                    pr.setMainImage(fileAttachmentService.getMainImageUrlForEntity("Product", p.getId()));
                    return pr;
                })
                .collect(Collectors.toList());
        response.setFrequentlyBought(frequentlyBought);

        return response;
    }

    @Transactional
    public void incrementViews(Long productId) {
        Product product = getById(productId);
        product.incrementViews();
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategorySlug(String categorySlug, Pageable pageable) {
        return productRepository.findByCategorySlug(categorySlug, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategoryWithDescendants(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryWithDescendants(categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.searchProducts(searchTerm, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllAvailableProducts(Pageable pageable) {
        return productRepository.findAllAvailable(pageable);
    }

    @Transactional
    public Product createProduct(Product product, Long categoryId) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new BadRequestException("Товар с таким артикулом уже существует");
        }
        if (productRepository.existsBySlug(product.getSlug())) {
            throw new BadRequestException("Товар с таким slug уже существует");
        }

        Category category = categoryService.getById(categoryId);
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct, Long categoryId) {
        Product product = getById(id);

        if (!product.getSku().equals(updatedProduct.getSku()) &&
                productRepository.existsBySku(updatedProduct.getSku())) {
            throw new BadRequestException("Товар с таким артикулом уже существует");
        }

        if (!product.getSlug().equals(updatedProduct.getSlug()) &&
                productRepository.existsBySlug(updatedProduct.getSlug())) {
            throw new BadRequestException("Товар с таким slug уже существует");
        }

        if (categoryId != null && (product.getCategory() == null || !product.getCategory().getId().equals(categoryId))) {
            Category category = categoryService.getById(categoryId);
            product.setCategory(category);
        }

        product.setSku(updatedProduct.getSku());
        product.setName(updatedProduct.getName());
        product.setSlug(updatedProduct.getSlug());
        product.setShortDescription(updatedProduct.getShortDescription());
        product.setFullDescription(updatedProduct.getFullDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setOldPrice(updatedProduct.getOldPrice());
        product.setQuantity(updatedProduct.getQuantity());
        product.setIsActive(updatedProduct.getIsActive());
        product.setIsNew(updatedProduct.getIsNew());
        product.setIsBestseller(updatedProduct.getIsBestseller());
        product.setWarrantyMonths(updatedProduct.getWarrantyMonths());
        product.setWeight(updatedProduct.getWeight());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getById(id);
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Transactional
    public void hardDeleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getNewProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findNewProducts(pageable).stream()
                .map(p -> {
                    ProductResponse response = ProductResponse.from(p);
                    response.setMainImage(fileAttachmentService.getMainImageUrlForEntity("Product", p.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getBestsellers(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findBestsellers(pageable).stream()
                .map(p -> {
                    ProductResponse response = ProductResponse.from(p);
                    response.setMainImage(fileAttachmentService.getMainImageUrlForEntity("Product", p.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getMostViewedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findMostViewed(pageable).stream()
                .map(p -> {
                    ProductResponse response = ProductResponse.from(p);
                    response.setMainImage(fileAttachmentService.getMainImageUrlForEntity("Product", p.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getMostPurchasedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findMostPurchased(pageable).stream()
                .map(p -> {
                    ProductResponse response = ProductResponse.from(p);
                    response.setMainImage(fileAttachmentService.getMainImageUrlForEntity("Product", p.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    @Transactional
    public void updateProductRating(Long productId, Double newRating) {
        Product product = getById(productId);
        product.setRating(newRating);
        productRepository.save(product);
    }

    @Transactional
    public void addRelatedProduct(Long productId, Long relatedProductId) {
        Product product = getById(productId);
        Product related = getById(relatedProductId);

        product.addRelatedProduct(related);
        productRepository.save(product);
    }

    @Transactional
    public void removeRelatedProduct(Long productId, Long relatedProductId) {
        Product product = getById(productId);
        Product related = getById(relatedProductId);

        product.removeRelatedProduct(related);
        productRepository.save(product);
    }

    @Transactional
    public void incrementPurchaseCount(Long productId) {
        Product product = getById(productId);
        product.incrementPurchaseCount();
        productRepository.save(product);
    }
}