package com.example.fpt_midterm_pos.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import com.example.fpt_midterm_pos.data.model.Product;

@Data
public class ProductDTO {
    private String id;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    private String name;

    @Min(value = 0, message = "Price must be a positive number")
    private int price;

    private Product.Status status;

    private Integer quantity;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

}
