package com.example.fpt_midterm_pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveDTO {
    private String name;
    private String phoneNumber;
}
