package com.example.fpt_midterm_pos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSearchCriteriaDTO;

public interface ProductService {

    Page<ProductDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable);

    Product save(ProductDTO productDTO);

    Product updateProduct(UUID id, ProductDTO productDTO);

    Product updateProductStatus(UUID id, Status status);

    List<ProductDTO> saveProductsFromCSV(MultipartFile file);
}
