package com.example.fpt_midterm_pos.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 10)
    private int amount;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable=false, insertable = false, updatable = false)
    private Customer customer;
}
