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
}