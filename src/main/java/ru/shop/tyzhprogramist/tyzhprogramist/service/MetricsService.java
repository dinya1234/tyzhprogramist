package ru.shop.tyzhprogramist.tyzhprogramist.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeChatSessionsGauge;
    private final AtomicInteger totalProductsInStockGauge;
    private final AtomicInteger totalOrdersTodayGauge;

    //COUNTERS
    private Counter ordersCreatedCounter;
    private Counter usersRegisteredCounter;
    private Counter cartAdditionsCounter;
    private Counter productViewsCounter;
    private Counter feedbackSubmittedCounter;
    private Counter repairRequestsCounter;
    private Counter apiErrorsCounter;

    //TIMERS
    private Timer orderCreationTimer;
    private Timer apiResponseTimer;

    @PostConstruct
    public void initCounters() {
        ordersCreatedCounter = Counter.builder("orders.created.total")
                .description("Общее количество созданных заказов")
                .register(meterRegistry);

        usersRegisteredCounter = Counter.builder("users.registered.total")
                .description("Общее количество зарегистрированных пользователей")
                .register(meterRegistry);

        cartAdditionsCounter = Counter.builder("cart.additions.total")
                .description("Общее количество добавлений товаров в корзину")
                .register(meterRegistry);

        productViewsCounter = Counter.builder("product.views.total")
                .description("Общее количество просмотров товаров")
                .register(meterRegistry);

        feedbackSubmittedCounter = Counter.builder("feedback.submitted.total")
                .description("Общее количество оставленных отзывов и вопросов")
                .register(meterRegistry);

        repairRequestsCounter = Counter.builder("repair.requests.total")
                .description("Общее количество заявок на ремонт")
                .register(meterRegistry);

        apiErrorsCounter = Counter.builder("api.errors.total")
                .description("Общее количество ошибок API (4xx, 5xx)")
                .register(meterRegistry);

        orderCreationTimer = Timer.builder("order.creation.duration")
                .description("Время создания заказа в миллисекундах")
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                .register(meterRegistry);

        apiResponseTimer = Timer.builder("api.response.duration")
                .description("Время ответа API в миллисекундах")
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                .register(meterRegistry);

        log.info("MetricsService инициализирован, все счетчики зарегистрированы");
    }

    //INCREMENT METHODS
    public void incrementOrdersCreated() {
        ordersCreatedCounter.increment();
        log.debug("Метрика orders.created.total увеличена");
    }

    public void incrementUsersRegistered() {
        usersRegisteredCounter.increment();
        log.debug("Метрика users.registered.total увеличена");
    }

    public void incrementCartAdditions() {
        cartAdditionsCounter.increment();
    }

    public void incrementProductViews() {
        productViewsCounter.increment();
    }

    public void incrementFeedbackSubmitted() {
        feedbackSubmittedCounter.increment();
    }

    public void incrementRepairRequests() {
        repairRequestsCounter.increment();
    }

    public void incrementApiErrors() {
        apiErrorsCounter.increment();
    }

    //GAUGE UPDATE METHODS
    public void updateActiveChatSessions(int count) {
        activeChatSessionsGauge.set(count);
        log.debug("Активных сессий чата: {}", count);
    }

    public void updateTotalProductsInStock(int total) {
        totalProductsInStockGauge.set(total);
        log.debug("Товаров на складе: {}", total);
    }

    public void updateTotalOrdersToday(int count) {
        totalOrdersTodayGauge.set(count);
        log.debug("Заказов сегодня: {}", count);
    }

    //TIMER METHODS
    public Timer.Sample startOrderCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOrderCreationTimer(Timer.Sample sample) {
        if (sample != null) {
            sample.stop(orderCreationTimer);
        }
    }

    public Timer.Sample startApiTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopApiTimer(Timer.Sample sample) {
        if (sample != null) {
            sample.stop(apiResponseTimer);
        }
    }
}