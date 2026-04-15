package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.EntityRelation;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.RelationType;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.EntityRelationRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EntityRelationService Тесты")
class EntityRelationServiceTest {

    @Mock
    private EntityRelationRepository entityRelationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private EntityRelationService entityRelationService;

    private User testUser;
    private EntityRelation testRelation;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testRelation = new EntityRelation();
        testRelation.setId(1L);
        testRelation.setRelationType(RelationType.COMPATIBILITY);
        testRelation.setIsCompatible(true);
    }

    @Test
    @DisplayName("ТЕСТ-1: getById - должен вернуть связь по ID")
    void getById_ShouldReturnRelation_WhenIdExists() {
        when(entityRelationRepository.findById(1L)).thenReturn(Optional.of(testRelation));

        EntityRelation result = entityRelationService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getIsCompatible()).isTrue();
    }

    @Test
    @DisplayName("ТЕСТ-2: checkCompatibility - должен вернуть true для совместимых компонентов")
    void checkCompatibility_ShouldReturnTrue_WhenComponentsAreCompatible() {
        when(entityRelationRepository.checkCompatibility(
                EntityRelationService.TYPE_PRODUCT, 1L,
                EntityRelationService.TYPE_PRODUCT, 2L))
                .thenReturn(Optional.of(true));

        Optional<Boolean> result = entityRelationService.checkCompatibility(
                EntityRelationService.TYPE_PRODUCT, 1L,
                EntityRelationService.TYPE_PRODUCT, 2L);

        assertThat(result).isPresent();
        assertThat(result.get()).isTrue();
    }

    @Test
    @DisplayName("ТЕСТ-3: createCompatibilityRule - должен выбросить исключение при существующем правиле")
    void createCompatibilityRule_ShouldThrowException_WhenRuleAlreadyExists() {
        when(entityRelationRepository.existsCompatibilityRule(
                EntityRelationService.TYPE_PRODUCT, 1L,
                EntityRelationService.TYPE_PRODUCT, 2L))
                .thenReturn(true);

        assertThatThrownBy(() -> entityRelationService.createCompatibilityRule(
                EntityRelationService.TYPE_PRODUCT, 1L,
                EntityRelationService.TYPE_PRODUCT, 2L, true))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже существует");
    }
}