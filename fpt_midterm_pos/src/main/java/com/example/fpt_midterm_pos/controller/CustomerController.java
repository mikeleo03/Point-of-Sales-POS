package com.example.fpt_midterm_pos.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "Retrieve all Customers.")
    @GetMapping
    public ResponseEntity<Page<CustomerShowDTO>> getAllCustomer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerShowDTO> customerPage = customerService.findAll(pageable);
        if (customerPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(customerPage);
    }

    @Operation(summary = "Create a new Customer.")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customer = customerService.createCustomer(customerSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @Operation(summary = "Update existing Customer.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customer = customerService.updateCustomer(id, customerSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @Operation(summary = "Update existing Customer status from Deactive to Active.")
    @PutMapping(value = "/active/{id}")
    public ResponseEntity<CustomerDTO> updateCustomerStatusActive(@PathVariable("id") UUID id) {
        CustomerDTO customer = customerService.updateCustomerStatus(id, Status.Active);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @Operation(summary = "Update existing Customer status from Active to Deactive.")
    @PutMapping(value = "/deactive/{id}")
    public ResponseEntity<CustomerDTO> updateCustomerStatusDeactive(@PathVariable("id") UUID id) {
        CustomerDTO customer = customerService.updateCustomerStatus(id, Status.Deactive);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
