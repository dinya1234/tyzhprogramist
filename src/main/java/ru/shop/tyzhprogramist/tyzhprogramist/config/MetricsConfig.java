package ru.shop.tyzhprogramist.tyzhprogramist.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MetricsConfig {

    /**
     * Gauge для активных сессий чата
     */
    @Bean
    public AtomicInteger activeChatSessionsGauge(MeterRegistry registry) {
        AtomicInteger gauge = new AtomicInteger(0);
        Gauge.builder("chat.sessions.active", gauge, AtomicInteger::get)
                .description("Активные сессии чата в реальном времени")
                .register(registry);
        return gauge;
    }

    /**
     * Gauge для общего количества товаров на складе
     */
    @Bean
    public AtomicInteger totalProductsInStockGauge(MeterRegistry registry) {
        AtomicInteger gauge = new AtomicInteger(0);
        Gauge.builder("products.in.stock.total", gauge, AtomicInteger::get)
                .description("Общее количество товаров на складе (сумма всех quantity)")
                .register(registry);
        return gauge;
    }

    /**
     * Gauge для количества заказов, созданных сегодня
     */
    @Bean
    public AtomicInteger totalOrdersTodayGauge(MeterRegistry registry) {
        AtomicInteger gauge = new AtomicInteger(0);
        Gauge.builder("orders.today.count", gauge, AtomicInteger::get)
                .description("Количество заказов, созданных сегодня")
                .register(registry);
        return gauge;
    }

    /**
     * JVM Memory Metrics
     */
    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics(MeterRegistry registry) {
        JvmMemoryMetrics metrics = new JvmMemoryMetrics();
        metrics.bindTo(registry);
        return metrics;
    }

    /**
     * JVM GC Metrics
     */
    @Bean
    public JvmGcMetrics jvmGcMetrics(MeterRegistry registry) {
        JvmGcMetrics metrics = new JvmGcMetrics();
        metrics.bindTo(registry);
        return metrics;
    }

    /**
     * JVM Thread Metrics
     */
    @Bean
    public JvmThreadMetrics jvmThreadMetrics(MeterRegistry registry) {
        JvmThreadMetrics metrics = new JvmThreadMetrics();
        metrics.bindTo(registry);
        return metrics;
    }

    /**
     * System Processor Metrics
     */
    @Bean
    public ProcessorMetrics processorMetrics(MeterRegistry registry) {
        ProcessorMetrics metrics = new ProcessorMetrics();
        metrics.bindTo(registry);
        return metrics;
    }
}