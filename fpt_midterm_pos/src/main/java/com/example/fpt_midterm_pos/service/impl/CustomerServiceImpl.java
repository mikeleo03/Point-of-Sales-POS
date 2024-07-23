package com.example.fpt_midterm_pos.service.impl;

import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.service.CustomerService;
import com.example.fpt_midterm_pos.mapper.CustomerMapper;
import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;
import com.example.fpt_midterm_pos.data.model.Status;

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
    public CustomerDTO createCustomer(CustomerSaveDTO customerSaveDTO) {
        Customer customer = customerMapper.toCustomer(customerSaveDTO);
        customer.setId(UUID.randomUUID());
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) {
        Customer custCheck = customerRepository.findById(id).orElse(null);
        if(custCheck != null) {
            Customer customer = customerMapper.toCustomer(customerDTO);
            custCheck.setName(customer.getName());
            custCheck.setPhoneNumber(customer.getPhoneNumber());
            custCheck.setUpdatedAt(new Date());
            Customer updatedCustomer = customerRepository.save(custCheck);
            return customerMapper.toCustomerDTO(updatedCustomer);
        }
        return null;
    }

    @Override
    public CustomerDTO updateCustomerStatus(UUID id, Status status) {
        Customer custCheck = customerRepository.findById(id).orElse(null);
        if(custCheck != null) {
            if(status != custCheck.getStatus()) {
                if(status == Status.Active) {
                    custCheck.setStatus(Status.Deactive);
                } else if(status == Status.Deactive) {
                    custCheck.setStatus(Status.Active);
                }
                Customer updatedCustomer = customerRepository.save(custCheck);
                return customerMapper.toCustomerDTO(updatedCustomer);
            }
            return customerMapper.toCustomerDTO(custCheck);
        }
        return null;
    }
}
