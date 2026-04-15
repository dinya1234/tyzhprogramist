package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.AddToCartRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.*;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.CartRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductItemRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Тесты")
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
    private AddToCartRequest addToCartRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testCart = new Cart(testUser);
        testCart.setId(1L);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Тестовый товар");
        testProduct.setQuantity(100);
        testProduct.setPrice(new BigDecimal("1000.00"));

        addToCartRequest = new AddToCartRequest();
        addToCartRequest.setProductId(1L);
        addToCartRequest.setQuantity(2);
    }

    @Test
    @DisplayName("ТЕСТ-1: findUserCart - должен вернуть существующую корзину пользователя")
    void findUserCart_ShouldReturnExistingCart_WhenCartExists() {
        when(cartRepository.findByUser(testUser)).thenReturn(java.util.List.of(testCart));

        Optional<Cart> result = cartService.findUserCart(testUser);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testCart.getId());
        verify(cartRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("ТЕСТ-2: createUserCart - должен создать новую корзину для пользователя")
    void createUserCart_ShouldCreateNewCart_WhenCalled() {
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.createUserCart(testUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testCart.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("ТЕСТ-3: addToCart - должен выбросить исключение при недостатке товара на складе")
    void addToCart_ShouldThrowException_WhenInsufficientStock() {
        testProduct.setQuantity(1);
        addToCartRequest.setQuantity(5);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThatThrownBy(() -> cartService.addToCart(1L, addToCartRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Недостаточное количество товара");

        verify(productItemRepository, never()).save(any(ProductItem.class));
    }
}