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

    /**
     * Retrieves all Invoices based on the provided search criteria.
     *
     * @param criteria The search criteria to filter the invoices.
     * @param page The page number to retrieve. Defaults to 0.
     * @param size The number of invoices to retrieve per page. Defaults to 20.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link InvoiceDTO} objects representing the retrieved invoices.
     * @apiNote If no invoices are found based on the provided criteria, a {@link ResponseEntity} with status code 204 (No Content) is returned.
     */
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

    /**
     * This method retrieves the revenue report for the specified date and revenue type (year, month, or day).
     *
     * @param date The date for which the revenue report is requested.
     * @param revenueBy The type of revenue to be included in the report (year, month, or day).
     * @return A {@link ResponseEntity} containing a {@link RevenueShowDTO} object representing the revenue report.
     * @apiNote If the revenue report is successfully generated, a {@link ResponseEntity} with status code 200 (OK) is returned.
     */
    @Operation(summary = "Create report Revenue Invoice by year or month or day.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenue Report created successfully")
    })
    @GetMapping(value = "/revenue")
    public ResponseEntity<RevenueShowDTO> getRevenue(@RequestParam Date date, @RequestParam String revenueBy) {
        RevenueShowDTO revenue = invoiceService.getInvoicesRevenue(date, revenueBy);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    /**
     * Creates a new Invoice.
     *
     * @param invoiceDTO The InvoiceSaveDTO object containing the details of the new Invoice to be created.
     * @return A ResponseEntity containing the newly created InvoiceDTO object and an HTTP status code of 201 (Created) upon successful creation.
     */
    @Operation(summary = "Create a new Invoice.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully")
    })
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceSaveDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    /**
     * Updates an existing Invoice with the provided InvoiceDTO object.
     *
     * @param id The unique identifier of the Invoice to be updated.
     * @param invoiceDTO The InvoiceSaveDTO object containing the details of the updated Invoice.
     * @return A ResponseEntity containing the updated InvoiceDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Invoice with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Invoice.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
        @ApiResponse(responseCode = "204", description = "Invoice not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable UUID id, @Valid @RequestBody InvoiceSaveDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedInvoice);
    }

    /**
     * Exports the Invoice details data into a PDF file.
     *
     * @param id The unique identifier of the Invoice to be exported.
     * @return A ResponseEntity containing the PDF data as a byte array.
     * @throws IOException If an error occurs while exporting the PDF.
     * @apiNote If the Invoice is successfully exported, a ResponseEntity with status code 200 (OK) is returned. If the Invoice is not found, a ResponseEntity with status code 204 (No Content) is returned.
     * @see InvoiceService#exportInvoiceToPDF(UUID)
     */
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
