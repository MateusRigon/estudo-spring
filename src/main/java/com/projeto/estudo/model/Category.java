package com.projeto.estudo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name of category is mandatory")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnoreProperties("categories")
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        if (products.add(product)) {
            product.getCategories().add(this);
        }
    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            product.getCategories().remove(this);
        }
    }
}
