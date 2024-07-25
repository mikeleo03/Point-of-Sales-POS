package com.example.fpt_midterm_pos.service;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.data.model.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {
    Page<CustomerShowDTO> findAll(Pageable pageable);
    CustomerDTO createCustomer(CustomerSaveDTO customerDTO);
    CustomerDTO updateCustomer(UUID id, CustomerSaveDTO customerSaveDTO);
    CustomerDTO updateCustomerStatus(UUID id, Status status);

    Customer findById(UUID id);
}
