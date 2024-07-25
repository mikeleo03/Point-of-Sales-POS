package com.example.fpt_midterm_pos.controller;

import java.util.List;
import java.util.UUID;

import com.example.fpt_midterm_pos.dto.*;
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

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    public ResponseEntity<Page<ProductShowDTO>> getProductsByCriteria(ProductSearchCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductShowDTO> products = productService.findByCriteria(criteria, pageable);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(products);
    }


    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductSaveDTO productSaveDTO) {
        ProductDTO productDTO = productService.save(productSaveDTO);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductSaveDTO productSaveDTO) {
        ProductDTO productDTO = productService.updateProduct(id, productSaveDTO);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ProductDTO> updateProductStatusActive(@PathVariable UUID id) {
        ProductDTO productDTO = productService.updateProductStatus(id, Status.Active);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/deactive/{id}")
    public ResponseEntity<ProductDTO> updateProductStatusDeactive(@PathVariable UUID id) {
        ProductDTO productDTO = productService.updateProductStatus(id, Status.Deactive);
        return ResponseEntity.ok(productDTO);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<List<ProductSaveDTO>> uploadCSV(@RequestParam("file") MultipartFile file) {
//        try {
//            List<ProductSaveDTO> products = productService.saveProductsFromCSV(file);
//            return new ResponseEntity<>(products, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
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
