package com.example.fpt_midterm_pos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSaveDTO;

@Mapper(componentModel = "spring", uses = {InvoiceDetailMapper.class})
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    // Invoice - InvoiceSaveDTO
    @Mapping(source = "customer.id", target = "customerId")
    InvoiceSaveDTO toInvoiceSaveDTO(Invoice invoice);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Invoice toInvoice(InvoiceSaveDTO invoiceDTO);

    // Invoice - InvoiceDTO
    @Mapping(source = "customer.id", target = "customerId")
    InvoiceDTO toInvoiceDTO(Invoice invoice);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Invoice toInvoice(InvoiceDTO invoiceDTO);
}
