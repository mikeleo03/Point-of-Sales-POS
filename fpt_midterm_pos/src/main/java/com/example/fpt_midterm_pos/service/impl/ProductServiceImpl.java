package com.example.fpt_midterm_pos.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.mapper.ProductMapper;
import com.example.fpt_midterm_pos.service.ProductService;
import com.example.fpt_midterm_pos.utils.FileUtils;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAllByStatus(Status.Active, Pageable.unpaged())
                .getContent()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAllByStatus(Status.Active, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public List<ProductDTO> findByNameLike(String name) {
        return productRepository.findByNameContainingAndStatus(name, Status.Active)
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findAllByStatusAndName(String name, Pageable pageable) {
        return productRepository.findByStatusAndNameContaining(Status.Active, name, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public Product save(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product.setId(UUID.randomUUID());
        product.setStatus(Status.Active);  // Ensure the product is set to active when saving
        product.setCreatedAt(LocalDate.now());
        product.setUpdatedAt(LocalDate.now());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(UUID id, ProductDTO productDTO) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            product.setUpdatedAt(LocalDate.now());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public Product updateProductStatus(UUID id, Status status) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus(status);
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
    @Override
    public List<ProductDTO> saveProductsFromCSV(MultipartFile file) {
        if (!FileUtils.hasCSVFormat(file)) {
            throw new IllegalArgumentException("Invalid file format. Only CSV files are accepted.");
        }

        try {
            List<Product> products = FileUtils.readProductsFromCSV(file);

            // Save products to the database
            List<Product> savedProducts = productRepository.saveAll(products);

            // Convert saved products to DTO
            List<ProductDTO> productDTOs = savedProducts.stream()
                    .map(productMapper::toDTO)
                    .collect(Collectors.toList());

            return productDTOs;
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
    }

}
