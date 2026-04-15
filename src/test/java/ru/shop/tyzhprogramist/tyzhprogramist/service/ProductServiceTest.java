package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Тесты")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private FileAttachmentService fileAttachmentService;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Тестовый товар");
        testProduct.setSku("TEST-SKU-001");
        testProduct.setSlug("test-product");
        testProduct.setPrice(new BigDecimal("1500.00"));
        testProduct.setQuantity(50);
    }

    @Test
    @DisplayName("ТЕСТ-1: getById - должен вернуть товар при существующем ID")
    void getById_ShouldReturnProduct_WhenIdExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Тестовый товар");
        assertThat(result.getSku()).isEqualTo("TEST-SKU-001");
    }

    @Test
    @DisplayName("ТЕСТ-2: getBySlug - должен вернуть товар при существующем slug")
    void getBySlug_ShouldReturnProduct_WhenSlugExists() {
        when(productRepository.findBySlug("test-product")).thenReturn(Optional.of(testProduct));

        Product result = productService.getBySlug("test-product");

        assertThat(result).isNotNull();
        assertThat(result.getSlug()).isEqualTo("test-product");
    }

    @Test
    @DisplayName("ТЕСТ-3: getBySlug - должен выбросить NotFoundException при несуществующем slug")
    void getBySlug_ShouldThrowNotFoundException_WhenSlugDoesNotExist() {
        when(productRepository.findBySlug("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getBySlug("non-existent"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Товар не найден");
    }
}