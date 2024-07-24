package com.example.fpt_midterm_pos.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping(params = {"page", "size"})
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @GetMapping(params = "name")
    public List<ProductDTO> getProductsByName(@RequestParam String name) {
        return productService.findByNameLike(name);
    }

    @GetMapping(params = {"name", "sortBy", "sortOrder", "page", "size"})
    public Page<ProductDTO> getProductsByNameWithSortingAndPagination(
            @RequestParam String name,
            @RequestParam String sortBy,
            @RequestParam String sortOrder,
            @RequestParam int page,
            @RequestParam int size) {

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.findAllByStatusAndName(name, pageable);
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @PutMapping("/active/{id}")
    public Product updateProductStatusActive(@PathVariable UUID id) {
        return productService.updateProductStatus(id, Status.Active);
    }

    @PutMapping("/deactive/{id}")
    public Product updateProductStatusDeactive(@PathVariable UUID id) {
        return productService.updateProductStatus(id, Status.Deactive);
    }


    @PostMapping("/upload")
    public ResponseEntity<List<ProductDTO>> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            List<ProductDTO> products = productService.saveProductsFromCSV(file);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

