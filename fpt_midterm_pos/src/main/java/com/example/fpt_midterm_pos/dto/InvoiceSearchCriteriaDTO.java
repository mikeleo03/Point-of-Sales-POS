package com.example.fpt_midterm_pos.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSearchCriteriaDTO {
    private String customerName;
    private UUID customerId;
    private Date startDate;
    private Date endDate;
    private Integer month;
    private String sortByDate;
    private String sortByAmount;
}

