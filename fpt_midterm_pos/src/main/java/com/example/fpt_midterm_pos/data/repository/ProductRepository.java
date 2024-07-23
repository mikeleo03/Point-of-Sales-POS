package com.example.fpt_midterm_pos.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fpt_midterm_pos.data.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingAndStatus(String name, Product.Status status);
    Page<Product> findAllByStatus(Product.Status status, Pageable pageable);
    Page<Product> findByStatusAndNameContaining(Product.Status status, String name, Pageable pageable);
}
