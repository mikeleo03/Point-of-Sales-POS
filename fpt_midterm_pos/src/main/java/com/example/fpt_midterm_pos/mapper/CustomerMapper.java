package com.example.fpt_midterm_pos.mapper;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "invoice", source = "invoice")
    CustomerDTO toCustomerDTO(Customer customer);

    @Mapping(target = "invoice", source = "invoice")
    Customer toCustomer(CustomerDTO customerDTO);

    CustomerShowDTO toCustomerShowDTO(Customer customer);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    Customer toCustomer(CustomerShowDTO customerShowDTO);
}
