package com.honeybadgersoftware.productservice.product.repository;

import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findAll(Pageable pageable);

    @Query(value = "SELECT e.id FROM ProductEntity e WHERE e.name = :name AND e.manufacturer = :manufacturer")
    Optional<Long> findIdByNameAndManufacturer(String name, String manufacturer);

    List<ProductEntity> findAllByNameOrManufacturer(String name, String manufacturer);
}
