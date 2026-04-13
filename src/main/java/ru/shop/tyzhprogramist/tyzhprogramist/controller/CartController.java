package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.AddToCartRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CartItemResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CartResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Cart;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.security.SecurityUser;
import ru.shop.tyzhprogramist.tyzhprogramist.service.CartService;
import ru.shop.tyzhprogramist.tyzhprogramist.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    private Long getCurrentUserId() {
        try {
            SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return principal != null ? principal.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getSessionKey(HttpServletRequest request) {
        String sessionKey = request.getHeader("X-Session-Key");
        if (sessionKey == null || sessionKey.isEmpty()) {
            sessionKey = request.getSession(true).getId();
        }
        return sessionKey;
    }

    private Long getCartId(Long userId, String sessionKey) {
        if (userId != null) {
            User user = userService.getById(userId);
            return cartService.getOrCreateUserCart(user).getId();
        }
        return cartService.getGuestCart(sessionKey).getId();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated() or true")
    public ResponseEntity<CartResponse> getCart(HttpServletRequest request) {
        Long userId = getCurrentUserId();
        String sessionKey = getSessionKey(request);
        Long cartId = getCartId(userId, sessionKey);

        List<CartItemResponse> items = cartService.getCartItems(cartId);
        BigDecimal total = cartService.getCartTotal(cartId);
        Integer totalItems = cartService.getCartItemsCount(cartId);
        Cart cart = cartService.findById(cartId);

        return ResponseEntity.ok(CartResponse.from(cart, items, total, totalItems));
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() or true")
    public ResponseEntity<CartItemResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId();
        String sessionKey = getSessionKey(httpRequest);
        Long cartId = getCartId(userId, sessionKey);

        cartService.addToCart(cartId, request);

        CartItemResponse response = cartService.getCartItems(cartId).stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated() or true")
    public ResponseEntity<Void> updateQuantity(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            HttpServletRequest request) {
        Long userId = getCurrentUserId();
        String sessionKey = getSessionKey(request);
        Long cartId = getCartId(userId, sessionKey);

        if (quantity <= 0) {
            cartService.removeFromCart(cartId, productId);
        } else {
            cartService.updateCartItemQuantity(cartId, productId, quantity);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() or true")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId, HttpServletRequest request) {
        Long userId = getCurrentUserId();
        String sessionKey = getSessionKey(request);
        Long cartId = getCartId(userId, sessionKey);

        cartService.removeFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() or true")
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        Long userId = getCurrentUserId();
        String sessionKey = getSessionKey(request);
        Long cartId = getCartId(userId, sessionKey);

        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}