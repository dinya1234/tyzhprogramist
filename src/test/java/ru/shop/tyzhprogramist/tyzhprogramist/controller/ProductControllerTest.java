package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.shop.tyzhprogramist.tyzhprogramist.TyzhprogramistApplication;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFullResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.security.JwtAuthenticationFilter;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ProductService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.auth.JwtTokenProvider;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = {
        "spring.liquibase.enabled=false",
        "vite.enabled=false",
        "vite.server.enabled=false"
})
@WebMvcTest(controllers = {ProductController.class})
@ContextConfiguration(classes = {TyzhprogramistApplication.class})
@DisplayName("ProductController REST API тесты")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ProductFullResponse mockProductFullResponse;
    private ProductResponse mockProductResponse;

    @BeforeEach
    void setUp() {
        mockProductFullResponse = new ProductFullResponse();
        mockProductFullResponse.setId(100L);
        mockProductFullResponse.setName("Test Product");
        mockProductFullResponse.setSku("SKU001");
        mockProductFullResponse.setSlug("test-product");
        mockProductFullResponse.setShortDescription("Short description");
        mockProductFullResponse.setFullDescription("Full description");
        mockProductFullResponse.setPrice(new BigDecimal("99.99"));
        mockProductFullResponse.setOldPrice(new BigDecimal("129.99"));
        mockProductFullResponse.setQuantity(50);
        mockProductFullResponse.setIsActive(true);
        mockProductFullResponse.setIsNew(true);
        mockProductFullResponse.setIsBestseller(false);
        mockProductFullResponse.setImages(List.of("/image1.jpg", "/image2.jpg"));
        mockProductFullResponse.setMainImage("/image1.jpg");
        mockProductFullResponse.setRelatedProducts(List.of());
        mockProductFullResponse.setFrequentlyBought(List.of());

        mockProductResponse = new ProductResponse();
        mockProductResponse.setId(100L);
        mockProductResponse.setName("Test Product");
        mockProductResponse.setSlug("test-product");
        mockProductResponse.setPrice(new BigDecimal("99.99"));
        mockProductResponse.setMainImage("/image1.jpg");
    }

    // ==================== 1. ПОЗИТИВНЫЙ СЦЕНАРИЙ (200 OK + jsonPath) ====================

    @Test
    @DisplayName("GET /api/products/{id} - успешный запрос (200)")
    @WithMockUser
    void shouldReturnProductById_whenProductExists() throws Exception {
        when(productService.getProductFullResponse(100L)).thenReturn(mockProductFullResponse);

        mockMvc.perform(get("/api/products/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.sku").value("SKU001"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.mainImage").value("/image1.jpg"))
                .andExpect(jsonPath("$.images", org.hamcrest.Matchers.hasSize(2)));
    }

    // ==================== 2. НЕГАТИВНЫЙ СЦЕНАРИЙ (404 Not Found) ====================

    @Test
    @DisplayName("GET /api/products/{id} - 404 товар не найден")
    @WithMockUser
    void shouldReturn404_whenProductNotFound() throws Exception {
        when(productService.getProductFullResponse(999L))
                .thenThrow(new NotFoundException("Товар не найден с id: 999"));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Товар не найден с id: 999"));
    }

    // ==================== 3. ПРОВЕРКА СТРУКТУРЫ ОТВЕТА (наличие всех полей) ====================

    @Test
    @DisplayName("GET /api/products/{id} - проверка наличия всех полей в ответе")
    @WithMockUser
    void shouldHaveAllRequiredFieldsInResponse() throws Exception {
        when(productService.getProductFullResponse(100L)).thenReturn(mockProductFullResponse);

        mockMvc.perform(get("/api/products/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.sku").exists())
                .andExpect(jsonPath("$.slug").exists())
                .andExpect(jsonPath("$.shortDescription").exists())
                .andExpect(jsonPath("$.fullDescription").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.oldPrice").exists())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$.isActive").exists())
                .andExpect(jsonPath("$.isNew").exists())
                .andExpect(jsonPath("$.isBestseller").exists())
                .andExpect(jsonPath("$.images").exists())
                .andExpect(jsonPath("$.mainImage").exists());
    }

    // ==================== 4. ТЕСТИРОВАНИЕ ОШИБОК (ControllerAdvice) ====================

    @Test
    @DisplayName("ControllerAdvice - проверка ErrorResponse при 404")
    @WithMockUser
    void shouldReturnErrorResponseFormat_whenNotFoundException() throws Exception {
        when(productService.getProductFullResponse(999L))
                .thenThrow(new NotFoundException("Товар с id 999 не найден"));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Товар с id 999 не найден"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ==================== 5. GET ALL PRODUCTS (проверка пагинации и структуры) ====================

    @Test
    @DisplayName("GET /api/products - успешный запрос с пагинацией")
    @WithMockUser
    void shouldReturnAllProductsWithPagination() throws Exception {
        org.springframework.data.domain.Page<Product> mockPage =
                new org.springframework.data.domain.PageImpl<>(List.of(new Product()));

        when(productService.getAllAvailableProducts(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }
}