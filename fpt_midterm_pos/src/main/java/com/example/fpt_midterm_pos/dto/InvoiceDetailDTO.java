package com.example.fpt_midterm_pos.dto;

import com.example.fpt_midterm_pos.data.model.InvoiceDetailKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDTO {
    private InvoiceDetailKey id;
    private Integer quantity;
    private Double price;
    private Double amount;
}
