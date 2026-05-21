package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFullResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private FileAttachmentService fileAttachmentService;

    @InjectMocks
    private ProductService productService;

    private Category testCategory;
    private Product testProduct;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setSlug("electronics");

        testProduct = new Product();
        testProduct.setId(100L);
        testProduct.setName("Test Product");
        testProduct.setSku("SKU001");
        testProduct.setSlug("test-product");
        testProduct.setShortDescription("Short desc");
        testProduct.setFullDescription("Full desc");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setOldPrice(new BigDecimal("129.99"));
        testProduct.setQuantity(50);
        testProduct.setIsActive(true);
        testProduct.setIsNew(true);
        testProduct.setIsBestseller(false);
        testProduct.setWarrantyMonths(12);
        testProduct.setWeight(new BigDecimal("1.5"));
        testProduct.setCategory(testCategory);
        testProduct.setRating(0.0);

        // Используем HashSet, так как relatedProducts это Set<Product>
        testProduct.setRelatedProducts(new HashSet<>());
        testProduct.setFrequentlyBoughtWith(new HashSet<>());

        pageable = PageRequest.of(0, 10);
    }

    // ==================== 1. ПОЗИТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    @DisplayName("Должен найти товар по id, когда он существует")
    void shouldFindProductById_whenProductExists() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getById(100L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getSku()).isEqualTo("SKU001");
        verify(productRepository).findById(100L);
    }

    @Test
    @DisplayName("Должен найти товар по slug, когда он существует")
    void shouldFindProductBySlug_whenProductExists() {
        when(productRepository.findBySlug("test-product")).thenReturn(Optional.of(testProduct));

        Product result = productService.getBySlug("test-product");

        assertThat(result).isNotNull();
        assertThat(result.getSlug()).isEqualTo("test-product");
    }

    @Test
    @DisplayName("Должен найти товар по sku, когда он существует")
    void shouldFindProductBySku_whenProductExists() {
        when(productRepository.findBySku("SKU001")).thenReturn(Optional.of(testProduct));

        Product result = productService.getBySku("SKU001");

        assertThat(result).isNotNull();
        assertThat(result.getSku()).isEqualTo("SKU001");
    }

    @Test
    @DisplayName("Должен создать новый товар с валидными данными")
    void shouldCreateProduct_whenValidData() {
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.existsBySlug(anyString())).thenReturn(false);
        when(categoryService.getById(1L)).thenReturn(testCategory);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(testCategory);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Должен обновить существующий товар с валидными данными")
    void shouldUpdateProduct_whenValidData() {
        Product updatedProduct = new Product();
        updatedProduct.setSku("SKU002");
        updatedProduct.setSlug("updated-slug");
        updatedProduct.setName("Updated Name");
        updatedProduct.setPrice(new BigDecimal("149.99"));
        updatedProduct.setQuantity(30);

        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.existsBySku("SKU002")).thenReturn(false);
        when(productRepository.existsBySlug("updated-slug")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.updateProduct(100L, updatedProduct, null);

        assertThat(result).isNotNull();
        verify(productRepository).save(testProduct);
    }

    // ==================== 2. НЕГАТИВНЫЕ СЦЕНАРИИ ====================

    @Test
    @DisplayName("Должен выбросить NotFoundException, когда товар не найден по id")
    void shouldThrowNotFoundException_whenProductNotFoundById() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Товар не найден с id: 999");
    }

    @Test
    @DisplayName("Должен выбросить NotFoundException, когда товар не найден по slug")
    void shouldThrowNotFoundException_whenProductNotFoundBySlug() {
        when(productRepository.findBySlug("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getBySlug("nonexistent"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Товар не найден: nonexistent");
    }

    @Test
    @DisplayName("Должен выбросить NotFoundException, когда товар не найден по sku")
    void shouldThrowNotFoundException_whenProductNotFoundBySku() {
        when(productRepository.findBySku("WRONG_SKU")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getBySku("WRONG_SKU"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Товар не найден с артикулом: WRONG_SKU");
    }

    @Test
    @DisplayName("Должен выбросить BadRequestException, когда sku уже существует при создании")
    void shouldThrowBadRequestException_whenDuplicateSkuOnCreate() {
        when(productRepository.existsBySku("SKU001")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(testProduct, 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Товар с таким артикулом уже существует");
    }

    @Test
    @DisplayName("Должен выбросить BadRequestException, когда slug уже существует при создании")
    void shouldThrowBadRequestException_whenDuplicateSlugOnCreate() {
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.existsBySlug("test-product")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(testProduct, 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Товар с таким slug уже существует");
    }

    // ==================== 3. ПАРАМЕТРИЗОВАННЫЕ ТЕСТЫ ====================

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "not-exists", "very-long-slug"})
    @DisplayName("Параметризованный тест: поиск по slug с разными некорректными значениями")
    void shouldThrowNotFoundException_forInvalidSlugs(String invalidSlug) {
        when(productRepository.findBySlug(invalidSlug)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getBySlug(invalidSlug))
                .isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 100",
            "100, 500",
            "500, 1000"
    })
    @DisplayName("Параметризованный тест: поиск товаров по диапазону цен")
    void shouldGetProductsByPriceRange(BigDecimal min, BigDecimal max) {
        Page<Product> mockPage = new PageImpl<>(List.of(testProduct), pageable, 1);

        when(productRepository.findByPriceRange(eq(min), eq(max), any(Pageable.class)))
                .thenReturn(mockPage);

        Page<Product> result = productService.getProductsByPriceRange(min, max, pageable);

        assertThat(result).isNotNull();
        verify(productRepository).findByPriceRange(eq(min), eq(max), any(Pageable.class));
    }

    // ==================== 4. VERIFY() ПРОВЕРКИ ====================

    @Test
    @DisplayName("Должен вызвать repository.save() при увеличении просмотров")
    void shouldCallRepositorySave_whenIncrementViews() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.incrementViews(100L);

        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Должен вызвать categoryService.getById() при создании товара")
    void shouldCallCategoryService_whenCreatingProduct() {
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.existsBySlug(anyString())).thenReturn(false);
        when(categoryService.getById(1L)).thenReturn(testCategory);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.createProduct(testProduct, 1L);

        verify(categoryService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("НЕ должен вызывать repository.save() при ошибке создания товара")
    void shouldNotCallRepositorySave_whenDuplicateSkuOnCreate() {
        when(productRepository.existsBySku("SKU001")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(testProduct, 1L))
                .isInstanceOf(BadRequestException.class);

        verify(productRepository, never()).save(any(Product.class));
    }

    // ==================== 5. ARGUMENT CAPTOR ====================

    @Test
    @DisplayName("ArgumentCaptor: должен правильно сохранить товар с нужными полями при создании")
    void shouldCaptureAndVerifyProduct_whenCreatingProduct() {
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.existsBySlug(anyString())).thenReturn(false);
        when(categoryService.getById(1L)).thenReturn(testCategory);
        when(productRepository.save(productCaptor.capture())).thenReturn(testProduct);

        productService.createProduct(testProduct, 1L);

        Product capturedProduct = productCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo(testProduct.getName());
        assertThat(capturedProduct.getSku()).isEqualTo(testProduct.getSku());
        assertThat(capturedProduct.getCategory()).isEqualTo(testCategory);
    }

    // ==================== 6. ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ДЛЯ ПОКРЫТИЯ ====================

    @Test
    @DisplayName("Должен мягко удалить товар (setIsActive=false)")
    void shouldSoftDeleteProduct() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.deleteProduct(100L);

        assertThat(testProduct.getIsActive()).isFalse();
        verify(productRepository).save(testProduct);
    }

    @Test
    @DisplayName("Должен жестко удалить товар из БД")
    void shouldHardDeleteProduct() {
        doNothing().when(productRepository).deleteById(100L);

        productService.hardDeleteProduct(100L);

        verify(productRepository, times(1)).deleteById(100L);
    }

    @Test
    @DisplayName("Должен обновить рейтинг товара")
    void shouldUpdateProductRating() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateProductRating(100L, 4.8);

        assertThat(testProduct.getRating()).isEqualTo(4.8);
        verify(productRepository).save(testProduct);
    }

    @Test
    @DisplayName("Должен добавить связанный товар")
    void shouldAddRelatedProduct() {
        Product relatedProduct = new Product();
        relatedProduct.setId(200L);
        relatedProduct.setName("Related Product");

        when(productRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(productRepository.findById(200L)).thenReturn(Optional.of(relatedProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.addRelatedProduct(100L, 200L);

        verify(productRepository).save(testProduct);
    }

    @Test
    @DisplayName("Должен вернуть страницу активных товаров по категории")
    void shouldReturnPageOfActiveProductsByCategory() {
        List<Product> products = List.of(testProduct);
        Page<Product> mockPage = new PageImpl<>(products, pageable, 1);

        when(productRepository.findByCategoryIdAndIsActiveTrue(eq(1L), any(Pageable.class)))
                .thenReturn(mockPage);

        Page<Product> result = productService.getProductsByCategory(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Должен вернуть список новых товаров с ограничением")
    void shouldReturnNewProductsWithLimit() {
        List<Product> products = List.of(testProduct);
        when(productRepository.findNewProducts(any(Pageable.class))).thenReturn(products);
        when(fileAttachmentService.getMainImageUrlForEntity("Product", 100L)).thenReturn("/image.jpg");

        List<ProductResponse> result = productService.getNewProducts(5);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMainImage()).isEqualTo("/image.jpg");
    }

    @Test
    @DisplayName("Должен вернуть список бестселлеров с ограничением")
    void shouldReturnBestsellersWithLimit() {
        List<Product> products = List.of(testProduct);
        when(productRepository.findBestsellers(any(Pageable.class))).thenReturn(products);
        when(fileAttachmentService.getMainImageUrlForEntity("Product", 100L)).thenReturn("/image.jpg");

        List<ProductResponse> result = productService.getBestsellers(5);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Должен вернуть список самых просматриваемых товаров")
    void shouldReturnMostViewedProducts() {
        List<Product> products = List.of(testProduct);
        when(productRepository.findMostViewed(any(Pageable.class))).thenReturn(products);
        when(fileAttachmentService.getMainImageUrlForEntity("Product", 100L)).thenReturn("/image.jpg");

        List<ProductResponse> result = productService.getMostViewedProducts(5);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Должен вернуть список самых покупаемых товаров")
    void shouldReturnMostPurchasedProducts() {
        List<Product> products = List.of(testProduct);
        when(productRepository.findMostPurchased(any(Pageable.class))).thenReturn(products);
        when(fileAttachmentService.getMainImageUrlForEntity("Product", 100L)).thenReturn("/image.jpg");

        List<ProductResponse> result = productService.getMostPurchasedProducts(5);

        assertThat(result).hasSize(1);
    }
}