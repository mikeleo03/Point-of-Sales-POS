package com.example.fpt_midterm_pos.controller;

import org.springframework.web.bind.annotation.*;

import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.service.CustomerService;
import com.example.fpt_midterm_pos.data.model.Status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerShowDTO>> getAllCustomer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerShowDTO> customerPage = customerService.findAll(pageable);
        if (customerPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customerPage);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customer = customerService.createCustomer(customerSaveDTO);
        return ResponseEntity.ok(customer);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customer = customerService.updateCustomer(id, customerSaveDTO);
        return ResponseEntity.ok(customer);
    }

    @PutMapping(value = "/active/{id}")
    public ResponseEntity<CustomerDTO> updateCustomerStatusActive(@PathVariable("id") UUID id) {
        CustomerDTO customer = customerService.updateCustomerStatus(id, Status.Active);
        return ResponseEntity.ok(customer);
    }

    @PutMapping(value = "/deactive/{id}")
    public ResponseEntity<CustomerDTO> updateCustomerStatusDeactive(@PathVariable("id") UUID id) {
        CustomerDTO customer = customerService.updateCustomerStatus(id, Status.Deactive);
        return ResponseEntity.ok(customer);
    }
}
