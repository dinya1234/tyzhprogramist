package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ProductFullResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Product;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты контроллера товаров")
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private ObjectMapper objectMapper;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        productController = new ProductController(productService);

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("GET /api/products - получение списка товаров с пагинацией")
    void getAllProducts_ShouldReturnPageOfProducts() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Электроника");

        Product product = new Product();
        product.setId(1L);
        product.setName("Игровой ноутбук");
        product.setSlug("gaming-laptop");
        product.setPrice(new BigDecimal("1500.00"));
        product.setQuantity(10);
        product.setCategory(category);

        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productService.getAllAvailableProducts(any(Pageable.class))).thenReturn(productPage);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Игровой ноутбук"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - получение товара по идентификатору")
    void getProduct_WithValidId_ShouldReturnProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("Игровой ноутбук");
        product.setCategory(category);

        ProductFullResponse response = ProductFullResponse.from(product);
        when(productService.getProductFullResponse(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Игровой ноутбук"));
    }

    @Test
    @DisplayName("GET /api/products/slug/{slug} - получение товара по ЧПУ-ссылке")
    void getProductBySlug_WithValidSlug_ShouldReturnProduct() throws Exception {
        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setSlug("gaming-laptop");
        product.setCategory(category);

        when(productService.getBySlug(anyString())).thenReturn(product);

        mockMvc.perform(get("/api/products/slug/gaming-laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slug").value("gaming-laptop"));
    }
}