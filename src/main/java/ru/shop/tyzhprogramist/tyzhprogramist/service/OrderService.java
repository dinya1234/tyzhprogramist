package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreateOrderRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.OrderItemResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.OrderResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.*;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.OrderRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductItemRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final SiteSettingsService siteSettingsService;

    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден с id: " + id));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponseById(Long id) {
        Order order = getById(id);
        return buildOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrderResponses(Pageable pageable) {
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::buildOrderResponse);
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Page<Order> getUserOrders(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> getUserOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrderResponses(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::buildOrderResponse);
    }

    @Transactional(readOnly = true)
    public List<Order> getUserRecentOrders(Long userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return orderRepository.findUserRecentOrders(userId, since);
    }

    @Transactional(readOnly = true)
    public long countUserOrders(User user) {
        return orderRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public long countUserOrdersByStatus(User user, OrderStatus status) {
        return orderRepository.countByUserAndStatus(user, status);
    }

    @Transactional
    public Order createOrderFromCart(User user, CreateOrderRequest request) {
        Cart cart = cartService.getOrCreateUserCart(user);

        List<ProductItem> cartItems = productItemRepository.findCartItemsWithProduct(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Корзина пуста");
        }

        for (ProductItem item : cartItems) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new BadRequestException(
                        "Товар " + product.getName() + " недоступен в нужном количестве. " +
                                "Доступно: " + product.getQuantity()
                );
            }
        }

        BigDecimal totalPrice = calculateTotalPrice(cartItems);

        SiteSettings settings = siteSettingsService.getSettings();
        if (!siteSettingsService.isFreeDeliveryEligible(totalPrice)) {
            totalPrice = totalPrice.add(settings.getDeliveryCost());
        }

        Order order = new Order(
                user,
                request.getDeliveryMethod(),
                request.getDeliveryAddress(),
                request.getPaymentMethod(),
                totalPrice,
                request.getComment()
        );

        Order savedOrder = orderRepository.save(order);

        for (ProductItem cartItem : cartItems) {
            ProductItem orderItem = new ProductItem(
                    savedOrder.getId(),
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getPrice()
            );
            orderItem.setParentType(ParentType.ORDER);
            productItemRepository.save(orderItem);

            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            product.incrementPurchaseCount();
            productRepository.save(product);
        }

        productItemRepository.clearCart(cart.getId());

        log.info("Создан новый заказ #{}. Пользователь: {}", savedOrder.getId(), user.getUsername());

        return savedOrder;
    }

    @Transactional
    public Order createOrderForGuest(String sessionKey, String email, String phone,
                                     CreateOrderRequest request) {
        Cart cart = cartService.getGuestCart(sessionKey);

        List<ProductItem> cartItems = productItemRepository.findCartItemsWithProduct(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Корзина пуста");
        }

        for (ProductItem item : cartItems) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new BadRequestException(
                        "Товар " + product.getName() + " недоступен в нужном количестве"
                );
            }
        }

        BigDecimal totalPrice = calculateTotalPrice(cartItems);

        SiteSettings settings = siteSettingsService.getSettings();
        if (!siteSettingsService.isFreeDeliveryEligible(totalPrice)) {
            totalPrice = totalPrice.add(settings.getDeliveryCost());
        }

        Order order = new Order();
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setTotalPrice(totalPrice);
        order.setComment(request.getComment());
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (ProductItem cartItem : cartItems) {
            ProductItem orderItem = new ProductItem(
                    savedOrder.getId(),
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getPrice()
            );
            orderItem.setParentType(ParentType.ORDER);
            productItemRepository.save(orderItem);

            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            product.incrementPurchaseCount();
            productRepository.save(product);
        }
        productItemRepository.clearCart(cart.getId());

        log.info("Создан новый гостевой заказ #{}", savedOrder.getId());

        return savedOrder;
    }

    private BigDecimal calculateTotalPrice(List<ProductItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderResponsesByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(this::buildOrderResponse);
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrdersByStatus(User user, OrderStatus status) {
        return orderRepository.findByUserAndStatus(user, status);
    }

    @Transactional(readOnly = true)
    public List<Order> getNewOrders() {
        return orderRepository.findNewOrders();
    }

    @Transactional(readOnly = true)
    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = getById(orderId);
        order.setStatus(newStatus);

        Order savedOrder = orderRepository.save(order);
        log.info("Статус заказа #{} изменен на: {}", orderId, newStatus);

        return savedOrder;
    }

    @Transactional
    public Order markAsPaid(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.PAID);
    }

    @Transactional
    public Order markAsShipped(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.SHIPPED);
    }

    @Transactional
    public Order markAsDelivered(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getById(orderId);

        if (order.getStatus() != OrderStatus.NEW && order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("Нельзя отменить заказ в статусе: " + order.getStatus());
        }

        List<ProductItem> items = productItemRepository.findOrderItemsWithProduct(orderId);
        for (ProductItem item : items) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        log.info("Заказ #{} отменен", orderId);

        return savedOrder;
    }

    @Transactional
    public int bulkUpdateStatus(List<Long> orderIds, OrderStatus status) {
        int count = orderRepository.bulkUpdateStatus(orderIds, status);
        log.info("Массово обновлен статус для {} заказов на: {}", count, status);
        return count;
    }

    @Transactional(readOnly = true)
    public List<ProductItem> getOrderItems(Long orderId) {
        return productItemRepository.findOrderItemsWithProduct(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponse> getOrderItemResponses(Long orderId) {
        return productItemRepository.findOrderItemsWithProduct(orderId).stream()
                .map(item -> OrderItemResponse.from(item, item.getProduct()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean hasProduct(Long orderId, Long productId) {
        return productItemRepository.existsByParentTypeAndParentIdAndProductId(
                ParentType.ORDER, orderId, productId);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersContainingProduct(Long productId) {
        return orderRepository.findOrdersContainingProduct(productId);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByDeliveryAddress(String address) {
        return orderRepository.searchByDeliveryAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByComment(String comment) {
        return orderRepository.searchByComment(comment);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Order> getTodayOrders() {
        return orderRepository.findTodayOrders();
    }

    @Transactional(readOnly = true)
    public List<Order> getCurrentMonthOrders() {
        return orderRepository.findCurrentMonthOrders();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersWithMinPrice(BigDecimal minPrice) {
        return orderRepository.findByTotalPriceGreaterThanEqual(minPrice);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return orderRepository.findByTotalPriceBetween(minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByDeliveryMethod(String deliveryMethod) {
        return orderRepository.findByDeliveryMethod(deliveryMethod);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByPaymentMethod(String paymentMethod) {
        return orderRepository.findByPaymentMethod(paymentMethod);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getOrderStatusStatistics() {
        return orderRepository.getOrderStatusStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getDailyStatistics(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getSalesReport(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal getAverageOrderValue() {
        return orderRepository.getAverageOrderValue();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        return orderRepository.sumTotalBetween(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now()
        );
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.sumTotalBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Order> getTopOrdersByTotal(int limit) {
        return orderRepository.findTopByOrderByTotalPriceDesc(PageRequest.of(0, limit));
    }


    @Transactional(readOnly = true)
    public List<Object[]> getTopUsersBySpending(int limit) {
        return orderRepository.getTopUsersBySpending(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<Object[]> getPopularDeliveryMethods() {
        return orderRepository.getPopularDeliveryMethods();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getPopularPaymentMethods() {
        return orderRepository.getPopularPaymentMethods();
    }

    @Transactional(readOnly = true)
    public long getTodayOrderCount() {
        return orderRepository.getTodayOrderCount();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTodayRevenue() {
        return orderRepository.getTodayRevenue();
    }

    @Transactional(readOnly = true)
    public Object getOrderStatistics() {
        long totalOrders = orderRepository.count();
        long todayOrders = getTodayOrderCount();
        BigDecimal todayRevenue = getTodayRevenue();
        BigDecimal totalRevenue = getTotalRevenue();
        BigDecimal avgOrderValue = getAverageOrderValue();

        return Map.of(
                "totalOrders", totalOrders,
                "todayOrders", todayOrders,
                "todayRevenue", todayRevenue,
                "totalRevenue", totalRevenue,
                "averageOrderValue", avgOrderValue,
                "statusStatistics", getOrderStatusStatistics(),
                "popularDeliveryMethods", getPopularDeliveryMethods(),
                "popularPaymentMethods", getPopularPaymentMethods()
        );
    }

    @Transactional(readOnly = true)
    public List<Order> getPaidNotShippedOrders(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        return orderRepository.findPaidNotShippedOrders(threshold);
    }

    @Transactional(readOnly = true)
    public List<Order> getShippedNotDeliveredOrders(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        return orderRepository.findShippedNotDeliveredOrders(threshold);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getUserOrderReport(Long userId) {
        return orderRepository.getUserOrderReport(userId);
    }


    @Transactional(readOnly = true)
    public BigDecimal getTotalSpentByUser(Long userId) {
        return orderRepository.sumTotalSpentByUser(userId);
    }

    @Transactional
    public int deleteOldCancelledOrders(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = orderRepository.deleteOldCancelledOrders(threshold);
        log.info("Удалено {} старых отмененных заказов", count);
        return count;
    }

    @Transactional
    public void deleteUserOrders(User user) {
        orderRepository.deleteByUser(user);
        log.info("Удалены все заказы пользователя: {}", user.getUsername());
    }

    private OrderResponse buildOrderResponse(Order order) {
        OrderResponse response = OrderResponse.from(order);

        List<OrderItemResponse> items = getOrderItemResponses(order.getId());
        response.setItems(items);

        return response;
    }

    public List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::buildOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return orderRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean userHasOrders(User user) {
        return orderRepository.existsByUser(user);
    }

    @Transactional(readOnly = true)
    public boolean userHasOrdersWithStatus(User user, OrderStatus status) {
        return orderRepository.existsByUserAndStatus(user, status);
    }
}