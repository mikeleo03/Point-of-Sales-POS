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
    @Mapping(source = "invoiceDetails", target = "invoiceDetails")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Invoice toInvoice(InvoiceDTO invoiceDTO);
}
