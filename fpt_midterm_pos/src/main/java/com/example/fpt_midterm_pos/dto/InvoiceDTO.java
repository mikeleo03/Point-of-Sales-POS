package com.example.fpt_midterm_pos.dto;

import java.util.Date;
import java.util.UUID;

import com.example.fpt_midterm_pos.data.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private UUID id;
    private Double amount;
    private Date date;
    private Date createdAt;
    private Date updatedAt;
    private Customer customer;
}
