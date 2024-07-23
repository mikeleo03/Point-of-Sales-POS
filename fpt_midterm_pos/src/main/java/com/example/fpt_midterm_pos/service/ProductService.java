package com.example.fpt_midterm_pos.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.data.model.Product;

public interface ProductService {

    List<ProductDTO> findAll();

    Page<ProductDTO> findAll(Pageable pageable);

    List<ProductDTO> findByNameLike(String name);

    Page<ProductDTO> findAllByStatusAndName(String name, Pageable pageable);

    Product save(ProductDTO productDTO);

    Product updateProduct(String id, ProductDTO productDTO);

    Product updateProductStatus(String id, Product.Status status);

    List<ProductDTO> saveProductsFromCSV(MultipartFile file);
}
