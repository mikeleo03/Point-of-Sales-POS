package com.example.fpt_midterm_pos.dto;

import com.example.fpt_midterm_pos.data.model.Customer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private UUID id;
    private int amount;
    private Date date;
    private Date createdAt;
    private Date updatedAt;
    private Customer customer;
}
