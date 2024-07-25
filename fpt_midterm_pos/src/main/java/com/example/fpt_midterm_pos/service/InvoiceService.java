package com.example.fpt_midterm_pos.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSaveDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSearchCriteriaDTO;
import com.example.fpt_midterm_pos.exception.BadRequestException;

public interface InvoiceService {
    Page<InvoiceDTO> findByCriteria(InvoiceSearchCriteriaDTO criteria, Pageable pageable);
    InvoiceDTO createInvoice(InvoiceSaveDTO invoiceDTO);
    InvoiceDTO updateInvoice(UUID id, InvoiceSaveDTO invoiceDTO) throws BadRequestException;
    void exportInvoiceToPDF(UUID id);
}