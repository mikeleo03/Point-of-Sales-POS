package com.example.fpt_midterm_pos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.ProductDTO;

public interface ProductService {

    List<ProductDTO> findAll();

    Page<ProductDTO> findAll(Pageable pageable);

    List<ProductDTO> findByNameLike(String name);

    Page<ProductDTO> findAllByStatusAndName(String name, Pageable pageable);

    Product save(ProductDTO productDTO);

    Product updateProduct(String id, ProductDTO productDTO);

    Product updateProductStatus(String id, Status status);

    List<ProductDTO> saveProductsFromCSV(MultipartFile file);
}
