package com.erply.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;


//the api returns fields that we don't need, so we ignore them with @JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {
    private Long id;
    private String number;
    private LocalDate date;
    private LocalDate deadlineDate;
    private Long customerId;
    private String customerName;
    private Double sumWithVat;
    private Double sumPaid;
    private String typeCode; // e.g. DOCUMENT_SELL

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDate getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(LocalDate deadlineDate) { this.deadlineDate = deadlineDate; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Double getSumWithVat() { return sumWithVat; }
    public void setSumWithVat(Double sumWithVat) { this.sumWithVat = sumWithVat; }

    public Double getSumPaid() { return sumPaid; }
    public void setSumPaid(Double sumPaid) { this.sumPaid = sumPaid; }

    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
}
