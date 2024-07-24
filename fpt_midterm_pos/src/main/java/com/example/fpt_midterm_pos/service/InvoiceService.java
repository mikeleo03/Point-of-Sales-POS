package com.example.fpt_midterm_pos.service;

import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSearchCriteriaDTO;

public interface InvoiceService {
    Page<InvoiceDTO> findByCriteria(InvoiceSearchCriteriaDTO criteria, Pageable pageable);
    Invoice createInvoice(InvoiceDTO invoiceDTO);
    Invoice updateInvoice(UUID id, InvoiceDTO invoiceDTO) throws BadRequestException;
    void exportInvoiceToPDF(UUID id);
}