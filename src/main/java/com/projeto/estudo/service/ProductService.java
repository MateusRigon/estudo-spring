package com.projeto.estudo.service;

import com.projeto.estudo.handler.DuplicateResourceException;
import com.projeto.estudo.handler.InvalidRequestException;
import com.projeto.estudo.handler.ResourceNotFoundException;
import com.projeto.estudo.model.Category;
import com.projeto.estudo.model.Product;
import com.projeto.estudo.repository.CategoryRepository;
import com.projeto.estudo.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
    }

    public Product createProduct(@Valid Product payload) {
        productRepository.findByNameIgnoreCase(payload.getName())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Product with name '" + payload.getName() + "' already exists.");
                });

        Product product = new Product();
        product.setName(payload.getName());
        product.setPrice(payload.getPrice());

        Set<Category> resolvedCategories = resolveCategories(payload.getCategories());
        for (Category category : resolvedCategories) {
            product.addCategory(category);
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, @Valid Product payload) {
        Product existingProduct = getProductById(id);

        productRepository.findByNameIgnoreCase(payload.getName())
                .filter(product -> !product.getId().equals(id))
                .ifPresent(product -> {
                    throw new DuplicateResourceException("Product with name '" + payload.getName() + "' already exists.");
                });

        existingProduct.setName(payload.getName());
        existingProduct.setPrice(payload.getPrice());

        Set<Category> currentCategories = new HashSet<>(existingProduct.getCategories());
        for (Category category : currentCategories) {
            existingProduct.removeCategory(category);
        }

        Set<Category> resolvedCategories = resolveCategories(payload.getCategories());
        for (Category category : resolvedCategories) {
            existingProduct.addCategory(category);
        }

        return productRepository.save(existingProduct);
    }

    @Transactional
    public Product addCategoryToProduct(Long productId, Long categoryId) {
        Product product = getProductById(productId);
        Category category = getCategoryById(categoryId);

        product.addCategory(category);
        return productRepository.save(product);
    }

    @Transactional
    public Product removeCategoryFromProduct(Long productId, Long categoryId) {
        Product product = getProductById(productId);
        Category category = getCategoryById(categoryId);

        product.removeCategory(category);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);

        Set<Category> categories = new HashSet<>(product.getCategories());
        for (Category category : categories) {
            product.removeCategory(category);
        }

        productRepository.delete(product);
    }

    private Set<Category> resolveCategories(Set<Category> categories) {
        Set<Category> resolvedCategories = new HashSet<>();

        if (categories == null || categories.isEmpty()) {
            return resolvedCategories;
        }

        for (Category categoryPayload : categories) {
            if (categoryPayload.getId() != null) {
                resolvedCategories.add(getCategoryById(categoryPayload.getId()));
                continue;
            }

            String categoryName = categoryPayload.getName();
            if (categoryName == null || categoryName.isBlank()) {
                throw new InvalidRequestException("Category id or name must be provided for product categories.");
            }

            Category category = categoryRepository.findByNameIgnoreCase(categoryName.trim())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName.trim());
                        return categoryRepository.save(newCategory);
                    });

            resolvedCategories.add(category);
        }

        return resolvedCategories;
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found."));
    }
}
