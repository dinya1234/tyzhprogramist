package ru.shop.tyzhprogramist.tyzhprogramist.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.ComponentTypeResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.service.ComponentTypeService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/component-types")
@RequiredArgsConstructor
public class ComponentTypeController {

    private final ComponentTypeService componentTypeService;

    @GetMapping
    public ResponseEntity<List<ComponentTypeResponse>> getAllTypes() {
        return ResponseEntity.ok(componentTypeService.getAllResponses());
    }

    @GetMapping("/ordered")
    public ResponseEntity<List<ComponentTypeResponse>> getOrderedTypes() {
        return ResponseEntity.ok(componentTypeService.getAllResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentTypeResponse> getTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.getResponseById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ComponentTypeResponse> getTypeByName(@PathVariable String name) {
        ComponentType type = componentTypeService.getByName(name);
        return ResponseEntity.ok(ComponentTypeResponse.from(type));
    }

    @GetMapping("/step/{step}")
    public ResponseEntity<ComponentTypeResponse> getTypeByStep(@PathVariable Integer step) {
        return componentTypeService.getByOrderStep(step)
                .map(ComponentTypeResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/first")
    public ResponseEntity<ComponentTypeResponse> getFirstStep() {
        return componentTypeService.getFirstStep()
                .map(ComponentTypeResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/last")
    public ResponseEntity<ComponentTypeResponse> getLastStep() {
        return componentTypeService.getLastStep()
                .map(ComponentTypeResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/next/{currentStep}")
    public ResponseEntity<Map<String, Object>> getNextStepInfo(@PathVariable Integer currentStep) {
        return ResponseEntity.ok(componentTypeService.getNextStepInfo(currentStep));
    }

    @GetMapping("/progress/{currentStep}")
    public ResponseEntity<Map<String, Object>> getProgress(@PathVariable(required = false) Integer currentStep) {
        return ResponseEntity.ok(componentTypeService.getProgress(currentStep));
    }

    @GetMapping("/total-steps")
    public ResponseEntity<Long> getTotalSteps() {
        return ResponseEntity.ok(componentTypeService.getTotalSteps());
    }

    @GetMapping("/order-range")
    public ResponseEntity<Map<String, Integer>> getOrderStepRange() {
        return ResponseEntity.ok(componentTypeService.getOrderStepRange());
    }

    @GetMapping("/metadata")
    public ResponseEntity<List<Map<String, Object>>> getAllTypesWithMetadata() {
        return ResponseEntity.ok(componentTypeService.getAllTypesWithMetadata());
    }

    @GetMapping("/{id}/compatible")
    public ResponseEntity<List<ComponentTypeResponse>> getCompatibleTypes(@PathVariable Long id) {
        List<ComponentType> types = componentTypeService.getCompatibleTypes(id);
        return ResponseEntity.ok(componentTypeService.toResponseList(types));
    }

    @GetMapping("/{id}/incompatible")
    public ResponseEntity<List<ComponentTypeResponse>> getIncompatibleTypes(@PathVariable Long id) {
        List<ComponentType> types = componentTypeService.getIncompatibleTypes(id);
        return ResponseEntity.ok(componentTypeService.toResponseList(types));
    }

    @GetMapping("/{id}/compatibility-rules")
    public ResponseEntity<List<EntityRelation>> getCompatibilityRules(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.getCompatibilityRules(id));
    }

    @GetMapping("/compatibility/check")
    public ResponseEntity<Boolean> checkTypesCompatibility(
            @RequestParam Long typeId1,
            @RequestParam Long typeId2) {
        boolean compatible = componentTypeService.areCompatible(typeId1, typeId2);
        return ResponseEntity.ok(compatible);
    }

    @GetMapping("/processors")
    public ResponseEntity<List<ComponentTypeResponse>> getProcessorTypes() {
        return ResponseEntity.ok(componentTypeService.toResponseList(componentTypeService.getProcessorTypes()));
    }

    @GetMapping("/memory")
    public ResponseEntity<List<ComponentTypeResponse>> getMemoryTypes() {
        return ResponseEntity.ok(componentTypeService.toResponseList(componentTypeService.getMemoryTypes()));
    }

    @GetMapping("/storage")
    public ResponseEntity<List<ComponentTypeResponse>> getStorageTypes() {
        return ResponseEntity.ok(componentTypeService.toResponseList(componentTypeService.getStorageTypes()));
    }

    @GetMapping("/{id}/is-processor")
    public ResponseEntity<Boolean> isProcessorType(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.isProcessorType(id));
    }

    @GetMapping("/{id}/is-memory")
    public ResponseEntity<Boolean> isMemoryType(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.isMemoryType(id));
    }

    @GetMapping("/{id}/is-storage")
    public ResponseEntity<Boolean> isStorageType(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.isStorageType(id));
    }

    @GetMapping("/{id}/multiple-allowed")
    public ResponseEntity<Boolean> isMultipleAllowed(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.isMultipleAllowed(id));
    }

    @GetMapping("/{id}/required")
    public ResponseEntity<Boolean> isRequired(@PathVariable Long id) {
        return ResponseEntity.ok(componentTypeService.isRequired(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ComponentTypeResponse>> searchByName(@RequestParam String q) {
        List<ComponentType> types = componentTypeService.searchByName(q);
        return ResponseEntity.ok(componentTypeService.toResponseList(types));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponentTypeResponse> createType(
            @RequestParam @NotBlank String name,
            @RequestParam(required = false) Integer orderStep) {
        ComponentType type = componentTypeService.createComponentType(name, orderStep);
        return ResponseEntity.status(HttpStatus.CREATED).body(ComponentTypeResponse.from(type));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponentTypeResponse> updateType(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer orderStep) {
        ComponentType type = componentTypeService.updateComponentType(id, name, orderStep);
        return ResponseEntity.ok(ComponentTypeResponse.from(type));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        componentTypeService.deleteComponentType(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reorder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> reorderSequential() {
        int count = componentTypeService.reorderSequential();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/compatibility/rule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityRelation> addCompatibilityRule(
            @RequestParam Long typeId1,
            @RequestParam Long typeId2,
            @RequestParam boolean compatible) {
        EntityRelation rule = componentTypeService.addCompatibilityRule(typeId1, typeId2, compatible);
        return ResponseEntity.status(HttpStatus.CREATED).body(rule);
    }

    @PostMapping("/compatibility/rule/symmetric")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityRelation>> addSymmetricCompatibilityRule(
            @RequestParam Long typeId1,
            @RequestParam Long typeId2,
            @RequestParam boolean compatible) {
        List<EntityRelation> rules = componentTypeService.addSymmetricCompatibilityRule(typeId1, typeId2, compatible);
        return ResponseEntity.status(HttpStatus.CREATED).body(rules);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getComponentTypeStatistics() {
        return ResponseEntity.ok(componentTypeService.getComponentTypeStatistics());
    }

    @GetMapping("/usage-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getComponentUsageStatistics() {
        return ResponseEntity.ok(componentTypeService.getComponentUsageStatistics());
    }

    @PostMapping("/initialize-defaults")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initializeDefaultTypes() {
        componentTypeService.initializeDefaultComponentTypes();
        return ResponseEntity.ok().build();
    }
}