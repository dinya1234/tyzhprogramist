package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreatePcBuildRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PcBuildComponentResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.PcBuildResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.*;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.PcBuildRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductItemRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PcBuildService {

    private final PcBuildRepository pcBuildRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ComponentTypeService componentTypeService;
    private final EntityRelationService entityRelationService;
    private final FileAttachmentService fileAttachmentService;

    @Transactional(readOnly = true)
    public PcBuild getById(Long id) {
        return pcBuildRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сборка ПК не найдена с id: " + id));
    }

    @Transactional(readOnly = true)
    public PcBuildResponse getPcBuildResponseById(Long id) {
        PcBuild build = getById(id);
        return buildPcBuildResponse(build);
    }

    @Transactional(readOnly = true)
    public Page<PcBuild> getPublicBuilds(Pageable pageable) {
        return pcBuildRepository.findByIsPublicTrueOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<PcBuildResponse> getPublicBuildResponses(Pageable pageable) {
        return pcBuildRepository.findByIsPublicTrueOrderByCreatedAtDesc(pageable)
                .map(this::buildPcBuildResponse);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getRecentPublicBuilds(int limit) {
        return pcBuildRepository.findRecentPublicBuilds(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Page<PcBuild> getMostViewedPublicBuilds(Pageable pageable) {
        return pcBuildRepository.findMostViewedPublicBuilds(pageable);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getUserBuilds(User user) {
        return pcBuildRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Page<PcBuild> getUserBuilds(User user, Pageable pageable) {
        return pcBuildRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PcBuild> getUserBuildsByUserId(Long userId, Pageable pageable) {
        return pcBuildRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PcBuildResponse> getUserBuildResponses(Long userId, Pageable pageable) {
        return pcBuildRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::buildPcBuildResponse);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getUserPrivateBuilds(User user) {
        return pcBuildRepository.findByUserAndIsPublicFalse(user);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getUserBuildsByName(Long userId, String name) {
        return pcBuildRepository.findByUserIdAndName(userId, name);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndName(Long userId, String name) {
        return pcBuildRepository.existsByUserIdAndName(userId, name);
    }

    @Transactional
    public PcBuild createBuild(User user, CreatePcBuildRequest request) {
        if (existsByUserIdAndName(user.getId(), request.getName())) {
            throw new BadRequestException("У вас уже есть сборка с таким именем");
        }

        PcBuild build = new PcBuild(
                user,
                request.getName(),
                request.getIsPublic() != null ? request.getIsPublic() : false
        );

        PcBuild savedBuild = pcBuildRepository.save(build);
        log.info("Создана новая сборка ПК: {} для пользователя {}",
                savedBuild.getName(), user.getUsername());

        return savedBuild;
    }

    @Transactional
    public PcBuild updateBuild(Long buildId, String name, Boolean isPublic) {
        PcBuild build = getById(buildId);

        if (name != null && !name.equals(build.getName())) {
            if (existsByUserIdAndName(build.getUser().getId(), name)) {
                throw new BadRequestException("У вас уже есть сборка с таким именем");
            }
            build.setName(name);
        }

        if (isPublic != null) {
            build.setIsPublic(isPublic);
        }

        PcBuild savedBuild = pcBuildRepository.save(build);
        log.info("Обновлена сборка ПК: {}", savedBuild.getName());

        return savedBuild;
    }

    @Transactional
    public void deleteBuild(Long buildId) {
        PcBuild build = getById(buildId);

        productItemRepository.clearPcBuild(buildId);

        pcBuildRepository.delete(build);
        log.info("Удалена сборка ПК: {}", build.getName());
    }

    @Transactional
    public PcBuild makePublic(Long buildId) {
        pcBuildRepository.makePublic(buildId);
        log.info("Сборка {} стала публичной", buildId);
        return getById(buildId);
    }

    @Transactional
    public PcBuild makePrivate(Long buildId) {
        pcBuildRepository.makePrivate(buildId);
        log.info("Сборка {} стала приватной", buildId);
        return getById(buildId);
    }

    @Transactional
    public int hideAllUserBuilds(Long userId) {
        int count = pcBuildRepository.hideAllUserBuilds(userId);
        log.info("Скрыто {} сборок пользователя {}", count, userId);
        return count;
    }

    @Transactional
    public ProductItem addComponent(Long buildId, Long productId, Integer quantity) {
        PcBuild build = getById(buildId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Товар не найден с id: " + productId));

        if (quantity == null || quantity <= 0) {
            quantity = 1;
        }

        if (product.getQuantity() < quantity) {
            throw new BadRequestException("Недостаточное количество товара на складе");
        }

        List<ProductItem> existingComponents = getBuildComponents(buildId);
        checkCompatibility(product, existingComponents);

        Optional<ProductItem> existingItem = productItemRepository.findPcBuildComponent(buildId, productId);

        if (existingItem.isPresent()) {
            ProductItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            item.setQuantity(newQuantity);
            return productItemRepository.save(item);
        } else {
            ProductItem newItem = new ProductItem(
                    buildId,
                    product,
                    quantity,
                    product.getPrice()
            );
            newItem.setParentType(ParentType.PC_BUILD);
            return productItemRepository.save(newItem);
        }
    }

    private void checkCompatibility(Product newProduct, List<ProductItem> existingComponents) {
        for (ProductItem existing : existingComponents) {
            Product existingProduct = existing.getProduct();

            Optional<Boolean> compatibility = entityRelationService.areProductsCompatible(
                    existingProduct.getId(), newProduct.getId());

            if (compatibility.isPresent() && !compatibility.get()) {
                throw new BadRequestException(String.format(
                        "Компонент '%s' несовместим с '%s'",
                        newProduct.getName(), existingProduct.getName()
                ));
            }

            if (isUniqueComponentType(newProduct) && isSameComponentType(newProduct, existingProduct)) {
                throw new BadRequestException(String.format(
                        "В сборке уже есть %s. Нельзя добавить второй.",
                        getComponentTypeName(newProduct)
                ));
            }
        }
    }

    private boolean isUniqueComponentType(Product product) {
        String categoryName = product.getCategory().getName().toLowerCase();
        return categoryName.contains("процессор") ||
                categoryName.contains("материнская") ||
                categoryName.contains("блок питания") ||
                categoryName.contains("видеокарта");
    }


    private boolean isSameComponentType(Product p1, Product p2) {
        return p1.getCategory().getId().equals(p2.getCategory().getId());
    }

    private String getComponentTypeName(Product product) {
        return product.getCategory().getName();
    }

    @Transactional
    public ProductItem updateComponentQuantity(Long buildId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            removeComponent(buildId, productId);
            return null;
        }

        ProductItem item = productItemRepository.findPcBuildComponent(buildId, productId)
                .orElseThrow(() -> new NotFoundException("Компонент не найден в сборке"));

        Product product = item.getProduct();
        if (product.getQuantity() < quantity) {
            throw new BadRequestException("Недостаточное количество товара на складе");
        }

        item.setQuantity(quantity);
        return productItemRepository.save(item);
    }

    @Transactional
    public void removeComponent(Long buildId, Long productId) {
        productItemRepository.deleteByParentTypeAndParentIdAndProductId(
                ParentType.PC_BUILD, buildId, productId);
        log.info("Компонент {} удален из сборки {}", productId, buildId);
    }

    @Transactional
    public void clearBuild(Long buildId) {
        productItemRepository.clearPcBuild(buildId);
        log.info("Сборка {} очищена", buildId);
    }

    @Transactional(readOnly = true)
    public List<ProductItem> getBuildComponents(Long buildId) {
        return productItemRepository.findPcBuildItemsWithProduct(buildId);
    }

    @Transactional(readOnly = true)
    public List<PcBuildComponentResponse> getBuildComponentResponses(Long buildId) {
        List<ProductItem> components = getBuildComponents(buildId);

        return components.stream()
                .map(item -> {
                    PcBuildComponentResponse response = new PcBuildComponentResponse();
                    response.setComponentType(item.getProduct().getCategory().getName());
                    response.setProductId(item.getProduct().getId());
                    response.setProductName(item.getProduct().getName());
                    response.setPrice(item.getPrice());
                    response.setQuantity(item.getQuantity());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, List<ProductItem>> getBuildComponentsByType(Long buildId) {
        List<ProductItem> components = getBuildComponents(buildId);

        return components.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getCategory().getName()
                ));
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPrice(Long buildId) {
        return productItemRepository.sumTotalPriceByParent(ParentType.PC_BUILD, buildId)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public Object[] getBuildStatistics(Long buildId) {
        return productItemRepository.getPcBuildStatistics(buildId);
    }

    @Transactional
    public void incrementViews(Long buildId) {
        pcBuildRepository.incrementViews(buildId);
    }

    @Transactional(readOnly = true)
    public Integer getViewsCount(Long buildId) {
        return pcBuildRepository.getViewsCount(buildId);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getTopByViews(int limit) {
        return pcBuildRepository.findTopByViews(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Page<PcBuild> searchPublicBuilds(String searchTerm, Pageable pageable) {
        return pcBuildRepository.searchPublicBuildsByName(searchTerm, pageable);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> searchUserBuilds(Long userId, String searchTerm) {
        return pcBuildRepository.searchUserBuildsByName(userId, searchTerm);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getBuildsContainingComponent(Long productId) {
        return pcBuildRepository.findBuildsContainingComponent(productId);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getPublicBuildsContainingComponent(Long productId) {
        return pcBuildRepository.findPublicBuildsContainingComponent(productId);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getBuildsWithCPU(String cpuName) {
        return pcBuildRepository.findBuildsWithCPU(cpuName);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getBuildsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return pcBuildRepository.findBuildsByPriceRange(minPrice, maxPrice, pageable);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getBuildsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return pcBuildRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Transactional
    public PcBuild cloneBuild(Long sourceBuildId, User newOwner) {
        PcBuild sourceBuild = getById(sourceBuildId);

        String newName = sourceBuild.getName() + " (копия)";
        int counter = 1;
        while (existsByUserIdAndName(newOwner.getId(), newName)) {
            newName = sourceBuild.getName() + " (копия " + counter + ")";
            counter++;
        }

        CreatePcBuildRequest request = new CreatePcBuildRequest();
        request.setName(newName);
        request.setIsPublic(false);

        PcBuild newBuild = createBuild(newOwner, request);

        List<ProductItem> sourceComponents = getBuildComponents(sourceBuildId);
        for (ProductItem component : sourceComponents) {
            addComponent(newBuild.getId(), component.getProduct().getId(), component.getQuantity());
        }

        log.info("Сборка {} скопирована пользователем {} как {}",
                sourceBuildId, newOwner.getUsername(), newName);

        return newBuild;
    }

    @Transactional(readOnly = true)
    public List<Object[]> getSimilarBuilds(Long buildId, int limit) {
        return pcBuildRepository.findSimilarBuilds(buildId, PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Object[] getUserBuildStatistics(Long userId) {
        return pcBuildRepository.getUserBuildStatistics(userId);
    }

    @Transactional(readOnly = true)
    public Object[] getOverallStatistics() {
        return pcBuildRepository.getOverallStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return pcBuildRepository.getDailyStatistics(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getTopUsersByBuilds(int limit) {
        return pcBuildRepository.getTopUsersByBuilds(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public long countUserBuilds(User user) {
        return pcBuildRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public long countPublicBuilds() {
        return pcBuildRepository.countByIsPublicTrue();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPcBuildStatistics() {
        Object[] overall = getOverallStatistics();
        long totalBuilds = pcBuildRepository.count();
        long publicBuilds = countPublicBuilds();

        return Map.of(
                "totalBuilds", totalBuilds,
                "publicBuilds", publicBuilds,
                "privateBuilds", totalBuilds - publicBuilds,
                "totalViews", overall != null ? overall[2] : 0,
                "avgViews", overall != null ? overall[3] : 0,
                "topUsers", getTopUsersByBuilds(10),
                "dailyStats", getDailyStatistics(
                        LocalDateTime.now().minusDays(30),
                        LocalDateTime.now()
                )
        );
    }

    @Transactional(readOnly = true)
    public boolean isBuildPublic(Long buildId) {
        return pcBuildRepository.isBuildPublic(buildId);
    }

    @Transactional(readOnly = true)
    public boolean isBuildOwner(Long buildId, Long userId) {
        PcBuild build = getById(buildId);
        return build.getUser().getId().equals(userId);
    }

    @Transactional(readOnly = true)
    public boolean canViewBuild(Long buildId, Long userId) {
        PcBuild build = getById(buildId);
        return build.getIsPublic() || (userId != null && build.getUser().getId().equals(userId));
    }

    @Transactional(readOnly = true)
    public boolean canEditBuild(Long buildId, Long userId) {
        if (userId == null) return false;
        PcBuild build = getById(buildId);
        return build.getUser().getId().equals(userId);
    }

    @Transactional
    public int deleteOldPrivateBuilds(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = pcBuildRepository.deleteOldPrivateBuilds(threshold);
        log.info("Удалено {} старых приватных сборок", count);
        return count;
    }

    @Transactional
    public int deleteUnpopularBuilds(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = pcBuildRepository.deleteUnpopularBuilds(threshold);
        log.info("Удалено {} непопулярных сборок", count);
        return count;
    }

    @Transactional
    public void deleteUserBuilds(User user) {
        pcBuildRepository.deleteByUser(user);
        log.info("Удалены все сборки пользователя: {}", user.getUsername());
    }

    private PcBuildResponse buildPcBuildResponse(PcBuild build) {
        PcBuildResponse response = PcBuildResponse.from(build);

        List<PcBuildComponentResponse> components = getBuildComponentResponses(build.getId());
        response.setComponents(components);

        response.setTotalPrice(calculateTotalPrice(build.getId()));

        return response;
    }

    public List<PcBuildResponse> toResponseList(List<PcBuild> builds) {
        return builds.stream()
                .map(this::buildPcBuildResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return pcBuildRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<PcBuild> getRecentlyUpdated(int limit) {
        return pcBuildRepository.findRecentlyUpdated(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public String exportBuildAsText(Long buildId) {
        PcBuild build = getById(buildId);
        List<ProductItem> components = getBuildComponents(buildId);
        BigDecimal total = calculateTotalPrice(buildId);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Сборка ПК: ").append(build.getName()).append(" ===\n");
        sb.append("Автор: ").append(build.getUser().getUsername()).append("\n");
        sb.append("Дата создания: ").append(build.getCreatedAt()).append("\n");
        sb.append("Просмотров: ").append(build.getViewsCount()).append("\n\n");
        sb.append("Компоненты:\n");

        Map<String, List<ProductItem>> byType = components.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getCategory().getName()
                ));

        for (Map.Entry<String, List<ProductItem>> entry : byType.entrySet()) {
            sb.append("\n--- ").append(entry.getKey()).append(" ---\n");
            for (ProductItem item : entry.getValue()) {
                sb.append(String.format("  %s x%d - %.2f руб.\n",
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ));
            }
        }

        sb.append("\n").append("=".repeat(40)).append("\n");
        sb.append("ИТОГО: ").append(total).append(" руб.\n");

        return sb.toString();
    }
}