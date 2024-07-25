package com.example.fpt_midterm_pos.service;

import java.io.IOException;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Workbook;
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
    byte[] exportInvoiceToPDF(UUID id) throws IOException;
    Workbook exportInvoiceToExcel(UUID id, int month, int year);

}