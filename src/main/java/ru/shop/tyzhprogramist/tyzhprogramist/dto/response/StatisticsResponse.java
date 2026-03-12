package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatisticsResponse(
        Long totalUsers,
        Long totalOrders,
        Long totalProducts,
        BigDecimal totalRevenue,
        Long ordersToday,
        Long usersToday,
        Map<String, Long> ordersByStatus,
        Map<String, Long> usersByRole
) {}