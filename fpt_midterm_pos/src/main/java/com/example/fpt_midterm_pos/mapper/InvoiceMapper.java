package com.example.fpt_midterm_pos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;

@Mapper(componentModel = "spring", uses = {InvoiceDetailMapper.class})
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "invoiceDetails", target = "invoiceDetails")
    InvoiceDTO toInvoiceDTO(Invoice invoice);

    @Mapping(source = "customerId", target = "customer.id")
    Invoice toInvoice(InvoiceDTO invoiceDTO);
}
