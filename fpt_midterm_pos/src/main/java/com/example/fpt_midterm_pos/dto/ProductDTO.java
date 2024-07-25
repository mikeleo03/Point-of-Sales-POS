package com.example.fpt_midterm_pos.dto;

import com.example.fpt_midterm_pos.data.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;
    private String name;
    private Double price;
    private Status status;
    private Integer quantity;
}