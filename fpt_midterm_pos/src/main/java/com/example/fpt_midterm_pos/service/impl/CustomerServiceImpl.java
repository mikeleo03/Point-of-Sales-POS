package com.example.fpt_midterm_pos.service.impl;

import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.service.CustomerService;
import com.example.fpt_midterm_pos.mapper.CustomerMapper;
import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Date;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerShowDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toCustomerShowDTO);
    }

    @Override
    public CustomerDTO createEmployee(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toCustomer(customerDTO);
        customer.setId(UUID.randomUUID());
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) {
        return new CustomerDTO();
    }

    @Override
    public CustomerDTO updateCustomerStatus(UUID id) {
        return new CustomerDTO();
    }
}
