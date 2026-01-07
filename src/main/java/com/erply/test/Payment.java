package com.erply.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

//the api returns fields that we don't need, so we ignore them with @JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {
    private Long id;
    private Long invoiceId;
    private LocalDate opDate; // Payment date
    private Double sumPaid;
    private Long customerId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    
    public LocalDate getOpDate() { return opDate; }
    public void setOpDate(LocalDate opDate) { this.opDate = opDate; }
    
    public Double getSumPaid() { return sumPaid; }
    public void setSumPaid(Double sumPaid) { this.sumPaid = sumPaid; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
