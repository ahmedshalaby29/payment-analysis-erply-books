package com.erply.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ErplyApiClient {
    private final String apiToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://api.erplybooks.com/api";

    public ErplyApiClient(String apiToken) {
        this.apiToken = apiToken;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules(); 
    }

    public List<Invoice> getInvoices(String start, String end) {
        // According to docs: dateFrom, dateTo. default type is all? No, error says "Please add Document Type".
        // URL: /invoices?dateFrom=...&dateTo=...&documentType=DOCUMENT_SELL
        String url = String.format("%s/invoices?dateFrom=%s&dateTo=%s&documentType=DOCUMENT_SELL&limit=10000", BASE_URL, start, end);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-TOKEN", apiToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch invoices: " + response.statusCode() + " " + response.body());
            }

            EntityListResponse<Invoice> wrapper = objectMapper.readValue(response.body(), new TypeReference<EntityListResponse<Invoice>>() {});
            return wrapper.getItems() != null ? wrapper.getItems() : List.of();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching invoices", e);
        }
    }

    public List<Payment> getPayments(String start, String end) {
         String url = String.format("%s/payments?dateFrom=%s&dateTo=%s&limit=10000&getEverything=true", BASE_URL, start, end);
        try {
             HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-TOKEN", apiToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch payments: " + response.statusCode() + " " + response.body());
            }

            EntityListResponse<Payment> wrapper = objectMapper.readValue(response.body(), new TypeReference<EntityListResponse<Payment>>() {});
            return wrapper.getItems() != null ? wrapper.getItems() : List.of();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching payments", e);
        }
    }
}
