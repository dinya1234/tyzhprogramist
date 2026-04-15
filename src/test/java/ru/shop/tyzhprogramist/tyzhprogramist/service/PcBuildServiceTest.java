package ru.shop.tyzhprogramist.tyzhprogramist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.request.CreatePcBuildRequest;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.PcBuild;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.User;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.PcBuildRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductItemRepository;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.ProductRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PcBuildService Тесты")
class PcBuildServiceTest {

    @Mock
    private PcBuildRepository pcBuildRepository;

    @Mock
    private ProductItemRepository productItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private ComponentTypeService componentTypeService;

    @Mock
    private EntityRelationService entityRelationService;

    @Mock
    private FileAttachmentService fileAttachmentService;

    @InjectMocks
    private PcBuildService pcBuildService;

    private User testUser;
    private PcBuild testBuild;
    private CreatePcBuildRequest createRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testBuild = new PcBuild(testUser, "Моя игровая сборка", true);
        testBuild.setId(1L);

        createRequest = new CreatePcBuildRequest();
        createRequest.setName("Моя игровая сборка");
        createRequest.setIsPublic(true);
    }

    @Test
    @DisplayName("ТЕСТ-1: getById - должен вернуть сборку при существующем ID")
    void getById_ShouldReturnBuild_WhenIdExists() {
        when(pcBuildRepository.findById(1L)).thenReturn(Optional.of(testBuild));

        PcBuild result = pcBuildService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Моя игровая сборка");
    }

    @Test
    @DisplayName("ТЕСТ-2: createBuild - должен создать новую сборку")
    void createBuild_ShouldCreateNewBuild_WhenValidRequest() {
        when(pcBuildRepository.existsByUserIdAndName(testUser.getId(), "Моя игровая сборка"))
                .thenReturn(false);
        when(pcBuildRepository.save(any(PcBuild.class))).thenReturn(testBuild);

        PcBuild result = pcBuildService.createBuild(testUser, createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Моя игровая сборка");
        verify(pcBuildRepository, times(1)).save(any(PcBuild.class));
    }

    @Test
    @DisplayName("ТЕСТ-3: createBuild - должен выбросить исключение при дубликате имени")
    void createBuild_ShouldThrowException_WhenDuplicateName() {
        when(pcBuildRepository.existsByUserIdAndName(testUser.getId(), "Моя игровая сборка"))
                .thenReturn(true);

        assertThatThrownBy(() -> pcBuildService.createBuild(testUser, createRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже есть сборка с таким именем");
    }
}