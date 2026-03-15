package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.AddToCartRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CartItemResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.*;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.CartRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductItemRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final FileAttachmentService fileAttachmentService;

    @Transactional(readOnly = true)
    public Cart getUserCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> createCartForUser(user));
    }

    @Transactional(readOnly = true)
    public Cart getGuestCart(String sessionKey) {
        return cartRepository.findBySessionKey(sessionKey)
                .orElseGet(() -> createCartForGuest(sessionKey));
    }

    @Transactional
    public Cart createCartForUser(User user) {
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart createCartForGuest(String sessionKey) {
        if (sessionKey == null || sessionKey.isEmpty()) {
            sessionKey = generateSessionKey();
        }
        Cart cart = new Cart(sessionKey);
        return cartRepository.save(cart);
    }

    private String generateSessionKey() {
        return "SESSION_" + UUID.randomUUID().toString();
    }

    @Transactional
    public ProductItem addToCart(Long cartId, AddToCartRequest request) {
        Cart cart = findById(cartId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Товар не найден с id: " + request.getProductId()));

        if (product.getQuantity() < request.getQuantity()) {
            throw new BadRequestException("Недостаточное количество товара на складе");
        }

        Optional<ProductItem> existingItem = productItemRepository.findCartItem(cartId, product.getId());

        if (existingItem.isPresent()) {
            ProductItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (product.getQuantity() < newQuantity) {
                throw new BadRequestException("Недостаточное количество товара на складе");
            }
            item.setQuantity(newQuantity);
            return productItemRepository.save(item);
        } else {
            ProductItem newItem = new ProductItem(
                    cartId,
                    product,
                    request.getQuantity(),
                    product.getPrice()
            );
            newItem.setParentType(ParentType.CART);
            return productItemRepository.save(newItem);
        }
    }

    @Transactional
    public ProductItem updateCartItemQuantity(Long cartId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(cartId, productId);
            return null;
        }

        ProductItem item = productItemRepository.findCartItem(cartId, productId)
                .orElseThrow(() -> new NotFoundException("Товар не найден в корзине"));

        Product product = item.getProduct();
        if (product.getQuantity() < quantity) {
            throw new BadRequestException("Недостаточное количество товара на складе");
        }

        item.setQuantity(quantity);
        return productItemRepository.save(item);
    }

    @Transactional
    public void removeFromCart(Long cartId, Long productId) {
        productItemRepository.removeFromCart(cartId, productId);
    }

    @Transactional
    public void clearCart(Long cartId) {
        productItemRepository.clearCart(cartId);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long cartId) {
        List<ProductItem> items = productItemRepository.findCartItemsWithProduct(cartId);

        return items.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    String imageUrl = fileAttachmentService.getMainImageUrlForEntity("Product", product.getId());
                    return CartItemResponse.from(item, product, imageUrl);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getCartTotal(Long cartId) {
        return productItemRepository.sumCartTotal(cartId);
    }

    @Transactional(readOnly = true)
    public Integer getCartItemsCount(Long cartId) {
        return productItemRepository.sumCartQuantity(cartId);
    }

    @Transactional(readOnly = true)
    public boolean areAllItemsAvailable(Long cartId) {
        return productItemRepository.areAllCartItemsAvailable(cartId);
    }

    @Transactional
    public Cart mergeCarts(String sessionKey, User user) {
        Cart guestCart = cartRepository.findGuestCartBySessionKey(sessionKey)
                .orElse(null);

        if (guestCart == null) {
            return getUserCart(user);
        }

        Cart userCart = getUserCart(user);

        List<ProductItem> guestItems = productItemRepository.findCartItemsWithProduct(guestCart.getId());

        for (ProductItem guestItem : guestItems) {
            AddToCartRequest request = new AddToCartRequest();
            request.setProductId(guestItem.getProduct().getId());
            request.setQuantity(guestItem.getQuantity());

            try {
                addToCart(userCart.getId(), request);
            } catch (Exception e) {
                log.warn("Не удалось перенести товар {} в корзину пользователя: {}",
                        guestItem.getProduct().getId(), e.getMessage());
            }
        }

        cartRepository.delete(guestCart);

        return userCart;
    }

    @Transactional(readOnly = true)
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Корзина не найдена с id: " + id));
    }

    @Transactional
    public int deleteOldGuestCarts(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        return cartRepository.deleteOldGuestCarts(threshold);
    }

    @Transactional(readOnly = true)
    public Object getCartStatistics() {
        return Map.of(
                "totalAuthorized", cartRepository.countAuthorizedCarts(),
                "totalGuest", cartRepository.countGuestCarts(),
                "creationStats", cartRepository.getCartCreationStatistics()
        );
    }
}