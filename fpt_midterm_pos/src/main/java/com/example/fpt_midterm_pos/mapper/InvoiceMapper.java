package com.example.fpt_midterm_pos.mapper;

import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    InvoiceDTO toInvoiceDTO(Invoice invoice);
    Invoice toInvoice(InvoiceDTO invoiceDTO);
}
