package ru.shop.tyzhprogramist.tyzhprogramist.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class MetricsAspect {

    private final MeterRegistry meterRegistry;

    public MetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object measureControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String metricName = "http.server.requests";

        Timer.Sample sample = Timer.start(meterRegistry);
        boolean success = true;

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            sample.stop(Timer.builder(metricName)
                    .tag("method", methodName)
                    .tag("controller", className)
                    .tag("success", String.valueOf(success))
                    .register(meterRegistry));
        }
    }
}