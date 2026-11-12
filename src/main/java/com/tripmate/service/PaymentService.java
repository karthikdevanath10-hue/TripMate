package com.tripmate.service;

import com.tripmate.model.Payment;

/**
 * Demonstrates ABSTRACTION:
 * Defines the abstract interface contract for processing payments.
 * Allows decoupling business logic from any specific gateway implementation
 * (e.g. DemoPaymentService today, Razorpay/Stripe in the future).
 */
public interface PaymentService {

    /**
     * Creates and initializes a new Payment record in PENDING state.
     */
    Payment initiatePayment();

    /**
     * Validates the provided transaction reference.
     */
    boolean validateTransactionReference(String transactionReference);

    /**
     * Executes payment processing through the Payment object's encapsulated method.
     */
    boolean processPayment(Payment payment, String transactionReference);
}

