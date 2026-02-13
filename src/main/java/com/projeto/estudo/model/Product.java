package com.projeto.estudo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name of product is mandatory")
    @Column(nullable = false)
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnoreProperties("products")
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        if (categories.add(category)) {
            category.getProducts().add(this);
        }
    }

    public void removeCategory(Category category) {
        if (categories.remove(category)) {
            category.getProducts().remove(this);
        }
    }
}
