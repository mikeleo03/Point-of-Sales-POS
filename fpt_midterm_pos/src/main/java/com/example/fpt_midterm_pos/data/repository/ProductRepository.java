package com.example.fpt_midterm_pos.data.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameContainingAndStatus(String name, Status status);
    Page<Product> findAllByStatus(Status status, Pageable pageable);
    Page<Product> findByStatusAndNameContaining(Status status, String name, Pageable pageable);
}
