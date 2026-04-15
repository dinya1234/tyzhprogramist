package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.AddToCartRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CartItemResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Cart;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.CartService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты контроллера корзины")
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;
    private CartController cartController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        cartController = new CartController(cartService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    @DisplayName("GET /api/cart - успешное получение корзины авторизованного пользователя")
    void getCart_WithAuthenticatedUser_ShouldReturnCart() throws Exception {
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            SecurityUser securityUser = mock(SecurityUser.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(securityUser.getId()).thenReturn(1L);

            User testUser = new User();
            testUser.setId(1L);

            Cart testCart = new Cart(testUser);
            testCart.setId(1L);

            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setProductId(100L);
            itemResponse.setQuantity(2);

            when(userService.getById(1L)).thenReturn(testUser);
            when(cartService.getOrCreateUserCart(any(User.class))).thenReturn(testCart);
            when(cartService.getCartItems(anyLong())).thenReturn(List.of(itemResponse));
            when(cartService.getCartTotal(anyLong())).thenReturn(new BigDecimal("1999.98"));
            when(cartService.getCartItemsCount(anyLong())).thenReturn(2);
            when(cartService.findById(anyLong())).thenReturn(testCart);

            mockMvc.perform(get("/api/cart"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.totalItems").value(2));
        }
    }

    @Test
    @DisplayName("GET /api/cart - успешное получение корзины гостя (неавторизованный пользователь)")
    void getCart_WithGuestUser_ShouldReturnGuestCart() throws Exception {
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(null);

            Cart guestCart = new Cart("session123");
            guestCart.setId(2L);

            when(cartService.getGuestCart(anyString())).thenReturn(guestCart);
            when(cartService.getCartItems(anyLong())).thenReturn(List.of());
            when(cartService.getCartTotal(anyLong())).thenReturn(BigDecimal.ZERO);
            when(cartService.getCartItemsCount(anyLong())).thenReturn(0);
            when(cartService.findById(anyLong())).thenReturn(guestCart);

            mockMvc.perform(get("/api/cart")
                            .header("X-Session-Key", "session123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2L))
                    .andExpect(jsonPath("$.totalItems").value(0));
        }
    }

    @Test
    @DisplayName("POST /api/cart/add - успешное добавление товара в корзину авторизованным пользователем")
    void addToCart_WithValidRequest_ShouldReturnCreated() throws Exception {
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            SecurityUser securityUser = mock(SecurityUser.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(securityUser.getId()).thenReturn(1L);

            AddToCartRequest request = new AddToCartRequest();
            request.setProductId(100L);
            request.setQuantity(2);

            User testUser = new User();
            testUser.setId(1L);

            Cart testCart = new Cart(testUser);
            testCart.setId(1L);

            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setProductId(100L);
            itemResponse.setQuantity(2);

            when(userService.getById(1L)).thenReturn(testUser);
            when(cartService.getOrCreateUserCart(any(User.class))).thenReturn(testCart);
            when(cartService.getCartItems(anyLong())).thenReturn(List.of(itemResponse));

            mockMvc.perform(post("/api/cart/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.productId").value(100L))
                    .andExpect(jsonPath("$.quantity").value(2));

            verify(cartService).addToCart(eq(1L), any(AddToCartRequest.class));
        }
    }

    @Test
    @DisplayName("POST /api/cart/add - ошибка валидации при неверном количестве товара")
    void addToCart_WithInvalidQuantity_ShouldReturnBadRequest() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(100L);
        request.setQuantity(0);

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(cartService, never()).addToCart(anyLong(), any());
    }

    @Test
    @DisplayName("PUT /api/cart/update - успешное обновление количества товара авторизованным пользователем")
    void updateQuantity_WithValidQuantity_ShouldUpdate() throws Exception {
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            SecurityUser securityUser = mock(SecurityUser.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(securityUser.getId()).thenReturn(1L);

            User testUser = new User();
            testUser.setId(1L);

            Cart testCart = new Cart(testUser);
            testCart.setId(1L);

            when(userService.getById(1L)).thenReturn(testUser);
            when(cartService.getOrCreateUserCart(any(User.class))).thenReturn(testCart);

            mockMvc.perform(put("/api/cart/update")
                            .param("productId", "100")
                            .param("quantity", "5"))
                    .andExpect(status().isOk());

            verify(cartService).updateCartItemQuantity(eq(1L), eq(100L), eq(5));
        }
    }

    @Test
    @DisplayName("DELETE /api/cart/remove/{productId} - успешное удаление товара из корзины")
    void removeFromCart_ShouldRemoveProduct() throws Exception {
        try (MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            SecurityUser securityUser = mock(SecurityUser.class);

            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(securityUser);
            when(securityUser.getId()).thenReturn(1L);

            User testUser = new User();
            testUser.setId(1L);

            Cart testCart = new Cart(testUser);
            testCart.setId(1L);

            when(userService.getById(1L)).thenReturn(testUser);
            when(cartService.getOrCreateUserCart(any(User.class))).thenReturn(testCart);

            mockMvc.perform(delete("/api/cart/remove/100"))
                    .andExpect(status().isNoContent());

            verify(cartService).removeFromCart(eq(1L), eq(100L));
        }
    }
}