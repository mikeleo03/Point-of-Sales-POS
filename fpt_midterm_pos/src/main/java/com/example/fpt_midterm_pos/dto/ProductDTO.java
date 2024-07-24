package com.example.fpt_midterm_pos.dto;

import java.time.LocalDate;

import com.example.fpt_midterm_pos.data.model.Status;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    private String name;

    @Min(value = 0, message = "Price must be a positive number")
    private Double price;

    private Status status;

    private Integer quantity;
}