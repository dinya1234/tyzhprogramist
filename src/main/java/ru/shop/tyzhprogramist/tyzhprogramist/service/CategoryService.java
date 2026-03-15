package ru.shop.tyzhprogramist.tyzhprogramist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.tyzhprogramist.tyzhprogramist.dto.response.CategoryResponse;
import ru.shop.tyzhprogramist.tyzhprogramist.entity.Category;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.BadRequestException;
import ru.shop.tyzhprogramist.tyzhprogramist.exception.NotFoundException;
import ru.shop.tyzhprogramist.tyzhprogramist.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findByParentIsNullOrderByOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Category> getRootCategoriesWithChildren() {
        return categoryRepository.findAllRootCategoriesWithChildren();
    }

    @Transactional(readOnly = true)
    public Category getBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Категория не найдена: " + slug));
    }

    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена с id: " + id));
    }

    @Transactional
    public Category createCategory(String name, String slug, Long parentId, String image, Integer order) {
        if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Категория с таким slug уже существует");
        }
        if (categoryRepository.existsByName(name)) {
            throw new BadRequestException("Категория с таким именем уже существует");
        }

        Category category = new Category(name, slug);
        category.setImage(image);

        if (parentId != null) {
            Category parent = getById(parentId);
            category.setParent(parent);
        }

        if (order == null) {
            order = categoryRepository.getMaxOrder(parentId) + 1;
        }
        category.setOrder(order);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, String name, String slug, Long parentId, String image, Integer order) {
        Category category = getById(id);

        if (!category.getSlug().equals(slug) && categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Категория с таким slug уже существует");
        }

        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new BadRequestException("Категория с таким именем уже существует");
        }

        category.setName(name);
        category.setSlug(slug);
        category.setImage(image);

        if (parentId != null) {
            Category newParent = getById(parentId);

            if (isAncestor(newParent, category.getId())) {
                throw new BadRequestException("Нельзя сделать категорию потомком самой себя");
            }

            category.setParent(newParent);
        } else {
            category.setParent(null);
        }

        if (order != null && !order.equals(category.getOrder())) {
            Long parentForOrder = parentId;
            int oldOrder = category.getOrder();
            int maxOrder = categoryRepository.getMaxOrder(parentForOrder);

            if (order > maxOrder) {
                order = maxOrder + 1;
            }

            if (order < oldOrder) {
                categoryRepository.shiftOrders(parentForOrder, order, 1);
            } else if (order > oldOrder) {
                categoryRepository.shiftOrders(parentForOrder, oldOrder + 1, -1);
            }

            category.setOrder(order);
        }

        return categoryRepository.save(category);
    }

    private boolean isAncestor(Category category, Long targetId) {
        if (category.getId().equals(targetId)) {
            return true;
        }

        Category current = category;
        while (current.getParent() != null) {
            if (current.getParent().getId().equals(targetId)) {
                return true;
            }
            current = current.getParent();
        }

        return false;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getById(id);

        if (!category.getProducts().isEmpty()) {
            if (category.getParent() != null) {
                categoryRepository.reassignProductsToCategory(id, category.getParent().getId());
            } else {
                throw new BadRequestException("Нельзя удалить корневую категорию с товарами");
            }
        }

        if (!category.getChildren().isEmpty()) {
            for (Category child : category.getChildren()) {
                child.setParent(category.getParent());
                categoryRepository.save(child);
            }
        }

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBreadcrumbs(Long categoryId) {
        List<Object[]> results = categoryRepository.findBreadcrumbs(categoryId);
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> crumb = new HashMap<>();
            crumb.put("id", row[0]);
            crumb.put("name", row[1]);
            crumb.put("slug", row[2]);
            breadcrumbs.add(crumb);
        }

        return breadcrumbs;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNullOrderByOrderAsc();
        return buildTree(rootCategories);
    }

    private List<Map<String, Object>> buildTree(List<Category> categories) {
        List<Map<String, Object>> tree = new ArrayList<>();

        for (Category category : categories) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", category.getId());
            node.put("name", category.getName());
            node.put("slug", category.getSlug());
            node.put("image", category.getImage());
            node.put("order", category.getOrder());

            if (!category.getChildren().isEmpty()) {
                node.put("children", buildTree(category.getChildren()));
            }

            tree.add(node);
        }

        return tree;
    }

    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAllByOrderByOrderAsc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Category> searchCategories(String searchTerm, Pageable pageable) {
        return categoryRepository.searchCategories(searchTerm, pageable);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getCategoryStatistics() {
        return categoryRepository.getCategoryStatistics();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoriesWithProductCount() {
        List<Object[]> results = categoryRepository.findAllCategoriesWithProductCount();
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] row : results) {
            Category category = (Category) row[0];
            Long count = (Long) row[1];

            Map<String, Object> stat = new HashMap<>();
            stat.put("category", CategoryResponse.from(category));
            stat.put("productCount", count);
            stats.add(stat);
        }

        return stats;
    }

    @Transactional(readOnly = true)
    public Integer getMaxDepth() {
        return categoryRepository.getMaxDepth();
    }

    @Transactional
    public int deleteEmptyCategories() {
        return categoryRepository.deleteEmptyCategories();
    }
}