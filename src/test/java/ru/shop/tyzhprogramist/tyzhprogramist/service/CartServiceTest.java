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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Unit Tests")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductItemRepository productItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FileAttachmentService fileAttachmentService;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Cart testCart;
    private Product testProduct;
    private ProductItem testProductItem;
    private AddToCartRequest addToCartRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testCart = new Cart();
        testCart.setId(100L);
        testCart.setUser(testUser);
        testCart.setCreatedAt(LocalDateTime.now());

        testProduct = new Product();
        testProduct.setId(200L);
        testProduct.setName("Test Product");
        testProduct.setSlug("test-product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(50);

        testProductItem = new ProductItem();
        testProductItem.setId(300L);
        testProductItem.setParentId(100L);
        testProductItem.setParentType(ParentType.CART);
        testProductItem.setProduct(testProduct);
        testProductItem.setQuantity(2);
        testProductItem.setPrice(new BigDecimal("99.99"));

        addToCartRequest = new AddToCartRequest();
        addToCartRequest.setProductId(200L);
        addToCartRequest.setQuantity(1);
    }

    //1. ПОЗИТИВНЫЕ СЦЕНАРИИ

    @Test
    @DisplayName("Должен найти корзину пользователя, когда она существует")
    void shouldFindUserCart_whenCartExists() {
        when(cartRepository.findByUser(testUser)).thenReturn(List.of(testCart));

        Optional<Cart> result = cartService.findUserCart(testUser);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(100L);
        verify(cartRepository).findByUser(testUser);
    }

    @Test
    @DisplayName("Должен создать новую корзину для пользователя")
    void shouldCreateUserCart_whenUserDoesNotHaveCart() {
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.createUserCart(testUser);

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(testUser);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Должен добавить новый товар в корзину, когда товар еще не в корзине")
    void shouldAddNewProductToCart_whenProductNotExistsInCart() {
        when(cartRepository.findById(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(200L)).thenReturn(Optional.of(testProduct));
        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.empty());
        when(productItemRepository.save(any(ProductItem.class))).thenReturn(testProductItem);

        ProductItem result = cartService.addToCart(100L, addToCartRequest);

        assertThat(result).isNotNull();
        verify(productItemRepository).save(any(ProductItem.class));
    }

    @Test
    @DisplayName("Должен увеличить количество товара в корзине, когда товар уже существует")
    void shouldIncreaseQuantity_whenProductAlreadyInCart() {
        ProductItem existingItem = new ProductItem();
        existingItem.setId(300L);
        existingItem.setQuantity(1);
        existingItem.setProduct(testProduct);

        when(cartRepository.findById(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(200L)).thenReturn(Optional.of(testProduct));
        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.of(existingItem));
        when(productItemRepository.save(any(ProductItem.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductItem result = cartService.addToCart(100L, addToCartRequest);

        assertThat(result.getQuantity()).isEqualTo(2);
        verify(productItemRepository).save(any(ProductItem.class));
    }

    @Test
    @DisplayName("Должен объединить гостевую корзину с корзиной пользователя после логина")
    void shouldMergeGuestCartWithUserCart() {
        String sessionKey = "SESSION_TEST123";
        Cart guestCart = new Cart(sessionKey);
        guestCart.setId(999L);

        when(cartRepository.findGuestCartBySessionKey(sessionKey)).thenReturn(Optional.of(guestCart));
        when(cartRepository.findByUser(testUser)).thenReturn(List.of(testCart));
        when(productItemRepository.findCartItemsWithProduct(999L)).thenReturn(List.of(testProductItem));

        Cart result = cartService.mergeCarts(sessionKey, testUser);

        assertThat(result).isEqualTo(testCart);
        verify(productItemRepository).findCartItemsWithProduct(999L);
        verify(cartRepository).delete(guestCart);
    }

    //2. НЕГАТИВНЫЕ СЦЕНАРИИ

    @Test
    @DisplayName("Должен выбросить NotFoundException, когда корзина не найдена")
    void shouldThrowNotFoundException_whenCartNotFound() {
        when(cartRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.findById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Корзина не найдена с id: 999");
    }

    @Test
    @DisplayName("Должен выбросить NotFoundException, когда товар не найден при добавлении в корзину")
    void shouldThrowNotFoundException_whenProductNotFound() {
        when(cartRepository.findById(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(200L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addToCart(100L, addToCartRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Товар не найден с id: 200");
    }

    @Test
    @DisplayName("Должен выбросить BadRequestException, когда недостаточно товара на складе")
    void shouldThrowBadRequestException_whenInsufficientStock() {
        testProduct.setQuantity(0);
        when(cartRepository.findById(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(200L)).thenReturn(Optional.of(testProduct));

        assertThatThrownBy(() -> cartService.addToCart(100L, addToCartRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Недостаточное количество товара на складе");
    }

    @Test
    @DisplayName("Должен выбросить NotFoundException, когда товар не найден в корзине при обновлении количества")
    void shouldThrowNotFoundException_whenProductNotInCartForUpdate() {
        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCartItemQuantity(100L, 200L, 5))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Товар не найден в корзине");
    }

    @Test
    @DisplayName("Должен выбросить BadRequestException при обновлении количества, если недостаточно товара")
    void shouldThrowBadRequestException_whenInsufficientStockForUpdate() {
        testProduct.setQuantity(3);
        testProductItem.setProduct(testProduct);
        testProductItem.setQuantity(10);

        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.of(testProductItem));

        assertThatThrownBy(() -> cartService.updateCartItemQuantity(100L, 200L, 10))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Недостаточное количество товара на складе");
    }

    //3. ПАРАМЕТРИЗОВАННЫЕ ТЕСТЫ

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10})
    @DisplayName("Параметризованный тест: обновление количества товара с разными значениями")
    void shouldUpdateCartItemQuantity_withDifferentQuantities(int quantity) {
        testProduct.setQuantity(100);
        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.of(testProductItem));
        when(productItemRepository.save(any(ProductItem.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductItem result = cartService.updateCartItemQuantity(100L, 200L, quantity);

        assertThat(result.getQuantity()).isEqualTo(quantity);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 99.99",
            "2, 199.98",
            "3, 299.97",
            "5, 499.95"
    })
    @DisplayName("Параметризованный тест: расчет общей суммы корзины")
    void shouldCalculateTotalPrice_whenMultipleItems(int quantity, BigDecimal expectedTotal) {
        BigDecimal calculatedTotal = new BigDecimal("99.99").multiply(BigDecimal.valueOf(quantity));
        when(productItemRepository.sumCartTotal(100L)).thenReturn(calculatedTotal);

        BigDecimal result = cartService.getCartTotal(100L);

        assertThat(result).isEqualTo(calculatedTotal);
    }

    //4. VERIFY() ПРОВЕРКИ

    @Test
    @DisplayName("Должен вызвать repository.save() при создании корзины")
    void shouldCallRepositorySave_whenCreatingCart() {
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        cartService.createUserCart(testUser);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("НЕ должен вызывать repository.save() при обновлении, когда товара нет в корзине")
    void shouldNotCallRepositorySave_whenProductNotFoundInCart() {
        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCartItemQuantity(100L, 200L, 5))
                .isInstanceOf(NotFoundException.class);

        verify(productItemRepository, never()).save(any(ProductItem.class));
    }

    @Test
    @DisplayName("Должен вызвать productItemRepository.removeFromCart() при удалении товара")
    void shouldCallRemoveFromCart_whenRemovingProduct() {
        cartService.removeFromCart(100L, 200L);

        verify(productItemRepository, times(1)).removeFromCart(100L, 200L);
    }

    //5. ARGUMENT CAPTOR

    @Test
    @DisplayName("ArgumentCaptor: должен правильно сохранить ProductItem с нужными полями")
    void shouldCaptureAndVerifyProductItem_whenAddingToCart() {
        ArgumentCaptor<ProductItem> productItemCaptor = ArgumentCaptor.forClass(ProductItem.class);
        when(cartRepository.findById(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(200L)).thenReturn(Optional.of(testProduct));
        when(productItemRepository.findCartItem(100L, 200L)).thenReturn(Optional.empty());
        when(productItemRepository.save(productItemCaptor.capture())).thenReturn(testProductItem);

        cartService.addToCart(100L, addToCartRequest);

        ProductItem capturedItem = productItemCaptor.getValue();
        assertThat(capturedItem.getParentId()).isEqualTo(100L);
        assertThat(capturedItem.getParentType()).isEqualTo(ParentType.CART);
        assertThat(capturedItem.getProduct()).isEqualTo(testProduct);
        assertThat(capturedItem.getQuantity()).isEqualTo(1);
        assertThat(capturedItem.getPrice()).isEqualTo(testProduct.getPrice());
    }

    //6. ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ

    @Test
    @DisplayName("Должен вернуть пустой Optional, когда у пользователя нет корзины")
    void shouldReturnEmptyOptional_whenUserHasNoCart() {
        when(cartRepository.findByUser(testUser)).thenReturn(List.of());

        Optional<Cart> result = cartService.findUserCart(testUser);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен получить или создать корзину для пользователя")
    void shouldGetOrCreateUserCart() {
        when(cartRepository.findByUser(testUser)).thenReturn(List.of(testCart));

        Cart result = cartService.getOrCreateUserCart(testUser);

        assertThat(result).isEqualTo(testCart);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Должен создать гостевую корзину с новым sessionKey, если передан null")
    void shouldCreateGuestCartWithGeneratedSessionKey_whenSessionKeyIsNull() {
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        Cart result = cartService.createCartForGuest(null);

        assertThat(result.getSessionKey()).startsWith("SESSION_");
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Должен вернуть все позиции корзины с преобразованием в CartItemResponse")
    void shouldReturnCartItemsWithImages() {
        when(productItemRepository.findCartItemsWithProduct(100L)).thenReturn(List.of(testProductItem));
        when(fileAttachmentService.getMainImageUrlForEntity("Product", 200L)).thenReturn("/images/test.jpg");

        List<CartItemResponse> result = cartService.getCartItems(100L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(200L);
        assertThat(result.get(0).getProductName()).isEqualTo("Test Product");
        assertThat(result.get(0).getProductImage()).isEqualTo("/images/test.jpg");
    }

    @Test
    @DisplayName("Должен вернуть общее количество товаров в корзине")
    void shouldReturnTotalItemsCount() {
        when(productItemRepository.sumCartQuantity(100L)).thenReturn(5);

        Integer result = cartService.getCartItemsCount(100L);

        assertThat(result).isEqualTo(5);
    }

    @Test
    @DisplayName("Должен проверить доступность всех товаров в корзине")
    void shouldCheckAllItemsAvailability() {
        when(productItemRepository.areAllCartItemsAvailable(100L)).thenReturn(true);

        boolean result = cartService.areAllItemsAvailable(100L);

        assertThat(result).isTrue();
        verify(productItemRepository).areAllCartItemsAvailable(100L);
    }

    @Test
    @DisplayName("Должен вернуть статистику по корзинам")
    void shouldReturnCartStatistics() {
        when(cartRepository.countAuthorizedCarts()).thenReturn(10L);
        when(cartRepository.countGuestCarts()).thenReturn(5L);
        when(cartRepository.getCartCreationStatistics()).thenReturn(List.of());

        Object result = cartService.getCartStatistics();

        assertThat(result).isNotNull();
        verify(cartRepository).countAuthorizedCarts();
        verify(cartRepository).countGuestCarts();
        verify(cartRepository).getCartCreationStatistics();
    }

    @Test
    @DisplayName("Должен очистить корзину")
    void shouldClearCart() {
        cartService.clearCart(100L);

        verify(productItemRepository, times(1)).clearCart(100L);
    }
}