package com.example.fpt_midterm_pos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.dto.InvoiceDetailDTO;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {
    InvoiceDetailMapper INSTANCE = Mappers.getMapper(InvoiceDetailMapper.class);

    @Mapping(source = "id.productId", target = "productId")
    InvoiceDetailDTO toInvoiceDetailDTO(InvoiceDetail invoiceDetail);

    @Mapping(source = "productId", target = "id.productId")
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InvoiceDetail toInvoiceDetail(InvoiceDetailDTO invoiceDetail);
}