package com.honeybadgersoftware.productservice.product.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @SequenceGenerator(name = "product_seq_gen", sequenceName = "product_sequence", allocationSize = 100)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    @NotNull
    private String name;
    @NotNull
    private String manufacturer;
    @Size(max = 5000)
    private String description;
    private String imageUrl;

}
