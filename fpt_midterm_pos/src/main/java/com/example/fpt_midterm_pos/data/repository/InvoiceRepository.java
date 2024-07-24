package com.example.fpt_midterm_pos.data.repository;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    
    @Query("SELECT i FROM Invoice i WHERE " +
           "(:customerName IS NULL OR i.customer.name LIKE %:customerName%) AND " +
           "(:customerId IS NULL OR i.customer.id = :customerId) AND " +
           "(:startDate IS NULL OR :endDate IS NULL OR i.date BETWEEN :startDate AND :endDate) AND " +
           "(:month IS NULL OR MONTH(i.date) = :month)")
    Page<Invoice> findByFilters(@Param("customerName") String customerName,
                                @Param("customerId") UUID customerId,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate,
                                @Param("month") Integer month,
                                Pageable pageable);
}