package com.projeto.estudo.controller;

import com.projeto.estudo.dto.ApiResponse;
import com.projeto.estudo.model.Product;
import com.projeto.estudo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully.", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully.", product));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product payload) {
        Product product = productService.createProduct(payload);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully.", product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product payload
    ) {
        Product product = productService.updateProduct(id, payload);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully.", product));
    }

    @PutMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Product>> addCategoryToProduct(
            @PathVariable Long productId,
            @PathVariable Long categoryId
    ) {
        Product product = productService.addCategoryToProduct(productId, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category linked to product successfully.", product));
    }

    @DeleteMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Product>> removeCategoryFromProduct(
            @PathVariable Long productId,
            @PathVariable Long categoryId
    ) {
        Product product = productService.removeCategoryFromProduct(productId, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category removed from product successfully.", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully.", null));
    }
}
