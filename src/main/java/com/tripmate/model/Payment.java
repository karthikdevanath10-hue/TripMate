package com.tripmate.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * ============================================================================
 * CORE OOP DEMONSTRATION: DATA ENCAPSULATION
 * ============================================================================
 * In this class:
 * 1. All member variables are marked PRIVATE to hide internal financial state.
 * 2. External classes CANNOT directly modify paymentStatus, advanceAmount,
 *    paymentDate, or transactionId.
 * 3. Public GETTERS allow safe read-only access.
 * 4. State transitions (such as marking a payment SUCCESS or FAILED) can ONLY
 *    occur through controlled, validated public methods like processPayment().
 * ============================================================================
 */
public class Payment {

    // Private fields - Encapsulated data
    private String paymentId;
    private final double advanceAmount = 200.0; // Fixed advance booking amount in INR
    private String paymentStatus;               // "PENDING", "SUCCESS", "FAILED"
    private String transactionId;
    private LocalDateTime paymentDate;
    private String paymentMethod;               // "Demo UPI / QR Scan"

    /**
     * Default constructor initializes payment in a safe PENDING state with a unique ID.
     */
    public Payment() {
        this.paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.paymentStatus = "PENDING";
        this.paymentMethod = "Demo UPI / QR Code";
    }

    /**
     * Controlled payment processing method.
     * Demonstrates Encapsulation: Ensures transactionId cannot be set arbitrarily
     * without passing validation and synchronizing payment status and timestamp.
     *
     * @param transactionId Reference number provided by the user/payment simulator
     * @return true if payment validation and processing succeed, false otherwise
     */
    public boolean processPayment(String transactionId) {
        if (transactionId != null && !transactionId.trim().isEmpty() && transactionId.trim().length() >= 6) {
            this.transactionId = transactionId.trim();
            this.paymentStatus = "SUCCESS";
            this.paymentDate = LocalDateTime.now();
            return true;
        } else {
            this.paymentStatus = "FAILED";
            return false;
        }
    }

    // ==========================================
    // Public Getters (Read-Only Access)
    // ==========================================

    public String getPaymentId() {
        return paymentId;
    }

    public double getAdvanceAmount() {
        return advanceAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getFormattedPaymentDate() {
        if (paymentDate == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return paymentDate.format(formatter);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSuccessful() {
        return "SUCCESS".equalsIgnoreCase(this.paymentStatus);
    }

    // NOTICE: There are NO public setters for advanceAmount, paymentStatus,
    // or transactionId. This strictly enforces DATA ENCAPSULATION!
}

