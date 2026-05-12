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
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.AddToCartRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CartItemResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ProductItem;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.security.JwtAuthenticationFilter;
import ru.shop.tyzhprogramist.tyzhprogramist.service.CartService;
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
@WebMvcTest(controllers = {CartController.class})
@ContextConfiguration(classes = {TyzhprogramistApplication.class})
@DisplayName("CartController REST API тесты")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private CartItemResponse mockCartItem;

    @BeforeEach
    void setUp() {
        mockCartItem = new CartItemResponse();
        mockCartItem.setProductId(1L);
        mockCartItem.setProductName("Test Product");
        mockCartItem.setProductImage("/images/test.jpg");
        mockCartItem.setQuantity(2);
        mockCartItem.setPrice(new BigDecimal("99.99"));

    }

    //ТЕСТЫ С @WithMockUser

    @Test
    @DisplayName("GET /api/cart/{cartId} - успешный запрос (200)")
    @WithMockUser
    void shouldReturnCartItems_whenCartExists() throws Exception {
        when(cartService.getCartItems(100L)).thenReturn(List.of(mockCartItem));

        mockMvc.perform(get("/api/cart/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @Test
    @DisplayName("POST /api/cart/{cartId}/add - успешное добавление (201)")
    @WithMockUser
    void shouldAddToCart_whenValidData() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(200L);
        request.setQuantity(1);

        ProductItem mockItem = new ProductItem();
        mockItem.setId(300L);

        when(cartService.addToCart(eq(100L), any(AddToCartRequest.class)))
                .thenReturn(mockItem);

        mockMvc.perform(post("/api/cart/100/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(300L));
    }

    @Test
    @DisplayName("GET /api/cart/{cartId} - 404 корзина не найдена")
    @WithMockUser
    void shouldReturn404_whenCartNotFound() throws Exception {
        when(cartService.getCartItems(999L))
                .thenThrow(new NotFoundException("Корзина не найдена с id: 999"));

        mockMvc.perform(get("/api/cart/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Корзина не найдена с id: 999"));
    }

    @Test
    @DisplayName("POST /api/cart/{cartId}/add - 400 недостаточно товара")
    @WithMockUser
    void shouldReturn400_whenInsufficientStock() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(200L);
        request.setQuantity(100);

        when(cartService.addToCart(eq(100L), any(AddToCartRequest.class)))
                .thenThrow(new BadRequestException("Недостаточно товара на складе"));

        mockMvc.perform(post("/api/cart/100/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Недостаточно товара на складе"));
    }

    @Test
    @DisplayName("GET /api/cart/{cartId} - проверка наличия всех полей")
    @WithMockUser
    void shouldHaveAllRequiredFieldsInResponse() throws Exception {
        when(cartService.getCartItems(100L)).thenReturn(List.of(mockCartItem));

        mockMvc.perform(get("/api/cart/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").exists())
                .andExpect(jsonPath("$[0].productName").exists())
                .andExpect(jsonPath("$[0].productImage").exists())
                .andExpect(jsonPath("$[0].quantity").exists())
                .andExpect(jsonPath("$[0].price").exists());
    }

    @Test
    @DisplayName("ControllerAdvice - проверка ErrorResponse при 404")
    @WithMockUser
    void shouldReturnErrorResponseFormat_whenNotFoundException() throws Exception {
        when(cartService.getCartItems(999L))
                .thenThrow(new NotFoundException("Корзина с id 999 не найдена"));

        mockMvc.perform(get("/api/cart/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Корзина с id 999 не найдена"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("ControllerAdvice - проверка ErrorResponse при 400")
    @WithMockUser
    void shouldReturnErrorResponseFormat_whenBadRequestException() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(200L);
        request.setQuantity(100);

        when(cartService.addToCart(eq(100L), any(AddToCartRequest.class)))
                .thenThrow(new BadRequestException("Недостаточно товара на складе"));

        mockMvc.perform(post("/api/cart/100/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Недостаточно товара на складе"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    //ТЕСТЫ БЕЗОПАСНОСТИ

    @Test
    @DisplayName("Security: GET /api/cart/{cartId} - доступ разрешён с аутентификацией")
    @WithMockUser
    void shouldAllowAccess_whenAuthenticated() throws Exception {
        when(cartService.getCartItems(100L)).thenReturn(List.of(mockCartItem));

        mockMvc.perform(get("/api/cart/100"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Security: GET /api/cart/{cartId} - доступ запрещён без аутентификации")
    void shouldDenyAccess_whenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/cart/100"))
                .andExpect(status().isUnauthorized());
    }
}