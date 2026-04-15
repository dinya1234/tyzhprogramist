package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreateOrderRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.*;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.OrderRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductItemRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Тесты")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductItemRepository productItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private SiteSettingsService siteSettingsService;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Order testOrder;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setStatus(OrderStatus.NEW);
        testOrder.setTotalPrice(new BigDecimal("5000.00"));

        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setDeliveryMethod("Курьер");
        createOrderRequest.setDeliveryAddress("ул. Тестовая, 1");
        createOrderRequest.setPaymentMethod("Картой онлайн");
        createOrderRequest.setComment("Тестовый комментарий");
    }

    @Test
    @DisplayName("ТЕСТ-1: getById - должен вернуть заказ при существующем ID")
    void getById_ShouldReturnOrder_WhenIdExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order result = orderService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    @DisplayName("ТЕСТ-2: getById - должен выбросить NotFoundException при несуществующем ID")
    void getById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Заказ не найден");
    }

    @Test
    @DisplayName("ТЕСТ-3: updateOrderStatus - должен обновить статус заказа")
    void updateOrderStatus_ShouldUpdateStatus_WhenCalled() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.updateOrderStatus(1L, OrderStatus.PAID);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(orderRepository, times(1)).save(testOrder);
    }
}