package com.example.fpt_midterm_pos.mapper;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    // Mapper to Product DTO
    ProductDTO toDTO(Product product);

    // Mapper to Product model
    @Mapping(target = "ID", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductDTO employeeDTO);
}