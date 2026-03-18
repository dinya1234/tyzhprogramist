package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ComponentTypeResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ComponentTypeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComponentTypeService {

    private final ComponentTypeRepository componentTypeRepository;
    private final EntityRelationService entityRelationService;

    public static final String TYPE_PROCESSOR = "Процессор";
    public static final String TYPE_MOTHERBOARD = "Материнская плата";
    public static final String TYPE_RAM = "Оперативная память";
    public static final String TYPE_GPU = "Видеокарта";
    public static final String TYPE_STORAGE = "Накопитель";
    public static final String TYPE_PSU = "Блок питания";
    public static final String TYPE_COOLER = "Охлаждение";
    public static final String TYPE_CASE = "Корпус";

    @Transactional(readOnly = true)
    public ComponentType getById(Long id) {
        return componentTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Тип компонента не найден с id: " + id));
    }

    @Transactional(readOnly = true)
    public ComponentTypeResponse getResponseById(Long id) {
        return ComponentTypeResponse.from(getById(id));
    }

    @Transactional(readOnly = true)
    public ComponentType getByName(String name) {
        return componentTypeRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Тип компонента не найден: " + name));
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getAllOrdered() {
        return componentTypeRepository.findAllOrdered();
    }

    @Transactional(readOnly = true)
    public List<ComponentTypeResponse> getAllResponses() {
        return getAllOrdered().stream()
                .map(ComponentTypeResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return componentTypeRepository.existsByName(name);
    }

    @Transactional
    public ComponentType createComponentType(String name, Integer orderStep) {
        if (existsByName(name)) {
            throw new BadRequestException("Тип компонента с таким именем уже существует: " + name);
        }

        if (orderStep == null) {
            orderStep = componentTypeRepository.getMaxOrderStep() + 1;
        }

        if (orderStep <= componentTypeRepository.getMaxOrderStep()) {
            componentTypeRepository.shiftOrderSteps(orderStep, 1);
        }

        ComponentType componentType = new ComponentType(orderStep, name);
        ComponentType saved = componentTypeRepository.save(componentType);

        log.info("Создан новый тип компонента: {} с шагом {}", name, orderStep);

        return saved;
    }

    @Transactional
    public ComponentType updateComponentType(Long id, String name, Integer orderStep) {
        ComponentType componentType = getById(id);

        if (name != null && !name.equals(componentType.getName())) {
            if (existsByName(name)) {
                throw new BadRequestException("Тип компонента с таким именем уже существует: " + name);
            }
            componentType.setName(name);
        }

        if (orderStep != null && !orderStep.equals(componentType.getOrder_step())) {
            updateOrderStep(componentType, orderStep);
        }

        ComponentType saved = componentTypeRepository.save(componentType);
        log.info("Обновлен тип компонента: {}", saved.getName());

        return saved;
    }

    private void updateOrderStep(ComponentType componentType, Integer newOrderStep) {
        Integer oldOrderStep = componentType.getOrder_step();
        Integer maxOrderStep = componentTypeRepository.getMaxOrderStep();

        if (newOrderStep < 0) {
            throw new BadRequestException("Шаг порядка не может быть отрицательным");
        }

        if (newOrderStep > maxOrderStep) {
            newOrderStep = maxOrderStep;
        }

        if (newOrderStep < oldOrderStep) {
            componentTypeRepository.shiftOrderSteps(newOrderStep, 1);
            componentType.setOrder_step(newOrderStep);
        } else if (newOrderStep > oldOrderStep) {
            componentTypeRepository.shiftOrderSteps(oldOrderStep + 1, -1);
            componentType.setOrder_step(newOrderStep);
        }
    }

    @Transactional
    public void deleteComponentType(Long id) {
        ComponentType componentType = getById(id);

        entityRelationService.deleteAllCompatibilityRules(
                EntityRelationService.TYPE_COMPONENT_TYPE, id);

        componentTypeRepository.delete(componentType);

        componentTypeRepository.reorderSequential();

        log.info("Удален тип компонента: {}", componentType.getName());
    }

    @Transactional(readOnly = true)
    public Optional<ComponentType> getByOrderStep(Integer orderStep) {
        return componentTypeRepository.findByOrderStep(orderStep);
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getByOrderStepLessThanEqual(Integer orderStep) {
        return componentTypeRepository.findByOrderStepLessThanEqual(orderStep);
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getByOrderStepGreaterThan(Integer orderStep) {
        return componentTypeRepository.findByOrderStepGreaterThan(orderStep);
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getNextTypes(Integer currentOrder) {
        return componentTypeRepository.findNextTypes(currentOrder);
    }

    @Transactional(readOnly = true)
    public Optional<ComponentType> getNextType(Integer currentOrder) {
        List<ComponentType> nextTypes = componentTypeRepository.findNextStep(currentOrder, PageRequest.of(0, 1));
        return nextTypes.isEmpty() ? Optional.empty() : Optional.of(nextTypes.get(0));
    }

    @Transactional(readOnly = true)
    public Optional<ComponentType> getPreviousType(Integer currentOrder) {
        List<ComponentType> prevTypes = componentTypeRepository.findPreviousStep(currentOrder, PageRequest.of(0, 1));
        return prevTypes.isEmpty() ? Optional.empty() : Optional.of(prevTypes.get(0));
    }

    @Transactional(readOnly = true)
    public Optional<ComponentType> getFirstStep() {
        return componentTypeRepository.findFirstStep();
    }

    @Transactional(readOnly = true)
    public Optional<ComponentType> getLastStep() {
        return componentTypeRepository.findLastStep();
    }

    @Transactional(readOnly = true)
    public Integer getMaxOrderStep() {
        return componentTypeRepository.getMaxOrderStep();
    }

    @Transactional(readOnly = true)
    public Integer getMinOrderStep() {
        return componentTypeRepository.getMinOrderStep();
    }

    @Transactional(readOnly = true)
    public long getTotalSteps() {
        return componentTypeRepository.getTotalSteps();
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> getOrderStepRange() {
        Object[] range = componentTypeRepository.getOrderStepRange();
        return Map.of(
                "min", (Integer) range[0],
                "max", (Integer) range[1]
        );
    }

    @Transactional
    public int reorderSequential() {
        int count = componentTypeRepository.reorderSequential();
        log.info("Выполнена перенумерация шагов для {} типов компонентов", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getCompatibleTypes(Long componentTypeId) {
        return componentTypeRepository.findCompatibleTypes(componentTypeId);
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getIncompatibleTypes(Long componentTypeId) {
        return componentTypeRepository.findIncompatibleTypes(componentTypeId);
    }

    @Transactional(readOnly = true)
    public List<EntityRelation> getCompatibilityRules(Long componentTypeId) {
        return entityRelationService.getComponentTypeCompatibilityRules(componentTypeId);
    }

    @Transactional
    public EntityRelation addCompatibilityRule(Long typeId1, Long typeId2, boolean isCompatible) {
        return entityRelationService.createComponentTypeCompatibilityRule(
                typeId1, typeId2, isCompatible);
    }

    @Transactional
    public List<EntityRelation> addSymmetricCompatibilityRule(Long typeId1, Long typeId2,
                                                              boolean isCompatible) {
        return entityRelationService.createSymmetricCompatibilityRule(
                EntityRelationService.TYPE_COMPONENT_TYPE, typeId1,
                EntityRelationService.TYPE_COMPONENT_TYPE, typeId2,
                isCompatible);
    }

    @Transactional(readOnly = true)
    public boolean areCompatible(Long typeId1, Long typeId2) {
        return entityRelationService.checkCompatibility(
                EntityRelationService.TYPE_COMPONENT_TYPE, typeId1,
                EntityRelationService.TYPE_COMPONENT_TYPE, typeId2
        ).orElse(true);
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getProcessorTypes() {
        return componentTypeRepository.findProcessorTypes();
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getMemoryTypes() {
        return componentTypeRepository.findMemoryTypes();
    }

    @Transactional(readOnly = true)
    public List<ComponentType> getStorageTypes() {
        return componentTypeRepository.findStorageTypes();
    }

    @Transactional(readOnly = true)
    public boolean isProcessorType(Long id) {
        ComponentType type = getById(id);
        String name = type.getName().toLowerCase();
        return name.contains("процессор") || name.contains("cpu");
    }

    @Transactional(readOnly = true)
    public boolean isMemoryType(Long id) {
        ComponentType type = getById(id);
        String name = type.getName().toLowerCase();
        return name.contains("озу") || name.contains("ram") || name.contains("память");
    }

    @Transactional(readOnly = true)
    public boolean isStorageType(Long id) {
        ComponentType type = getById(id);
        String name = type.getName().toLowerCase();
        return name.contains("ssd") || name.contains("hdd") || name.contains("накопитель");
    }

    @Transactional(readOnly = true)
    public boolean isMultipleAllowed(Long id) {
        return componentTypeRepository.isMultipleAllowed(id);
    }
    @Transactional(readOnly = true)
    public boolean isRequired(Long id) {
        return componentTypeRepository.isRequired(id);
    }

    @Transactional(readOnly = true)
    public List<ComponentType> searchByName(String name) {
        return getAllOrdered().stream()
                .filter(type -> type.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Object[]> getComponentUsageStatistics() {
        return componentTypeRepository.getComponentUsageStatistics();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getComponentTypeStatistics() {
        long totalTypes = componentTypeRepository.count();
        Map<String, Integer> range = getOrderStepRange();

        return Map.of(
                "totalTypes", totalTypes,
                "orderStepRange", range,
                "processorTypes", getProcessorTypes().size(),
                "memoryTypes", getMemoryTypes().size(),
                "storageTypes", getStorageTypes().size(),
                "usageStatistics", getComponentUsageStatistics()
        );
    }

    public void validateComponentType(ComponentType componentType) {
        if (componentType.getName() == null || componentType.getName().trim().isEmpty()) {
            throw new BadRequestException("Название типа компонента не может быть пустым");
        }

        if (componentType.getOrder_step() == null || componentType.getOrder_step() < 0) {
            throw new BadRequestException("Шаг порядка должен быть положительным числом");
        }
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return componentTypeRepository.existsById(id);
    }

    public List<ComponentTypeResponse> toResponseList(List<ComponentType> types) {
        return types.stream()
                .map(ComponentTypeResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void initializeDefaultComponentTypes() {
        if (componentTypeRepository.count() == 0) {
            log.info("Инициализация базовых типов компонентов...");

            createComponentType(TYPE_PROCESSOR, 1);
            createComponentType(TYPE_MOTHERBOARD, 2);
            createComponentType(TYPE_RAM, 3);
            createComponentType(TYPE_GPU, 4);
            createComponentType(TYPE_STORAGE, 5);
            createComponentType(TYPE_PSU, 6);
            createComponentType(TYPE_COOLER, 7);
            createComponentType(TYPE_CASE, 8);

            log.info("Базовые типы компонентов инициализированы");
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getNextStepInfo(Integer currentStep) {
        Optional<ComponentType> nextType = getNextType(currentStep);
        Optional<ComponentType> prevType = getPreviousType(currentStep);

        Map<String, Object> info = new HashMap<>();
        info.put("currentStep", currentStep);
        info.put("hasNext", nextType.isPresent());
        info.put("hasPrev", prevType.isPresent());
        info.put("totalSteps", getTotalSteps());

        nextType.ifPresent(type -> {
            info.put("nextStep", type.getOrder_step());
            info.put("nextType", ComponentTypeResponse.from(type));
        });

        prevType.ifPresent(type -> {
            info.put("prevStep", type.getOrder_step());
            info.put("prevType", ComponentTypeResponse.from(type));
        });

        return info;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getProgress(Integer currentStep) {
        long totalSteps = getTotalSteps();
        int current = currentStep != null ? currentStep : 0;

        return Map.of(
                "currentStep", current,
                "totalSteps", totalSteps,
                "progressPercent", (int) ((current * 100.0) / totalSteps),
                "stepsRemaining", totalSteps - current
        );
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllTypesWithMetadata() {
        return getAllOrdered().stream()
                .map(type -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", type.getId());
                    map.put("name", type.getName());
                    map.put("orderStep", type.getOrder_step());
                    map.put("multipleAllowed", isMultipleAllowed(type.getId()));
                    map.put("required", isRequired(type.getId()));
                    return map;
                })
                .collect(Collectors.toList());
    }
}