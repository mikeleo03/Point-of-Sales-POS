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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSearchCriteriaDTO;
import com.example.fpt_midterm_pos.mapper.ProductMapper;
import com.example.fpt_midterm_pos.service.ProductService;
import com.example.fpt_midterm_pos.utils.FileUtils;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public Page<ProductDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable) {
        // Listing all the criteria
        String productName = criteria.getName();
        String sortByName = criteria.getSortByName();
        String sortByPrice = criteria.getSortByPrice();
        Double minPrice = criteria.getMinPrice();
        Double maxPrice = criteria.getMaxPrice();

        // Define the sort rules
        Sort sort = Sort.unsorted();
        if (sortByName != null) {
            sort = sort.and(Sort.by("name").ascending());
            if (sortByName.equalsIgnoreCase("desc")) {
                sort = sort.and(Sort.by("name").descending());
            }
        }
        if (sortByPrice != null) {
            sort = sort.and(Sort.by("price").ascending());
            if (sortByPrice.equalsIgnoreCase("desc")) {
                sort = sort.and(Sort.by("price").descending());
            }
        }

        // Set the pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Get the product data from the repo
        Page<Product> products = productRepository.findByFilters(productName, minPrice, maxPrice, sortedPageable);
        return products.map(productMapper::toDTO);
    }

    @Override
    public Product save(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product.setStatus(Status.Active); // Ensure the product is set to active when saving
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
