package com.example.fpt_midterm_pos.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.dto.*;
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
import com.example.fpt_midterm_pos.mapper.ProductMapper;
import com.example.fpt_midterm_pos.service.ProductService;
import com.example.fpt_midterm_pos.utils.FileUtils;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public Page<ProductShowDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable) {
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
        return products.map(productMapper::toShowDTO);
    }

    @Override
    public ProductDTO save(ProductSaveDTO productSaveDTO) {
        Product product = productMapper.toProduct(productSaveDTO);
        product.setStatus(Status.Active); // Ensure the product is set to active when saving
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(UUID id, ProductSaveDTO productSaveDTO) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productSaveDTO.getName());
            product.setPrice(productSaveDTO.getPrice());
            product.setQuantity(productSaveDTO.getQuantity());
            product.setUpdatedAt(new Date());
            Product updateProduct = productRepository.save(product);
            return productMapper.toProductDTO(updateProduct);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

//    @Override
//    public ProductDTO updateProductStatus(UUID id, Status status) {
//        Optional<Product> productOpt = productRepository.findById(id);
//        if (productOpt.isPresent()) {
//            Product product = productOpt.get();
//            product.setStatus(status);
//            Product updateProduct = productRepository.save(product);
//            return productMapper.toProductDTO(updateProduct);
//        } else {
//            throw new RuntimeException("Product not found");
//        }
//    }
    @Override
    public ProductDTO updateProductStatus(UUID id, Status status) {
        Product prodCheck = productRepository.findById(id).orElse(null);
        if(prodCheck != null) {
            if(status != prodCheck.getStatus()) {
                if(prodCheck.getStatus() == Status.Active) {
                    prodCheck.setStatus(Status.Deactive);
                } else if(prodCheck.getStatus() == Status.Deactive) {
                    prodCheck.setStatus(Status.Active);
                }
                Product updatedProduct = productRepository.save(prodCheck);
                return productMapper.toProductDTO(updatedProduct);
            }
            return productMapper.toProductDTO(prodCheck);
        }
        return null;
    }

//
//    @Override
//    public List<ProductDTO> saveProductsFromCSV(MultipartFile file) {
//        if (!FileUtils.hasCSVFormat(file)) {
//            throw new IllegalArgumentException("Invalid file format. Only CSV files are accepted.");
//        }
//
//        try {
//            List<Product> products = FileUtils.readProductsFromCSV(file);
//
//            // Save products to the database
//            List<Product> savedProducts = productRepository.saveAll(products);
//
//            // Convert saved products to DTO
//            List<ProductDTO> productDTOs = savedProducts.stream()
//                    .map(productMapper::toDTO)
//                    .collect(Collectors.toList());
//
//            return productDTOs;
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
//        }
//    }

}
