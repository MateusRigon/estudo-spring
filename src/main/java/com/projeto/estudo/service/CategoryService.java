package com.projeto.estudo.service;

import com.projeto.estudo.handler.DuplicateResourceException;
import com.projeto.estudo.handler.ResourceNotFoundException;
import com.projeto.estudo.model.Category;
import com.projeto.estudo.model.Product;
import com.projeto.estudo.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found."));
    }

    public Category createCategory(@Valid Category payload) {
        ensureUniqueName(payload.getName(), Optional.empty());
        return categoryRepository.save(payload);
    }

    public Category updateCategory(Long id, @Valid Category payload) {
        Category existingCategory = getCategoryById(id);
        ensureUniqueName(payload.getName(), Optional.of(id));

        existingCategory.setName(payload.getName());
        return categoryRepository.save(existingCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);

        Set<Product> products = new HashSet<>(category.getProducts());
        for (Product product : products) {
            category.removeProduct(product);
        }

        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }
    }

    private void ensureUniqueName(String name, Optional<Long> currentCategoryId) {
        categoryRepository.findByNameIgnoreCase(name)
                .ifPresent(existingCategory -> {
                    boolean isAnotherCategory = currentCategoryId
                            .map(id -> !existingCategory.getId().equals(id))
                            .orElse(true);

                    if (isAnotherCategory) {
                        throw new DuplicateResourceException("Category with name '" + name + "' already exists.");
                    }
                });
    }
}
