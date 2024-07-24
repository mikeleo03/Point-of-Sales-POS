package com.example.fpt_midterm_pos.data.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameContainingAndStatus(String name, Status status);

    Page<Product> findAllByStatus(Status status, Pageable pageable);

    Page<Product> findByStatusAndNameContaining(Status status, String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
    "(:name IS NULL OR p.name LIKE %:name%) AND " +
    "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
    "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findByFilters(@Param("name") String name,
                            @Param("minPrice") Double minPrice,
                            @Param("maxPrice") Double maxPrice,
                            Pageable pageable);
}
