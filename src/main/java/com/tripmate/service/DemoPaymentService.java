package com.tripmate.service;

import com.tripmate.model.Payment;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Concrete implementation of PaymentService for college project demonstration.
 * Demonstrates ABSTRACTION by implementing the PaymentService interface and
 * strictly respecting DATA ENCAPSULATION by calling payment.processPayment().
 */
@Service
public class DemoPaymentService implements PaymentService {

    @Override
    public Payment initiatePayment() {
        return new Payment();
    }

    @Override
    public boolean validateTransactionReference(String transactionReference) {
        if (transactionReference == null) {
            return false;
        }
        String cleaned = transactionReference.trim();
        // Validation: Must be at least 6 characters and not empty
        return cleaned.length() >= 6 && cleaned.matches("^[a-zA-Z0-9/_-]+$");
    }

    @Override
    public boolean processPayment(Payment payment, String transactionReference) {
        if (payment == null) {
            return false;
        }
        if (!validateTransactionReference(transactionReference)) {
            return false;
        }
        // Encapsulation in action: The payment object validates and updates its own state
        return payment.processPayment(transactionReference.trim());
    }

    /**
     * Helper to simulate a generated demo UPI reference number for quick testing.
     */
    public String generateDemoTransactionId() {
        long randomSuffix = 10000000L + (long) (new Random().nextDouble() * 90000000L);
        return "UPI/DEMO/" + randomSuffix;
    }
}

