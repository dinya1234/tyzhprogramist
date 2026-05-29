package ru.shop.tyzhprogramist.tyzhprogramist.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ChatStatus;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ChatSessionRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.OrderRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.service.MetricsService;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class MetricsUpdateScheduler {

    private final MetricsService metricsService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ChatSessionRepository chatSessionRepository;

    /**
     * Обновление количества товаров на складе каждые 30 секунд
     */
    @Scheduled(fixedDelay = 30000)
    public void updateProductStockGauge() {
        try {
            // Тебе нужно добавить этот метод в ProductRepository
            Integer totalStock = productRepository.getTotalStockQuantity();
            if (totalStock == null) totalStock = 0;
            metricsService.updateTotalProductsInStock(totalStock);
            log.debug("Обновлена метрика products.in.stock.total: {}", totalStock);
        } catch (Exception e) {
            log.error("Ошибка при обновлении метрики остатков товаров: {}", e.getMessage());
        }
    }

    /**
     * Обновление количества заказов за сегодня каждую минуту
     * Используем твой существующий метод getTodayOrderCount()
     */
    @Scheduled(fixedDelay = 60000)
    public void updateTodayOrdersGauge() {
        try {
            long todayOrders = orderRepository.getTodayOrderCount();
            metricsService.updateTotalOrdersToday((int) todayOrders);
            log.debug("Обновлена метрика orders.today.count: {}", todayOrders);
        } catch (Exception e) {
            log.error("Ошибка при обновлении метрики заказов за сегодня: {}", e.getMessage());
        }
    }

    /**
     * Обновление количества активных сессий чата каждые 15 секунд
     * Используем твой существующий метод countByStatus(ChatStatus.ACTIVE)
     */
    @Scheduled(fixedDelay = 15000)
    public void updateActiveChatSessionsGauge() {
        try {
            long activeSessions = chatSessionRepository.countByStatus(ChatStatus.ACTIVE);
            metricsService.updateActiveChatSessions((int) activeSessions);
            log.debug("Обновлена метрика chat.sessions.active: {}", activeSessions);
        } catch (Exception e) {
            log.error("Ошибка при обновлении метрики активных сессий чата: {}", e.getMessage());
        }
    }
}