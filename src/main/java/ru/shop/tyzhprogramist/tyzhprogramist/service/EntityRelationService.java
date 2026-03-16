package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.EntityRelationResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RelationType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.EntityRelationRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityRelationService {

    private final EntityRelationRepository entityRelationRepository;
    private final UserService userService;

    public static final String TYPE_PRODUCT = "Product";
    public static final String TYPE_COMPONENT_TYPE = "ComponentType";
    public static final String TYPE_PC_BUILD = "PcBuild";
    public static final String TYPE_CATEGORY = "Category";

    @Transactional(readOnly = true)
    public EntityRelation getById(Long id) {
        return entityRelationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Связь не найдена с id: " + id));
    }

    @Transactional(readOnly = true)
    public EntityRelationResponse getResponseById(Long id) {
        return EntityRelationResponse.from(getById(id));
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getByRelationType(RelationType relationType) {
        return entityRelationRepository.findByRelationType(relationType);
    }

    @Transactional(readOnly = true)
    public Page<EntityRelation> getByRelationType(RelationType relationType, Pageable pageable) {
        return entityRelationRepository.findByRelationType(relationType, pageable);
    }

    @Transactional
    public EntityRelation createCompatibilityRule(String fromType, Long fromId,
                                                  String toType, Long toId,
                                                  boolean isCompatible) {
        if (entityRelationRepository.existsCompatibilityRule(fromType, fromId, toType, toId)) {
            throw new BadRequestException("Правило совместимости уже существует");
        }

        EntityRelation relation = new EntityRelation(
                fromType, fromId, toType, toId, isCompatible
        );

        EntityRelation saved = entityRelationRepository.save(relation);
        log.info("Создано правило совместимости: {} {} -> {} {} = {}",
                fromType, fromId, toType, toId, isCompatible);

        return saved;
    }

    @Transactional
    public List<EntityRelation> createSymmetricCompatibilityRule(String type1, Long id1,
                                                                 String type2, Long id2,
                                                                 boolean isCompatible) {
        List<EntityRelation> relations = new ArrayList<>();

        if (!entityRelationRepository.existsCompatibilityRule(type1, id1, type2, id2)) {
            relations.add(createCompatibilityRule(type1, id1, type2, id2, isCompatible));
        }

        if (!entityRelationRepository.existsCompatibilityRule(type2, id2, type1, id1)) {
            relations.add(createCompatibilityRule(type2, id2, type1, id1, isCompatible));
        }

        log.info("Созданы симметричные правила совместимости для {}-{} и {}-{}",
                type1, id1, type2, id2);

        return relations;
    }

    @Transactional
    public EntityRelation updateCompatibilityRule(Long ruleId, boolean isCompatible) {
        EntityRelation relation = getById(ruleId);

        if (relation.getRelationType() != RelationType.COMPATIBILITY) {
            throw new BadRequestException("Это не правило совместимости");
        }

        relation.setIsCompatible(isCompatible);
        EntityRelation saved = entityRelationRepository.save(relation);
        log.info("Обновлено правило совместимости {}: isCompatible = {}", ruleId, isCompatible);

        return saved;
    }

    @Transactional
    public void deleteCompatibilityRule(String fromType, Long fromId, String toType, Long toId) {
        entityRelationRepository.deleteCompatibilityRule(fromType, fromId, toType, toId);
        log.info("Удалено правило совместимости: {} {} -> {} {}", fromType, fromId, toType, toId);
    }

    @Transactional
    public int deleteAllCompatibilityRules(String contentType, Long objectId) {
        int count = entityRelationRepository.deleteAllCompatibilityRules(contentType, objectId);
        log.info("Удалено {} правил совместимости для {}-{}", count, contentType, objectId);
        return count;
    }

    @Transactional(readOnly = true)
    public Optional<Boolean> checkCompatibility(String fromType, Long fromId,
                                                String toType, Long toId) {
        return entityRelationRepository.checkCompatibility(fromType, fromId, toType, toId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getCompatibleEntities(String contentType, Long objectId) {
        return entityRelationRepository.findCompatibleComponents(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getIncompatibleEntities(String contentType, Long objectId) {
        return entityRelationRepository.findIncompatibleComponents(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getCompatibilityRulesFrom(String contentType, Long objectId) {
        return entityRelationRepository.findCompatibilityRulesFrom(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getCompatibilityRulesTo(String contentType, Long objectId) {
        return entityRelationRepository.findCompatibilityRulesTo(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getAllCompatibilityRules(String contentType, Long objectId) {
        return entityRelationRepository.findAllCompatibilityRules(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public Optional<Boolean> areProductsCompatible(Long productId1, Long productId2) {
        return entityRelationRepository.areProductsCompatible(productId1, productId2);
    }

    @Transactional(readOnly = true)
    public Optional<Boolean> isProductCompatibleWithComponentType(Long productId, Long componentTypeId) {
        return entityRelationRepository.isProductCompatibleWithComponentType(productId, componentTypeId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getProductCompatibilityRules(Long productId) {
        return entityRelationRepository.findProductCompatibilityRules(productId);
    }

    @Transactional
    public EntityRelation createProductCompatibilityRule(Long productId1, Long productId2,
                                                         boolean isCompatible) {
        return createCompatibilityRule(TYPE_PRODUCT, productId1,
                TYPE_PRODUCT, productId2, isCompatible);
    }

    @Transactional
    public List<EntityRelation> createSymmetricProductCompatibilityRule(Long productId1,
                                                                        Long productId2,
                                                                        boolean isCompatible) {
        return createSymmetricCompatibilityRule(TYPE_PRODUCT, productId1,
                TYPE_PRODUCT, productId2, isCompatible);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getComponentTypeCompatibilityRules(Long componentTypeId) {
        return entityRelationRepository.findComponentTypeCompatibilityRules(componentTypeId);
    }

    @Transactional
    public EntityRelation createComponentTypeCompatibilityRule(Long typeId1, Long typeId2,
                                                               boolean isCompatible) {
        return createCompatibilityRule(TYPE_COMPONENT_TYPE, typeId1,
                TYPE_COMPONENT_TYPE, typeId2, isCompatible);
    }

    @Transactional
    public EntityRelation createComparison(User user, String name,
                                           String contentType, Long objectId) {
        if (entityRelationRepository.existsInComparison(user.getId(), name, contentType, objectId)) {
            throw new BadRequestException("Этот объект уже добавлен в сравнение '" + name + "'");
        }

        EntityRelation relation = new EntityRelation(
                user, name, contentType, objectId, contentType, objectId
        );

        EntityRelation saved = entityRelationRepository.save(relation);
        log.info("Пользователь {} добавил {}-{} в сравнение '{}'",
                user.getUsername(), contentType, objectId, name);

        return saved;
    }
    @Transactional
    public EntityRelation addToComparison(User user, String comparisonName,
                                          String contentType, Long objectId) {
        return createComparison(user, comparisonName, contentType, objectId);
    }

    @Transactional
    public void removeFromComparison(User user, String comparisonName,
                                     String contentType, Long objectId) {
        entityRelationRepository.removeFromComparison(user.getId(), comparisonName,
                contentType, objectId);
        log.info("Пользователь {} удалил {}-{} из сравнения '{}'",
                user.getUsername(), contentType, objectId, comparisonName);
    }

    @Transactional
    public void deleteComparison(User user, String comparisonName) {
        entityRelationRepository.deleteComparison(user.getId(), comparisonName);
        log.info("Пользователь {} удалил сравнение '{}'", user.getUsername(), comparisonName);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getUserComparisons(User user) {
        return entityRelationRepository.findByUserAndRelationType(user, RelationType.COMPARISON);
    }

    @Transactional(readOnly = true)
    public Page<EntityRelation> getUserComparisons(User user, Pageable pageable) {
        return entityRelationRepository.findByUserAndRelationType(user, RelationType.COMPARISON, pageable);
    }

    @Transactional(readOnly = true)
    public List<String> getUserComparisonNames(Long userId) {
        return entityRelationRepository.findUserComparisonNames(userId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getComparisonItems(Long userId, String comparisonName) {
        return entityRelationRepository.findComparisonItems(userId, comparisonName);
    }

    @Transactional(readOnly = true)
    public Map<String, List<EntityRelation>> getComparisonItemsGrouped(Long userId, String comparisonName) {
        List<EntityRelation> items = getComparisonItems(userId, comparisonName);

        return items.stream()
                .collect(Collectors.groupingBy(EntityRelation::getFromContentType));
    }

    @Transactional(readOnly = true)
    public int countComparisonItems(Long userId, String comparisonName) {
        return entityRelationRepository.countComparisonItems(userId, comparisonName);
    }

    @Transactional(readOnly = true)
    public boolean existsInComparison(Long userId, String comparisonName,
                                      String contentType, Long objectId) {
        return entityRelationRepository.existsInComparison(userId, comparisonName,
                contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getUserProductComparisons(Long userId) {
        return entityRelationRepository.findUserProductComparisons(userId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getUserPcBuildComparisons(Long userId) {
        return entityRelationRepository.findUserPcBuildComparisons(userId);
    }

    @Transactional
    public EntityRelation addProductToComparison(User user, String comparisonName, Long productId) {
        return addToComparison(user, comparisonName, TYPE_PRODUCT, productId);
    }

    @Transactional
    public EntityRelation addPcBuildToComparison(User user, String comparisonName, Long pcBuildId) {
        return addToComparison(user, comparisonName, TYPE_PC_BUILD, pcBuildId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> findByFromEntity(String contentType, Long objectId) {
        return entityRelationRepository.findByFromEntity(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> findByToEntity(String contentType, Long objectId) {
        return entityRelationRepository.findByToEntity(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> findAllByEntity(String contentType, Long objectId) {
        return entityRelationRepository.findAllByEntity(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return entityRelationRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getRecentUserRelations(Long userId, int limit) {
        return entityRelationRepository.findRecentUserRelations(userId, PageRequest.of(0, limit));
    }

    @Transactional
    public void deleteByUser(User user) {
        entityRelationRepository.deleteByUser(user);
        log.info("Удалены все связи пользователя: {}", user.getUsername());
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        int count = entityRelationRepository.deleteByUserId(userId);
        log.info("Удалено {} связей пользователя {}", count, userId);
        return count;
    }

    @Transactional
    public int deleteAllByEntity(String contentType, Long objectId) {
        int count = entityRelationRepository.deleteAllByEntity(contentType, objectId);
        log.info("Удалено {} связей для {}-{}", count, contentType, objectId);
        return count;
    }

    @Transactional
    public int deleteOldComparisons(int daysOld) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(daysOld);
        int count = entityRelationRepository.deleteOldComparisons(threshold);
        log.info("Удалено {} старых сравнений", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<Object[]> getRelationTypeStatistics() {
        return entityRelationRepository.getRelationTypeStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getFromEntityStatistics() {
        return entityRelationRepository.getFromEntityStatistics();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMostComparedProducts(int limit) {
        return entityRelationRepository.getMostComparedProducts(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Object[] getCompatibilityStatistics(String contentType, Long objectId) {
        return entityRelationRepository.getCompatibilityStatistics(contentType, objectId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getEntityRelationStatistics() {
        long totalCompatibilities = entityRelationRepository.findByRelationType(RelationType.COMPATIBILITY).size();
        long totalComparisons = entityRelationRepository.findByRelationType(RelationType.COMPARISON).size();

        return Map.of(
                "totalRelations", entityRelationRepository.count(),
                "totalCompatibilities", totalCompatibilities,
                "totalComparisons", totalComparisons,
                "byRelationType", getRelationTypeStatistics(),
                "byFromEntity", getFromEntityStatistics(),
                "mostComparedProducts", getMostComparedProducts(10)
        );
    }

    @Transactional(readOnly = true)
    public boolean existsCompatibilityRule(String fromType, Long fromId, String toType, Long toId) {
        return entityRelationRepository.existsCompatibilityRule(fromType, fromId, toType, toId);
    }

    @Transactional(readOnly = true)
    public boolean existsByRelationTypeAndEntities(RelationType relationType,
                                                   String fromType, Long fromId,
                                                   String toType, Long toId) {
        return entityRelationRepository
                .existsByRelationTypeAndFromContentTypeAndFromObjectIdAndToContentTypeAndToObjectId(
                        relationType, fromType, fromId, toType, toId);
    }

    @Transactional
    public List<EntityRelation> bulkCreateCompatibilityRules(String fromType, Long fromId,
                                                             List<String> toTypes, List<Long> toIds,
                                                             boolean isCompatible) {
        if (toTypes.size() != toIds.size()) {
            throw new BadRequestException("Количество типов и ID должно совпадать");
        }

        List<EntityRelation> created = new ArrayList<>();

        for (int i = 0; i < toTypes.size(); i++) {
            String toType = toTypes.get(i);
            Long toId = toIds.get(i);

            if (!existsCompatibilityRule(fromType, fromId, toType, toId)) {
                EntityRelation relation = createCompatibilityRule(
                        fromType, fromId, toType, toId, isCompatible
                );
                created.add(relation);
            }
        }

        log.info("Массово создано {} правил совместимости для {}-{}",
                created.size(), fromType, fromId);

        return created;
    }

    @Transactional
    public List<EntityRelation> copyCompatibilityRules(String sourceType, Long sourceId,
                                                       String targetType, Long targetId) {
        List<EntityRelation> sourceRules = getAllCompatibilityRules(sourceType, sourceId);
        List<EntityRelation> copied = new ArrayList<>();

        for (EntityRelation rule : sourceRules) {
            if (rule.getFromContentType().equals(sourceType) && rule.getFromObjectId().equals(sourceId)) {
                if (!existsCompatibilityRule(targetType, targetId, rule.getToContentType(), rule.getToObjectId())) {
                    EntityRelation newRule = createCompatibilityRule(
                            targetType, targetId,
                            rule.getToContentType(), rule.getToObjectId(),
                            rule.getIsCompatible()
                    );
                    copied.add(newRule);
                }
            } else if (rule.getToContentType().equals(sourceType) && rule.getToObjectId().equals(sourceId)) {
                if (!existsCompatibilityRule(rule.getFromContentType(), rule.getFromObjectId(), targetType, targetId)) {
                    EntityRelation newRule = createCompatibilityRule(
                            rule.getFromContentType(), rule.getFromObjectId(),
                            targetType, targetId,
                            rule.getIsCompatible()
                    );
                    copied.add(newRule);
                }
            }
        }

        log.info("Скопировано {} правил совместимости от {}-{} к {}-{}",
                copied.size(), sourceType, sourceId, targetType, targetId);

        return copied;
    }

    private void validateContentType(String contentType) {
        List<String> validTypes = Arrays.asList(TYPE_PRODUCT, TYPE_COMPONENT_TYPE,
                TYPE_PC_BUILD, TYPE_CATEGORY);
        if (!validTypes.contains(contentType)) {
            throw new BadRequestException("Недопустимый тип сущности: " + contentType);
        }
    }

    private void validateObjectId(Long objectId) {
        if (objectId == null || objectId <= 0) {
            throw new BadRequestException("Некорректный ID сущности");
        }
    }

    public List<EntityRelationResponse> toResponseList(List<EntityRelation> relations) {
        return relations.stream()
                .map(EntityRelationResponse::from)
                .collect(Collectors.toList());
    }

    public Page<EntityRelationResponse> toResponsePage(Page<EntityRelation> relations) {
        return relations.map(EntityRelationResponse::from);
    }
}