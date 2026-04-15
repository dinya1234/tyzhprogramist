package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.CategoryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Тесты")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("Тестовая категория", "test-category");
        testCategory.setId(1L);
    }

    @Test
    @DisplayName("ТЕСТ-1: getById - должен вернуть категорию при существующем ID")
    void getById_ShouldReturnCategory_WhenIdExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        Category result = categoryService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Тестовая категория");
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ТЕСТ-2: getById - должен выбросить NotFoundException при несуществующем ID")
    void getById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Категория не найдена");
    }

    @Test
    @DisplayName("ТЕСТ-3: createCategory - должен выбросить исключение при дубликате slug")
    void createCategory_ShouldThrowException_WhenSlugAlreadyExists() {
        when(categoryRepository.existsBySlug("test-category")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(
                "Новая категория", "test-category", null, null, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("slug уже существует");

        verify(categoryRepository, never()).save(any(Category.class));
    }

}