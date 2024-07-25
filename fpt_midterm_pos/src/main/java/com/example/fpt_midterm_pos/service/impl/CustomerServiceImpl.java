package com.example.fpt_midterm_pos.service.impl;

import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.service.CustomerService;
import com.example.fpt_midterm_pos.mapper.CustomerMapper;
import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.data.model.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Date;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves a paginated list of all customers from the repository.
     *
     * @param pageable The pagination parameters, including the page number and size.
     * @return A Page object containing a list of {@link CustomerShowDTO} objects representing the customers on the specified page.
     */
    @Override
    public Page<CustomerShowDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toCustomerShowDTO);
    }

    /**
     * Creates a new customer in the repository and returns the corresponding {@link CustomerDTO} object.
     *
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the details of the new customer to be created.
     * @return A {@link CustomerDTO} object representing the newly created customer.
     */
    @Override
    public CustomerDTO createCustomer(CustomerSaveDTO customerSaveDTO) {
        Customer customer = customerMapper.toCustomer(customerSaveDTO);
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(savedCustomer);
    }

    /**
     * Updates an existing customer in the repository with the provided details from the {@link CustomerSaveDTO} object.
     *
     * @param id The unique identifier of the customer to be updated.
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the details of the new customer to be updated.
     * @return A {@link CustomerDTO} object representing the updated customer.
     * @throws ResourceNotFoundException if the customer with the given ID is not found.
     */
    @Override
    public CustomerDTO updateCustomer(UUID id, CustomerSaveDTO customerSaveDTO) {
        Customer custCheck = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Customer customer = customerMapper.toCustomer(customerSaveDTO);
        custCheck.setName(customer.getName());
        custCheck.setPhoneNumber(customer.getPhoneNumber());
        custCheck.setUpdatedAt(new Date());
        Customer updatedCustomer = customerRepository.save(custCheck);
        return customerMapper.toCustomerDTO(updatedCustomer);
    }

    /**
     * Updates the status of an existing customer in the repository.
     *
     * @param id The unique identifier of the customer whose status is to be updated.
     * @param status The new status to be assigned to the customer.
     * @return A {@link CustomerDTO} object representing the updated customer.
     * @throws ResourceNotFoundException if the customer with the given ID is not found.
     */
    @Override
    public CustomerDTO updateCustomerStatus(UUID id, Status status) {
        Customer custCheck = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (status != custCheck.getStatus()) {
            if (custCheck.getStatus() == Status.Active) {
                custCheck.setStatus(Status.Deactive);
            } else if (custCheck.getStatus() == Status.Deactive) {
                custCheck.setStatus(Status.Active);
            }
            custCheck.setUpdatedAt(new Date());
            Customer updatedCustomer = customerRepository.save(custCheck);
            return customerMapper.toCustomerDTO(updatedCustomer);
        }
        return customerMapper.toCustomerDTO(custCheck);
    }
}