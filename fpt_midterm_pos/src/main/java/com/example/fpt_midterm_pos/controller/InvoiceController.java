package com.example.fpt_midterm_pos.controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSaveDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSearchCriteriaDTO;
import com.example.fpt_midterm_pos.dto.RevenueShowDTO;
import com.example.fpt_midterm_pos.service.InvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Operation(summary = "Retrieve all Invoices with criteria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "Invoices not found")
    })
    @GetMapping
    public ResponseEntity<Page<InvoiceDTO>> getInvoices(InvoiceSearchCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDTO> invoices = invoiceService.findByCriteria(criteria, pageable);

        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(invoices);
    }

    @Operation(summary = "Create report Revenue Invoice by year or month or day.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenue Report created successfully")
    })
    @GetMapping(value = "/revenue")
    public ResponseEntity<RevenueShowDTO> getRevenue(@RequestParam Date date, @RequestParam String revenueBy) {
        RevenueShowDTO revenue = invoiceService.getInvoicesRevenue(date, revenueBy);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    @Operation(summary = "Create a new Invoice.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully")
    })
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceSaveDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @Operation(summary = "Update existing Invoice.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable UUID id, @Valid @RequestBody InvoiceSaveDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedInvoice);
    }

    @Operation(summary = "Export the Invoice details data into PDF.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice exported successfully"),
        @ApiResponse(responseCode = "204", description = "Invoice not found")
    })
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportInvoiceToPDF(@PathVariable UUID id) throws IOException {
        byte[] pdfBytes = invoiceService.exportInvoiceToPDF(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("invoice.pdf").build());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(pdfBytes);
    }
}
