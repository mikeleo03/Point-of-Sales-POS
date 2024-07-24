package com.example.fpt_midterm_pos.service.impl;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Invoice;
import com.example.fpt_midterm_pos.data.model.InvoiceDetail;
import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;
import com.example.fpt_midterm_pos.data.repository.InvoiceDetailRepository;
import com.example.fpt_midterm_pos.data.repository.InvoiceRepository;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.dto.InvoiceDTO;
import com.example.fpt_midterm_pos.dto.InvoiceDetailDTO;
import com.example.fpt_midterm_pos.dto.InvoiceSearchCriteriaDTO;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.mapper.InvoiceMapper;
import com.example.fpt_midterm_pos.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    public Page<InvoiceDTO> findByCriteria(InvoiceSearchCriteriaDTO criteria, Pageable pageable) {
        // Listing all the criterias
        String customerName = criteria.getCustomerName();
        UUID customerId = criteria.getCustomerId();
        Date startDate = criteria.getStartDate();
        Date endDate = criteria.getEndDate();
        Integer month = criteria.getMonth();
        String sortByDate = criteria.getSortByDate();
        String sortByAmount = criteria.getSortByAmount();

        // Define the sort rules
        Sort sort = Sort.unsorted();
        if (sortByDate != null) {
            sort = sort.and(Sort.by("date").ascending());
            if (sortByDate.equalsIgnoreCase("desc")) {
                sort = sort.and(Sort.by("date").descending());
            }
        }
        if (sortByAmount != null) {
            sort = sort.and(Sort.by("amount").ascending());
            if (sortByAmount.equalsIgnoreCase("desc")) {
                sort = sort.and(Sort.by("amount").descending());
            }
        }

        // Set the pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        
        // Get the invoices data from the repo
        Page<Invoice> invoices = invoiceRepository.findByFilters(customerName, customerId, startDate, endDate, month, sortedPageable);
        return invoices.map(invoiceMapper::toInvoiceDTO);
    }

    @Override
    @Transactional
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        // 1. Select the customer
        // The main idea is by looking the invoice customer ID and browse on customer repo
        Customer customer = customerRepository.findById(invoiceDTO.getCustomer().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // 2. Add new invoice
        // Initialize a new invoice with initial value
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setAmount(0.00);    // Set the initial amount to 0.00
        invoice.setDate(invoiceDTO.getDate());
        invoice.setCreatedAt(new Date());
        invoice.setUpdatedAt(new Date());
        // Save the invoice
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 3. Add product to invoice
        // This can be done through invoice details
        double totalAmount = 0.00;
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();

        for (InvoiceDetailDTO detailDTO : invoiceDTO.getInvoiceDetails()) {
            // Check whether the product actually exists using the ID on the product repo
            Product product = productRepository.findById(detailDTO.getId().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            // Re-validate the product status
            if (product.getStatus() != Status.Active) {
                throw new IllegalArgumentException("Product is not active");
            }

            // If valid, then create invoice detail
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoice(savedInvoice);

            // Since the repo only returning active product, it already validated
            invoiceDetail.setProduct(product);
            invoiceDetail.setProductName(product.getName());
            invoiceDetail.setQuantity(detailDTO.getQuantity());
            invoiceDetail.setPrice(product.getPrice());
            invoiceDetail.setAmount(product.getPrice() * detailDTO.getQuantity());  // Amount = price * quantity
            invoiceDetails.add(invoiceDetail);

            // Update product quantity
            product.setQuantity(product.getQuantity() - detailDTO.getQuantity());
            productRepository.save(product);

            totalAmount += invoiceDetail.getAmount();
        }

        // Save all the invoice details
        invoiceDetailRepository.saveAll(invoiceDetails);

        // Update the invoice amount
        savedInvoice.setAmount(totalAmount);
        // Set list of products for the invoice
        savedInvoice.setInvoiceDetails(invoiceDetails);
        invoiceRepository.save(savedInvoice);

        return invoiceMapper.toInvoiceDTO(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceDTO updateInvoice(UUID id, InvoiceDTO invoiceDTO) throws BadRequestException {
        // Check if the invoice actually exists
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        // Check if the invoice is within the 10-minute editable window
        Instant createdAt = existingInvoice.getCreatedAt().toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(createdAt, now);
        if (duration.toMinutes() > 10) {
            throw new BadRequestException("Invoice can only be edited within 10 minutes of its creation");
        }

        // Update the invoice details
        existingInvoice.setDate(invoiceDTO.getDate());
        existingInvoice.setUpdatedAt(new Date());

        // Update invoice details
        double totalAmount = 0.00;
        List<InvoiceDetail> updatedInvoiceDetails = new ArrayList<>();

        for (InvoiceDetailDTO detailDTO : invoiceDTO.getInvoiceDetails()) {
            Product product = productRepository.findById(detailDTO.getId().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            if (product.getStatus() != Status.Active) {
                throw new BadRequestException("Product is not active");
            }

            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoice(existingInvoice);
            invoiceDetail.setProduct(product);
            invoiceDetail.setProductName(product.getName());
            invoiceDetail.setQuantity(detailDTO.getQuantity());
            invoiceDetail.setPrice(product.getPrice());
            invoiceDetail.setAmount(product.getPrice() * detailDTO.getQuantity());
            updatedInvoiceDetails.add(invoiceDetail);

            // Update product quantity
            product.setQuantity(product.getQuantity() - detailDTO.getQuantity());
            productRepository.save(product);

            totalAmount += invoiceDetail.getAmount();
        }

        invoiceDetailRepository.saveAll(updatedInvoiceDetails);

        // Update the invoice amount
        existingInvoice.setAmount(totalAmount);
        existingInvoice.setInvoiceDetails(updatedInvoiceDetails);
        invoiceRepository.save(existingInvoice);

        return invoiceMapper.toInvoiceDTO(existingInvoice);
    }

    @Override
    public void exportInvoiceToPDF(UUID id) {
        System.out.println("Haii!");
    }
}

