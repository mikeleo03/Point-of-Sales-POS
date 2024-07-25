package com.example.fpt_midterm_pos.service;

import java.util.List;
import java.util.UUID;

import com.example.fpt_midterm_pos.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Status;

public interface ProductService {

    Page<ProductShowDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable);

    ProductDTO save(ProductSaveDTO productSaveDTO);

    ProductDTO updateProduct(UUID id, ProductSaveDTO productSaveDTO);

    ProductDTO updateProductStatus(UUID id, Status status);
//
//    List<ProductShowDTO> saveProductsFromCSV(MultipartFile file);
}
