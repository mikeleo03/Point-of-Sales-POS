package com.example.fpt_midterm_pos.dto;

import com.example.fpt_midterm_pos.data.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private UUID Id;
    private String name;
    private String phoneNumber;
    private Status status;
    private Date createdAt;
    private Date updatedAt;
    private List<InvoiceDTO> invoice;
}
