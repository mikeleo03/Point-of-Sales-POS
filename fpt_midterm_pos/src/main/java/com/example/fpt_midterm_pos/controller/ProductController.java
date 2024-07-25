package com.example.fpt_midterm_pos.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import com.example.fpt_midterm_pos.dto.ProductSearchCriteriaDTO;
import com.example.fpt_midterm_pos.dto.ProductShowDTO;
import com.example.fpt_midterm_pos.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Retrieve all Products with criteria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "Products not found")
    })
    @GetMapping
    public ResponseEntity<Page<ProductShowDTO>> getProductsByCriteria(ProductSearchCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductShowDTO> products = productService.findByCriteria(criteria, pageable);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @Operation(summary = "Create a new Product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductSaveDTO productSaveDTO) {
        ProductDTO productDTO = productService.save(productSaveDTO);
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Update existing Product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "204", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductSaveDTO productSaveDTO) {
        ProductDTO productDTO = productService.updateProduct(id, productSaveDTO);
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Update existing Product status from Deactive to Active.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully activated"),
        @ApiResponse(responseCode = "204", description = "Product not found")
    })
    @PutMapping("/active/{id}")
    public ResponseEntity<ProductDTO> updateProductStatusActive(@PathVariable UUID id) {
        ProductDTO productDTO = productService.updateProductStatus(id, Status.Active);
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Update existing Product status from Active to Deactive.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully deactivated"),
        @ApiResponse(responseCode = "204", description = "Product not found")
    })
    @PutMapping("/deactive/{id}")
    public ResponseEntity<ProductDTO> updateProductStatusDeactive(@PathVariable UUID id) {
        ProductDTO productDTO = productService.updateProductStatus(id, Status.Deactive);
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Import the Product data from Excel.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product imported successfully")
    })
    @PostMapping("/upload")
    public ResponseEntity<List<ProductDTO>> uploadCSV(@RequestParam("file") MultipartFile file) {
        List<ProductDTO> products = productService.saveProductsFromCSV(file);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
