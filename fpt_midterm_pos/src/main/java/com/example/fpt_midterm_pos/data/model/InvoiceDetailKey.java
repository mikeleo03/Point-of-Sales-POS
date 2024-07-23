package com.example.fpt_midterm_pos.data.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Embeddable
@EqualsAndHashCode
public class InvoiceDetailKey implements Serializable {
    private UUID invoiceId;
    private UUID productId;
}
