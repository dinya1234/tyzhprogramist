package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.ComponentType;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ComponentTypeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComponentTypeService Тесты")
class ComponentTypeServiceTest {

    @Mock
    private ComponentTypeRepository componentTypeRepository;

    @Mock
    private EntityRelationService entityRelationService;

    @InjectMocks
    private ComponentTypeService componentTypeService;

    private ComponentType testComponentType;

    @BeforeEach
    void setUp() {
        testComponentType = new ComponentType(1, "Процессор");
        testComponentType.setId(1L);
    }

    @Test
    @DisplayName("ТЕСТ-1: getById - должен вернуть тип компонента по ID")
    void getById_ShouldReturnComponentType_WhenIdExists() {
        when(componentTypeRepository.findById(1L)).thenReturn(Optional.of(testComponentType));

        ComponentType result = componentTypeService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Процессор");
        assertThat(result.getOrder_step()).isEqualTo(1);
    }

    @Test
    @DisplayName("ТЕСТ-2: getByName - должен выбросить NotFoundException при отсутствии типа")
    void getByName_ShouldThrowNotFoundException_WhenNameDoesNotExist() {
        when(componentTypeRepository.findByName("Несуществующий тип")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> componentTypeService.getByName("Несуществующий тип"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Тип компонента не найден");
    }

    @Test
    @DisplayName("ТЕСТ-3: createComponentType - должен выбросить исключение при дубликате имени")
    void createComponentType_ShouldThrowException_WhenNameAlreadyExists() {
        when(componentTypeRepository.existsByName("Процессор")).thenReturn(true);

        assertThatThrownBy(() -> componentTypeService.createComponentType("Процессор", null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже существует");

        verify(componentTypeRepository, never()).save(any(ComponentType.class));
    }
}