package ru.shop.tyzhprogramist.tyzhprogramist.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Data
public class StatisticsResponse {
    private Long totalUsers;
    private Long totalOrders;
    private Long totalProducts;
    private BigDecimal totalRevenue;

    private Long ordersToday;
    private Long usersToday;
    private BigDecimal revenueToday;

    private Map<String, Long> ordersByStatus;
    private Map<String, Long> usersByRole;
    private Map<String, Long> productsByCategory;

    private List<ProductResponse> topSellingProducts;
    private List<ProductResponse> mostViewedProducts;
}