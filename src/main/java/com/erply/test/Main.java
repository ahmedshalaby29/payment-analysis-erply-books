package com.erply.test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read token from .env or args or hardcoded instructions
            // The task said "find the API_TOKEN in env file at root"
            // Path envPath = Path.of("../../.env"); // relative to src/main/java/com/erply/test ? No, running from root usually.
            // Let's assume running from project root
            
            // Read .env file manually
            String token = null;
            List<String> lines = Files.readAllLines(Paths.get("/Users/ahmedshalaby/Documents/ERPLY books/.env"));
            for (String line : lines) {
                if (line.startsWith("API_TOKEN=")) {
                    token = line.substring("API_TOKEN=".length()).trim();
                    break;
                }
            }

            if (token == null) {
                System.err.println("API_TOKEN not found in .env");
                return;
            }

            PaymentBehaviorAnalyzer analyzer = new PaymentBehaviorAnalyzer();
            List<String> customers = analyzer.findCustomersWithWorsenedPayments(token);

            System.out.println("Customers with worsened payment behavior:");
            for (String name : customers) {
                System.out.println("- " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
