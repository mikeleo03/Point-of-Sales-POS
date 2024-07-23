package com.example.fpt_midterm_pos.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}
