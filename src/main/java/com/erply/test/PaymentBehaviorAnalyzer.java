package com.erply.test;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentBehaviorAnalyzer {

    public List<String> findCustomersWithWorsenedPayments(String token) {
        ErplyApiClient client = new ErplyApiClient(token);

        // Define periods
        String start2024 = "2024-01-01T00:00:00";
        String end2024 = "2024-12-31T23:59:59";
        // assuming current period starts from 2025-01-01
        String startCurrent = "2025-01-01T00:00:00";
        String endCurrent = "2025-12-31T23:59:59"; // Future proofing for the "current period"

        try {
            // Fetch Data
            System.out.println("Fetching 2024 invoices...");
            List<Invoice> invoices2024 = client.getInvoices(start2024, end2024);
            System.out.println("Fetched " + invoices2024.size() + " invoices for 2024.");

            System.out.println("Fetching Current invoices...");
            List<Invoice> invoicesCurrent = client.getInvoices(startCurrent, endCurrent);
            System.out.println("Fetched " + invoicesCurrent.size() + " invoices for Current period.");

            System.out.println("Fetching all payments...");
            List<Payment> allPayments = client.getPayments(start2024, endCurrent);
            allPayments.forEach(p -> System.out.println(p.getOpDate()));
            System.out.println("Fetched " + allPayments.size() + " payments.");

            // Index payments by Invoice ID
            Map<Long, List<Payment>> paymentsByInvoice = allPayments.stream()
                    .filter(p -> p.getInvoiceId() != null)
                    .collect(Collectors.groupingBy(Payment::getInvoiceId));

            // Identify Good Customers in 2024
            // Group 2024 invoices by Customer ID
            Map<Long, List<Invoice>> invoices2024ByCustomer = invoices2024.stream()
                    .collect(Collectors.groupingBy(Invoice::getCustomerId));

            Set<Long> goodCustomerIds2024 = new HashSet<>();

            for (Map.Entry<Long, List<Invoice>> entry : invoices2024ByCustomer.entrySet()) {
                Long customerId = entry.getKey();
                List<Invoice> invoices = entry.getValue();
                
                boolean allPaidOnTime = invoices.stream().allMatch(inv -> isPaidOnTime(inv, paymentsByInvoice.get(inv.getId())));
                if (allPaidOnTime && !invoices.isEmpty()) {
                    goodCustomerIds2024.add(customerId);
                }
            }
            System.out.println("Found " + goodCustomerIds2024.size() + " good customers from 2024.");

            // Check Current Period Behavior for these customers
            List<String> worsenedCustomers = new ArrayList<>();
            
            // Map customer IDs to names (using current invoices or 2024 invoices to find name)
            Map<Long, String> customerNames = new HashMap<>();
            invoices2024.forEach(inv -> customerNames.putIfAbsent(inv.getCustomerId(), inv.getCustomerName()));
            invoicesCurrent.forEach(inv -> customerNames.putIfAbsent(inv.getCustomerId(), inv.getCustomerName()));

            Map<Long, List<Invoice>> invoicesCurrentByCustomer = invoicesCurrent.stream()
                    .collect(Collectors.groupingBy(Invoice::getCustomerId));

            for (Long customerId : goodCustomerIds2024) {
                List<Invoice> currentInvoices = invoicesCurrentByCustomer.get(customerId);
                if (currentInvoices == null || currentInvoices.isEmpty()) {
                    continue; // No activity in current period, so behavior hasn't "worsened" per se, just stopped.
                }

                boolean hasLatePayment = currentInvoices.stream().anyMatch(inv -> isLate(inv, paymentsByInvoice.get(inv.getId())));
                
                if (hasLatePayment) {
                    worsenedCustomers.add(customerNames.getOrDefault(customerId, "Unknown Customer " + customerId));
                }
            }

            return worsenedCustomers;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private boolean isPaidOnTime(Invoice invoice, List<Payment> payments) {
        if (invoice.getDeadlineDate() == null) return true; // No deadline? Assume OK.
        
        if (payments == null || payments.isEmpty()) {
             return invoice.getSumPaid() != null && invoice.getSumPaid() >= invoice.getSumWithVat();
        }

        // Check date of last payment that completed the sum
        double needed = invoice.getSumWithVat();
        double paidSoFar = 0;
        java.time.LocalDate paidFullDate = null;

        // Sort payments by date
        payments.sort(Comparator.comparing(Payment::getOpDate));

        for (Payment p : payments) {
            paidSoFar += p.getSumPaid();
            if (paidSoFar >= needed - 0.01) { // tolerance
                paidFullDate = p.getOpDate();
                break;
            }
        }

        if (paidFullDate == null) {
            // Never fully paid
            return false;
        }

        return !paidFullDate.isAfter(invoice.getDeadlineDate());
    }

    private boolean isLate(Invoice invoice, List<Payment> payments) {
        // "paying invoices after the deadline"
        // 1. Paid, but active date > deadline
        // 2. Not paid, and current date > deadline (Overdue)
        
        if (invoice.getDeadlineDate() == null) return false;

        boolean isFullyPaid = false;
        java.time.LocalDate paidFullDate = null;

        if (payments != null) {
            double needed = invoice.getSumWithVat();
            double paidSoFar = 0;
            payments.sort(Comparator.comparing(Payment::getOpDate));
            for (Payment p : payments) {
                paidSoFar += p.getSumPaid();
                if (paidSoFar >= needed - 0.01) {
                    paidFullDate = p.getOpDate();
                    isFullyPaid = true;
                    break;
                }
            }
        }
        
        if (isFullyPaid) {
            return paidFullDate.isAfter(invoice.getDeadlineDate());
        } else {
            // Not fully paid. Is it overdue?
             // Compare current date (LocalDate) with deadline
            return java.time.LocalDate.now().isAfter(invoice.getDeadlineDate());
        }
    }
}
