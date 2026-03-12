package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CategoryResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.CategoryAlreadyExistsException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.CategoryCycleException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.CategoryNotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(String name, String slug, Long parentId, String image, Integer order) {
        log.info("Создание новой категории: {}", name);

        if (categoryRepository.existsBySlug(slug)) {
            throw new CategoryAlreadyExistsException("Категория с slug " + slug + " уже существует");
        }

        if (categoryRepository.existsByName(name)) {
            throw new CategoryAlreadyExistsException("Категория с именем " + name + " уже существует");
        }

        Category category = new Category(name, slug);
        category.setImage(image);

        if (parentId != null) {
            Category parent = findCategoryById(parentId);
            category.setParent(parent);
        }

        if (order == null) {
            order = categoryRepository.getMaxOrder(parentId) + 1;
        }
        category.setOrder(order);

        Category savedCategory = categoryRepository.save(category);
        log.info("Категория успешно создана: {}", savedCategory.getName());

        return CategoryResponse.from(savedCategory);
    }

    public CategoryResponse getCategoryById(Long id) {
        log.debug("Получение категории по ID: {}", id);
        Category category = findCategoryById(id);
        return CategoryResponse.from(category);
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        log.debug("Получение категории по slug: {}", slug);
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена: " + slug));
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> getAllRootCategories() {
        log.debug("Получение всех корневых категорий");
        return categoryRepository.findByParentIsNullOrderByOrderAsc().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoryChildren(Long parentId) {
        log.debug("Получение дочерних категорий для родителя: {}", parentId);
        Category parent = findCategoryById(parentId);
        return categoryRepository.findByParentOrderByOrderAsc(parent).stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoryTree() {
        log.debug("Получение полного дерева категорий");
        return categoryRepository.findAllRootCategoriesWithChildren().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoryPath(Long categoryId) {
        log.debug("Получение пути к категории: {}", categoryId);
        List<Object[]> pathData = categoryRepository.findCategoryPath(categoryId);

        return pathData.stream()
                .map(data -> CategoryResponse.builder()
                        .id(((Number) data[0]).longValue())
                        .name((String) data[1])
                        .slug((String) data[2])
                        .build())
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getBreadcrumbs(Long categoryId) {
        log.debug("Получение хлебных крошек для категории: {}", categoryId);
        List<Object[]> breadcrumbs = categoryRepository.findBreadcrumbs(categoryId);

        return breadcrumbs.stream()
                .map(data -> CategoryResponse.builder()
                        .id(((Number) data[0]).longValue())
                        .name((String) data[1])
                        .slug((String) data[2])
                        .build())
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoriesWithProducts() {
        log.debug("Получение категорий с доступными товарами");
        return categoryRepository.findCategoriesWithAvailableProducts().stream()
                .map(CategoryResponse::simple)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getParentCategories() {
        log.debug("Получение родительских категорий");
        return categoryRepository.findParentCategories().stream()
                .map(CategoryResponse::simple)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getLeafCategories() {
        log.debug("Получение конечных категорий");
        return categoryRepository.findLeafCategories().stream()
                .map(CategoryResponse::simple)
                .collect(Collectors.toList());
    }

    public Page<CategoryResponse> getAllCategoriesPaginated(Pageable pageable) {
        log.debug("Получение списка категорий, страница: {}, размер: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return categoryRepository.findAllByOrderByOrderAsc(pageable)
                .map(CategoryResponse::simple);
    }

    public Page<CategoryResponse> searchCategories(String searchTerm, Pageable pageable) {
        log.debug("Поиск категорий по запросу: {}", searchTerm);
        return categoryRepository.searchCategories(searchTerm, pageable)
                .map(CategoryResponse::simple);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, String name, String slug,
                                           Long parentId, String image, Integer order) {
        log.info("Обновление категории: {}", id);
        Category category = findCategoryById(id);

        if (!category.getSlug().equals(slug) && categoryRepository.existsBySlug(slug)) {
            throw new CategoryAlreadyExistsException("Категория с slug " + slug + " уже существует");
        }

        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new CategoryAlreadyExistsException("Категория с именем " + name + " уже существует");
        }

        category.setName(name);
        category.setSlug(slug);
        category.setImage(image);

        if (parentId != null) {
            if (parentId.equals(id)) {
                throw new CategoryCycleException("Категория не может быть родителем самой себя");
            }

            Category newParent = findCategoryById(parentId);

            if (isDescendant(id, parentId)) {
                throw new CategoryCycleException("Нельзя сделать потомка родителем");
            }

            category.setParent(newParent);
        } else {
            category.setParent(null);
        }

        if (order != null) {
            category.setOrder(order);
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Категория успешно обновлена: {}", updatedCategory.getName());

        return CategoryResponse.from(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Удаление категории: {}", id);
        Category category = findCategoryById(id);

        if (!category.getChildren().isEmpty()) {
            log.warn("Попытка удалить категорию с дочерними элементами: {}", id);
        }

        categoryRepository.delete(category);
        log.info("Категория успешно удалена: {}", category.getName());
    }

    @Transactional
    public void moveCategory(Long id, Long newParentId, Integer newOrder) {
        log.info("Перемещение категории {} в родителя {} с порядком {}", id, newParentId, newOrder);
        Category category = findCategoryById(id);

        Long oldParentId = category.getParent() != null ? category.getParent().getId() : null;

        if (newParentId != null && !newParentId.equals(oldParentId)) {
            if (newParentId.equals(id)) {
                throw new CategoryCycleException("Категория не может быть родителем самой себя");
            }

            if (isDescendant(id, newParentId)) {
                throw new CategoryCycleException("Нельзя переместить категорию в своего потомка");
            }

            Category newParent = findCategoryById(newParentId);
            category.setParent(newParent);
        } else if (newParentId == null && oldParentId != null) {
            category.setParent(null);
        }

        if (newOrder != null) {
            category.setOrder(newOrder);
        }

        categoryRepository.save(category);
        log.info("Категория успешно перемещена");
    }

    @Transactional
    public CategoryResponse changeOrder(Long id, Integer newOrder) {
        log.info("Изменение порядка категории {} на {}", id, newOrder);
        Category category = findCategoryById(id);
        category.setOrder(newOrder);
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.from(updatedCategory);
    }

    @Transactional
    public void reorderCategories(Long parentId, List<Long> categoryIds) {
        log.info("Переупорядочивание категорий для родителя: {}", parentId);
        for (int i = 0; i < categoryIds.size(); i++) {
            Long categoryId = categoryIds.get(i);
            Category category = findCategoryById(categoryId);
            category.setOrder(i);
            categoryRepository.save(category);
        }
        log.info("Категории успешно переупорядочены");
    }

    public boolean isDescendant(Long categoryId, Long potentialDescendantId) {
        log.debug("Проверка, является ли категория {} потомком {}", potentialDescendantId, categoryId);
        List<Category> descendants = categoryRepository.findAllDescendants(categoryId);
        return descendants.stream().anyMatch(c -> c.getId().equals(potentialDescendantId));
    }

    public Integer getMaxDepth() {
        log.debug("Получение максимальной глубины дерева категорий");
        return categoryRepository.getMaxDepth();
    }

    public List<CategoryResponse> getEmptyCategories() {
        log.debug("Получение пустых категорий");
        return categoryRepository.findEmptyCategories().stream()
                .map(CategoryResponse::simple)
                .collect(Collectors.toList());
    }

    @Transactional
    public long deleteEmptyCategories() {
        log.info("Удаление пустых категорий");
        long deleted = categoryRepository.deleteEmptyCategories();
        log.info("Удалено {} пустых категорий", deleted);
        return deleted;
    }

    @Transactional
    public CategoryResponse setCategoryImage(Long id, String imageUrl) {
        log.info("Установка изображения для категории: {}", id);
        Category category = findCategoryById(id);
        category.setImage(imageUrl);
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.from(updatedCategory);
    }

    public List<Object[]> getCategoryStatistics() {
        log.debug("Получение статистики по категориям");
        return categoryRepository.getCategoryStatistics();
    }

    public long getTotalCategoriesCount() {
        log.debug("Получение общего количества категорий");
        return categoryRepository.count();
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена с ID: " + id));
    }
}